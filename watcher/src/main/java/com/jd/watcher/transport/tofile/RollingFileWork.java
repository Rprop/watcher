
package com.jd.watcher.transport.tofile;

import com.jd.watcher.transport.tofile.helper.FileUtil;
import com.jd.watcher.transport.tofile.helper.FileSplitPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class RollingFileWork {
    private static Logger logger = LoggerFactory.getLogger(RollingFileWork.class);
    private final Lock lock = new ReentrantLock();
    private final String realPath;
    private File file;
    private FileOutputStream fileOutputStream;
    private BufferedOutputStream bufWriter;
    private FileSplitPolicy rollingPolicy;

    public RollingFileWork(String realPath, final String maxFileSize, final int maxIndex) {
        this.realPath = realPath;
        this.file = new File(realPath);
        this.bufWriter = this.openOutputStream();
        this.rollingPolicy = new FileSplitPolicy(realPath, maxFileSize, maxIndex);
    }

    /**
     * 将文件保存至日志
     *
     * @param lines 要存储的信息
     */
    public void append(String lines) {
        logger.info("记录消息：{}", lines);
        if (rollingPolicy.isTriggeringRollover(file, lines)) {
            this.rollover();
        }
        try {
            bufWriter.write(lines.getBytes("UTF-8"));
            bufWriter.flush();
        } catch (IOException e) {
            logger.error("写文件异常", e);
        }

    }

    /**
     * 文件进行重命名
     */
    private void rollover() {
        lock.lock();
        try {
            this.closeOutputStream();
            rollingPolicy.rollover();
            openOutputStream();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 关闭输出流
     */
    private void closeOutputStream() {
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            logger.error("关闭fileOutputStream异常！", e);
            fileOutputStream = null;
        }
        try {
            bufWriter.close();
        } catch (IOException e) {
            logger.error("bufWriter！", e);
            bufWriter = null;
        }
    }


    /**
     * @param
     * @return
     */
    private BufferedOutputStream openOutputStream() {
        try {
            File file = new File(realPath);
            boolean result = FileUtil.createMissingTargetDirsIfNecessary(file);
            if (!result) {
                logger.error("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            this.fileOutputStream = new FileOutputStream(new File(realPath), true);
            bufWriter = new BufferedOutputStream(fileOutputStream);
            return bufWriter;
        } catch (IOException e) {
            logger.error("Could not open [" + realPath + "]. Defaulting to System.err", e);
            return null;
        }
    }
}

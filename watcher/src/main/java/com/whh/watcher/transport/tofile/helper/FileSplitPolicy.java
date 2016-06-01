/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package com.whh.watcher.transport.tofile.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * SizeBasedTriggeringPolicy looks at size of the file being currently written
 * to. If it grows bigger than the specified size, the FileAppender using the
 * SizeBasedTriggeringPolicy rolls the file and creates a new one.
 * <p/>
 * For more information about this policy, please refer to the online manual at
 * http://logback.qos.ch/manual/appenders.html#SizeBasedTriggeringPolicy
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class FileSplitPolicy<E> {
    private static Logger logger = LoggerFactory.getLogger(FileSplitPolicy.class);
    private FileSize maxFileSize;
    private int maxIndex;
    private FileUtil util = new FileUtil();
    private final FileNamePattern fileNamePattern;
    private DefaultInvocationGate invocationGate = new DefaultInvocationGate();

    public FileSplitPolicy(final String fileNamePath, final String maxFileSize, int maxIndex) {
        this.maxFileSize = FileSize.valueOf(maxFileSize);
        this.maxIndex = maxIndex;
        if (maxIndex < 1) {
            maxIndex = 20;
        }
        fileNamePattern = new FileNamePattern(fileNamePath, maxIndex);
    }

    /**
     * 文件是否触发文件名滚动
     *
     * @param activeFile 当前文件
     * @param event
     * @return
     */
    public boolean isTriggeringRollover(final File activeFile, final E event) {
        long now = System.currentTimeMillis();
        if (invocationGate.isTooSoon(now)) {
            return false;
        }

        return (activeFile.length() >= maxFileSize.getSize());
    }

    /**
     * 文件名进行滚动
     */
    public void rollover() {
        if (maxIndex >= 0) {
            // Delete the oldest file, to keep Windows happy.
            File file = new File(fileNamePattern.convertInt(maxIndex));
            if (file.exists()) {
                file.delete();
            }
            // Map {(maxIndex - 1), ..., minIndex} to {maxIndex, ..., minIndex+1}
            for (int i = maxIndex - 1; i > 0; i--) {
                String toRenameStr = fileNamePattern.convertInt(i);
                File toRename = new File(toRenameStr);
                // no point in trying to rename an inexistent file
                if (toRename.exists()) {
                    util.rename(toRenameStr, fileNamePattern.convertInt(i + 1));
                } else {
                    logger.info("Skipping roll-over for inexistent file " + toRenameStr);
                }
            }
        }
    }

    /**
     * 按文件大小自动滚动文件名
     */
    class FileNamePattern {
        private List<String> files = new ArrayList<String>();

        public FileNamePattern(final String realPath, final int maxIndex) {
            files.add(realPath);
            int index = realPath.lastIndexOf(".");
            for (int i = 1; i < maxIndex; i++) {
                StringBuffer orig = new StringBuffer(realPath);
                if (index > 0) {
                    orig.insert(index, "." + i);
                } else {
                    orig.append(".").append(i);
                }
                files.add(orig.toString());
                logger.info("logging perhaps rollover to this：{}", orig.toString());
            }
        }

        /**
         * 获得元素的最小下标
         *
         * @param index
         * @return
         */
        public String convertInt(int index) {
            return files.get(index - 1);
        }
    }

}

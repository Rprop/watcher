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
package com.jd.watcher.transport.tofile.helper;

import com.jd.watcher.transport.tofile.RollingFileWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileUtil{
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    static final int BUF_SIZE = 32 * 1024;


    /**
     * Creates the parent directories of a file. If parent directories not
     * specified in file's path, then nothing is done and this returns
     * gracefully.
     *
     * @param file file whose parent directories (if any) should be created
     * @return {@code true} if either no parents were specified, or if all
     * parent directories were created successfully; {@code false} otherwise
     */
    static public boolean createMissingTargetDirsIfNecessary(File file) {
        File parent = file.getParentFile();
        if (parent == null) {
            // Parent directory not specified, therefore it's a request to
            // create nothing. Done! ;)
            return true;
        }

        // File.mkdirs() creates the parent directories only if they don't
        // already exist; and it's okay if they do.
        parent.mkdirs();
        return parent.exists();
    }

    /**
     * A relatively robust file renaming method which in case of failure due to
     * src and target being on different volumes, falls back onto
     * renaming by copying.
     *
     * @param src
     * @param target
     */
    public void rename(String src, String target) {
        if (src.equals(target)) {
            logger.error("Source and target files are the same [" + src + "]. Skipping.");
            return;
        }
        File srcFile = new File(src);

        if (srcFile.exists()) {
            File targetFile = new File(target);
            FileUtil.createMissingTargetDirsIfNecessary(targetFile);
            logger.error("Renaming file [" + srcFile + "] to [" + targetFile + "]");
            boolean result = srcFile.renameTo(targetFile);
            if (!result) {
                this.copy(src, target);
                if (!srcFile.delete()) {
                    logger.error("Could not delete " + src);
                }
                logger.error("Please consider leaving the [file] option of " + RollingFileWork.class.getSimpleName() + " empty.");
            }
        } else {
            throw new RuntimeException("File [" + src + "] does not exist.");
        }
    }

    public void copy(String src, String destination) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(src));
            bos = new BufferedOutputStream(new FileOutputStream(destination));
            byte[] inbuf = new byte[BUF_SIZE];
            int n;

            while ((n = bis.read(inbuf)) != -1) {
                bos.write(inbuf, 0, n);
            }

            bis.close();
            bis = null;
            bos.close();
            bos = null;
        } catch (IOException ioe) {
            String msg = "Failed to copy [" + src + "] to [" + destination + "]";
            logger.error(msg, ioe);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    // ignore
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}

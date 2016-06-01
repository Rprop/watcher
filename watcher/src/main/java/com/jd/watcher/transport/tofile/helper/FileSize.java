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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Instances of this class represent the size of a file. Internally, the size is
 * stored as long.>
 * <p/>
 * <p>The {@link #valueOf} method can convert strings such as "3 kb", "5 mb", into
 * FileSize instances. The recognized unit specifications for file size are the
 * "kb", "mb", and "gb". The unit name may be followed by an "s". Thus, "2 kbs"
 * and "2 kb" are equivalent. In the absence of a time unit specification, byte
 * is assumed.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class FileSize {
    private final static String LENGTH_PART = "([0-9]+)";
    private final static int DOUBLE_GROUP = 1;
    private final static String UNIT_PART = "(|k|m|g)s?";
    private final static int UNIT_GROUP = 2;
    private static final Pattern FILE_SIZE_PATTERN = Pattern.compile(LENGTH_PART + "\\s*" + UNIT_PART, Pattern.CASE_INSENSITIVE);

    static public final long KB_COEFFICIENT = 1024;
    static public final long MB_COEFFICIENT = 1024 * KB_COEFFICIENT;
    static public final long GB_COEFFICIENT = 1024 * MB_COEFFICIENT;

    final long size;

    public FileSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public static FileSize valueOf(String fileSizeStr) {
        Matcher matcher = FILE_SIZE_PATTERN.matcher(fileSizeStr.toLowerCase());
        long coefficient;
        if (matcher.matches()) {
            String lenStr = matcher.group(DOUBLE_GROUP);
            String unitStr = matcher.group(UNIT_GROUP);

            long lenValue = Long.valueOf(lenStr);
            if (unitStr.equalsIgnoreCase("")) {
                coefficient = 1;
            } else if (unitStr.equalsIgnoreCase("k")) {
                coefficient = KB_COEFFICIENT;
            } else if (unitStr.equalsIgnoreCase("m")) {
                coefficient = MB_COEFFICIENT;
            } else if (unitStr.equalsIgnoreCase("g")) {
                coefficient = GB_COEFFICIENT;
            } else {
                throw new IllegalStateException("Unexpected " + unitStr);
            }
            return new FileSize(lenValue * coefficient);
        } else {
            return new FileSize(10 * 1024 * 1024);
        }
    }

    @Override
    public String toString() {
        long inKB = size / KB_COEFFICIENT;
        if (inKB == 0)
            return size + " Bytes";
        long inMB = size / MB_COEFFICIENT;
        if (inMB == 0) {
            return inKB + " KB";
        }
        return inMB + " MB";
    }
}

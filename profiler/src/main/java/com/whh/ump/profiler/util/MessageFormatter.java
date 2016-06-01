/**
 * Copyright (c) 2004-2011 QOS.ch
 * All rights reserved.
 * <p/>
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 * <p/>
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.whh.ump.profiler.util;

import java.text.MessageFormat;

// contributors: lizongbo: proposed special treatment of array parameter values
// Joern Huxhorn: pointed out double[] omission, suggested deep array copy

/**
 * Formats messages according to very simple substitution rules. Substitutions
 * can be made 1, 2 or more arguments.
 *
 * <p>
 * For example,
 *
 * <pre>
 * MessageFormatter.format(&quot;Hi {}.&quot;, &quot;there&quot;)
 * </pre>
 *
 * will return the string "Hi there.".
 * <p>
 * The {} pair is called the <em>formatting anchor</em>. It serves to designate
 * the location where arguments need to be substituted within the message
 * pattern.
 * <p>
 * In case your message contains the '{' or the '}' character, you do not have
 * to do anything special unless the '}' character immediately follows '{'. For
 * example,
 *
 * <pre>
 * MessageFormatter.format(&quot;Set {1,2,3} is not equal to {}.&quot;, &quot;1,2&quot;);
 * </pre>
 *
 * will return the string "Set {1,2,3} is not equal to 1,2.".
 *
 * <p>
 * If for whatever reason you need to place the string "{}" in the message
 * without its <em>formatting anchor</em> meaning, then you need to escape the
 * '{' character with '\', that is the backslash character. Only the '{'
 * character should be escaped. There is no need to escape the '}' character.
 * For example,
 *
 * <pre>
 * MessageFormatter.format(&quot;Set \\{} is not equal to {}.&quot;, &quot;1,2&quot;);
 * </pre>
 *
 * will return the string "Set {} is not equal to 1,2.".
 *
 * <p>
 * The escaping behavior just described can be overridden by escaping the escape
 * character '\'. Calling
 *
 * <pre>
 * MessageFormatter.format(&quot;File name is C:\\\\{}.&quot;, &quot;file.zip&quot;);
 * </pre>
 *
 * will return the string "File name is C:\file.zip".
 *
 * <p>
 * The formatting conventions are different than those of {@link java.text.MessageFormat}
 * which ships with the Java platform. This is justified by the fact that
 * SLF4J's implementation is 10 times faster than that of {@link java.text.MessageFormat}.
 * This local performance difference is both measurable and significant in the
 * larger context of the complete logging processing chain.
 *
 * <p>
 * See also {@link #format(String, Object)},
 * {@link #format(String, Object, Object)} and
 * {@link #arrayFormat(String, Object[])} methods for more details.
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author Joern Huxhorn
 */
final public class MessageFormatter {
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    /**
     * Same principle as the {@link #format(String, Object)} and
     * {@link #format(String, Object, Object)} methods except that any number of
     * arguments can be passed in an array.
     *
     * @param messagePattern
     *          The message pattern which will be parsed and formatted
     * @param argArray
     *          An array of arguments to be substituted in place of formatting
     *          anchors
     * @return The formatted message
     */
     public final static String arrayFormat(final String messagePattern, final Object[] argArray) {
        if (messagePattern == null) {
            return null;
        }

        if (argArray == null) {
            return messagePattern;
        }

        int i = 0;
        int j;
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 64);

        int L;
        for (L = 0; L < argArray.length; L++) {

            j = messagePattern.indexOf(DELIM_STR, i);

            if (j == -1) {
                // no more variables
                if (i == 0) { // this is a simple string
                    return messagePattern;
                } else { // add the tail string which contains no variables and return
                    // the result.
                    sbuf.append(messagePattern.substring(i, messagePattern.length()));
                    return sbuf.toString();
                }
            } else {
                if (isEscapedDelimeter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        L--; // DELIM_START was escaped, thus should not be incremented
                        sbuf.append(messagePattern.substring(i, j - 1));
                        sbuf.append(DELIM_START);
                        i = j + 1;
                    } else {
                        // The escape character preceding the delimiter start is
                        // itself escaped: "abc x:\\{}"
                        // we have to consume one backward slash
                        sbuf.append(messagePattern.substring(i, j - 1));
                        deeplyAppendParameter(sbuf, argArray[L]);
                        i = j + 2;
                    }
                } else {
                    // normal case
                    sbuf.append(messagePattern.substring(i, j));
                    deeplyAppendParameter(sbuf, argArray[L]);
                    i = j + 2;
                }
            }
        }
        // append the characters following the last {} pair.
        sbuf.append(messagePattern.substring(i, messagePattern.length()));
        return sbuf.toString();
    }

    final static boolean isEscapedDelimeter(String messagePattern,
                                            int delimeterStartIndex) {

        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        if (potentialEscape == ESCAPE_CHAR) {
            return true;
        } else {
            return false;
        }
    }

    final static boolean isDoubleEscaped(String messagePattern,
                                         int delimeterStartIndex) {
        if (delimeterStartIndex >= 2
                && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR) {
            return true;
        } else {
            return false;
        }
    }

    // special treatment of array values was suggested by 'lizongbo'
    private static void deeplyAppendParameter(StringBuilder sbuf, Object o) {
        if (o == null) {
            sbuf.append("null");
            return;
        }

        safeObjectAppend(sbuf, o);
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        } catch (Throwable t) {
        }
    }
}

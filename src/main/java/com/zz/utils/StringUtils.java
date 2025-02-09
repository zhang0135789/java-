/**
 * $Revision: 13635 $
 * $Date: 2013-05-04 02:55:48 -0500 (Sat, 04 May 2013) $
 * <p>
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zz.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class to peform common String manipulation algorithms.
 */
@Slf4j
public class StringUtils {


    private static final String XUEXIN_IMG_PARTURL = ".xuexin.org.cn/";// 图片替换路径

    // Constants used by escapeHTMLTags
    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
    private static final char[] AMP_ENCODE = "&amp;".toCharArray();
    private static final char[] LT_ENCODE = "&lt;".toCharArray();
    private static final char[] GT_ENCODE = "&gt;".toCharArray();

    private StringUtils() {
        // Not instantiable.
    }

    /**
     * Replaces all instances of oldString with newString in string.
     *
     * @param string the String to search to perform replacements on.
     * @param oldString the String that should be replaced by newString.
     * @param newString the String that will replace all instances of oldString.
     * @return a String will all instances of oldString replaced by newString.
     */
    public static String replace(String string, String oldString, String newString) {
        if (string == null) {
            return null;
        }
        int i = 0;
        // Make sure that oldString appears at least once before doing any
        // processing.
        if ((i = string.indexOf(oldString, i)) >= 0) {
            // Use char []'s, as they are more efficient to deal with.
            char[] string2 = string.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(string2.length);
            buf.append(string2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            // Replace all remaining instances of oldString with newString.
            while ((i = string.indexOf(oldString, i)) > 0) {
                buf.append(string2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(string2, j, string2.length - j);
            return buf.toString();
        }
        return string;
    }

    /**
     * Replaces all instances of oldString with newString in line with the added
     * feature that matches of newString in oldString ignore case.
     *
     * @param line the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @return a String will all instances of oldString replaced by newString
     */
    public static String replaceIgnoreCase(String line, String oldString, String newString) {
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    /**
     * Replaces all instances of oldString with newString in line with the added
     * feature that matches of newString in oldString ignore case. The count
     * paramater is set to the number of replaces performed.
     *
     * @param line the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @param count a value that will be updated with the number of replaces
     *            performed.
     * @return a String will all instances of oldString replaced by newString
     */
    public static String replaceIgnoreCase(String line, String oldString, String newString, int[] count) {
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            int counter = 1;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }

    /**
     * Replaces all instances of oldString with newString in line. The count
     * Integer is updated with number of replaces.
     *
     * @param line the String to search to perform replacements on.
     * @param oldString the String that should be replaced by newString.
     * @param newString the String that will replace all instances of oldString.
     * @return a String will all instances of oldString replaced by newString.
     */
    public static String replace(String line, String oldString, String newString, int[] count) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            int counter = 1;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }

    /**
     * This method takes a string and strips out all tags except <br>
     * tags while still leaving the tag body intact.
     *
     * @param in the text to be converted.
     * @return the input string with all tags removed.
     */
    public static String stripTags(String in) {
        if (in == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = in.toCharArray();
        int len = input.length;
        StringBuilder out = new StringBuilder((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
            } else if (ch == '<') {
                if (i + 3 < len && input[i + 1] == 'b' && input[i + 2] == 'r' && input[i + 3] == '>') {
                    i += 3;
                    continue;
                }
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
            } else if (ch == '>') {
                last = i + 1;
            }
        }
        if (last == 0) {
            return in;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * This method takes a string which may contain HTML tags (ie, &lt;b&gt;,
     * &lt;table&gt;, etc) and converts the '&lt'' and '&gt;' characters to
     * their HTML escape sequences. It will also replace LF with &lt;br&gt;.
     *
     * @param in the text to be converted.
     * @return the input string with the characters '&lt;' and '&gt;' replaced
     *         with their HTML escape sequences.
     */
    public static String escapeHTMLTags(String in) {
        return escapeHTMLTags(in, true);
    }

    /**
     * This method takes a string which may contain HTML tags (ie, &lt;b&gt;,
     * &lt;table&gt;, etc) and converts the '&lt'' and '&gt;' characters to
     * their HTML escape sequences.
     *
     * @param in the text to be converted.
     * @param includeLF set to true to replace \n with <br>
     *            .
     * @return the input string with the characters '&lt;' and '&gt;' replaced
     *         with their HTML escape sequences.
     */
    public static String escapeHTMLTags(String in, boolean includeLF) {
        if (in == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = in.toCharArray();
        int len = input.length;
        StringBuilder out = new StringBuilder((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
            } else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            } else if (ch == '>') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(GT_ENCODE);
            } else if (ch == '\n' && includeLF == true) {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append("<br>");
            }
        }
        if (last == 0) {
            return in;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * Used by the hash method.
     */
    private static Map<String, MessageDigest> digests = new ConcurrentHashMap<String, MessageDigest>();

    /**
     * Hashes a String using the Md5 algorithm and returns the result as a
     * String of hexadecimal numbers. This method is synchronized to avoid
     * excessive MessageDigest object creation. If calling this method becomes a
     * bottleneck in your code, you may wish to maintain a pool of MessageDigest
     * objects instead of using this method.
     * <p/>
     * A hash is a one-way function -- that is, given an input, an output is
     * easily computed. However, given the output, the input is almost
     * impossible to compute. This is useful for passwords since we can store
     * the hash and a hacker will then have a very hard time determining the
     * original password.
     * <p/>
     * In Jive, every time a user logs in, we simply take their plain text
     * password, compute the hash, and compare the generated hash to the stored
     * hash. Since it is almost impossible that two passwords will generate the
     * same hash, we know if the user gave us the correct password or not. The
     * only negative to this system is that password recovery is basically
     * impossible. Therefore, a reset password method is used instead.
     *
     * @param data the String to compute the hash of.
     * @return a hashed version of the passed-in String
     */
    public static String hash(String data) {
        return hash(data, "MD5");
    }

    /**
     * Hashes a String using the specified algorithm and returns the result as a
     * String of hexadecimal numbers. This method is synchronized to avoid
     * excessive MessageDigest object creation. If calling this method becomes a
     * bottleneck in your code, you may wish to maintain a pool of MessageDigest
     * objects instead of using this method.
     * <p/>
     * A hash is a one-way function -- that is, given an input, an output is
     * easily computed. However, given the output, the input is almost
     * impossible to compute. This is useful for passwords since we can store
     * the hash and a hacker will then have a very hard time determining the
     * original password.
     * <p/>
     * In Jive, every time a user logs in, we simply take their plain text
     * password, compute the hash, and compare the generated hash to the stored
     * hash. Since it is almost impossible that two passwords will generate the
     * same hash, we know if the user gave us the correct password or not. The
     * only negative to this system is that password recovery is basically
     * impossible. Therefore, a reset password method is used instead.
     *
     * @param data the String to compute the hash of.
     * @param algorithm the name of the algorithm requested.
     * @return a hashed version of the passed-in String
     */
    public static String hash(String data, String algorithm) {
        try {
            return hash(data.getBytes("UTF-8"), algorithm);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return data;
    }

    /**
     * Hashes a byte array using the specified algorithm and returns the result
     * as a String of hexadecimal numbers. This method is synchronized to avoid
     * excessive MessageDigest object creation. If calling this method becomes a
     * bottleneck in your code, you may wish to maintain a pool of MessageDigest
     * objects instead of using this method.
     * <p/>
     * A hash is a one-way function -- that is, given an input, an output is
     * easily computed. However, given the output, the input is almost
     * impossible to compute. This is useful for passwords since we can store
     * the hash and a hacker will then have a very hard time determining the
     * original password.
     * <p/>
     * In Jive, every time a user logs in, we simply take their plain text
     * password, compute the hash, and compare the generated hash to the stored
     * hash. Since it is almost impossible that two passwords will generate the
     * same hash, we know if the user gave us the correct password or not. The
     * only negative to this system is that password recovery is basically
     * impossible. Therefore, a reset password method is used instead.
     *
     * @param bytes the byte array to compute the hash of.
     * @param algorithm the name of the algorithm requested.
     * @return a hashed version of the passed-in String
     */
    public static String hash(byte[] bytes, String algorithm) {
        synchronized (algorithm.intern()) {
            MessageDigest digest = digests.get(algorithm);
            if (digest == null) {
                try {
                    digest = MessageDigest.getInstance(algorithm);
                    digests.put(algorithm, digest);
                } catch (NoSuchAlgorithmException nsae) {
                    log.error("Failed to load the " + algorithm + " MessageDigest. "
                            + "Jive will be unable to function normally.", nsae);
                    return null;
                }
            }
            // Now, compute hash.
            digest.update(bytes);
            return encodeHex(digest.digest());
        }
    }

    /**
     * Turns an array of bytes into a String representing each byte as an
     * unsigned hex number.
     * <p/>
     * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
     * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
     * Distributed under LGPL.
     *
     * @param bytes an array of bytes to convert to a hex-string
     * @return generated hex string
     */
    public static String encodeHex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        int i;

        for (i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }

    /**
     * Turns a hex encoded string into a byte array. It is specifically meant to
     * "reverse" the toHex(byte[]) method.
     *
     * @param hex a hex encoded String to transform into a byte array.
     * @return a byte array representing the hex String[
     */
    public static byte[] decodeHex(String hex) {
        char[] chars = hex.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int byteCount = 0;
        for (int i = 0; i < chars.length; i += 2) {
            int newByte = 0x00;
            newByte |= hexCharToByte(chars[i]);
            newByte <<= 4;
            newByte |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = (byte) newByte;
            byteCount++;
        }
        return bytes;
    }

    /**
     * Returns the the byte value of a hexadecmical char (0-f). It's assumed
     * that the hexidecimal chars are lower case as appropriate.
     *
     * @param ch a hexedicmal character (0-f)
     * @return the byte value of the character (0x00-0x0F)
     */
    private static byte hexCharToByte(char ch) {
        switch (ch) {
            case '0':
                return 0x00;
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return 0x03;
            case '4':
                return 0x04;
            case '5':
                return 0x05;
            case '6':
                return 0x06;
            case '7':
                return 0x07;
            case '8':
                return 0x08;
            case '9':
                return 0x09;
            case 'a':
                return 0x0A;
            case 'b':
                return 0x0B;
            case 'c':
                return 0x0C;
            case 'd':
                return 0x0D;
            case 'e':
                return 0x0E;
            case 'f':
                return 0x0F;
        }
        return 0x00;
    }

    /**
     * Encodes a String as a base64 String.
     *
     * @param data a String to encode.
     * @return a base64 encoded String.
     */
//    public static String encodeBase64(String data) {
//        byte[] bytes = null;
//        try {
//            bytes = data.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException uee) {
//            log.error(uee.getMessage(), uee);
//        }
//        return encodeBase64(bytes);
//    }

    /**
     * Encodes a byte array into a base64 String.
     *
     * @param data a byte array to encode.
     * @return a base64 encode String.
     */
//    public static String encodeBase64(byte[] data) {
//        // Encode the String. We pass in a flag to specify that line
//        // breaks not be added. This is consistent with our previous base64
//        // implementation. Section 2.1 of 3548 (base64 spec) also specifies
//        // no line breaks by default.
//        return Base64.encodeBytes(data, Base64.DONT_BREAK_LINES);
//    }

    /**
     * Decodes a base64 String.
     *
     * @param data a base64 encoded String to decode.
     * @return the decoded String.
     */
//    public static byte[] decodeBase64(String data) {
//        return Base64.decode(data);
//    }

    /**
     * Converts a line of text into an array of lower case words using a
     * BreakIterator.wordInstance().
     * <p>
     *
     * This method is under the Jive Open Source Software License and was
     * written by Mark Imbriaco.
     *
     * @param text a String of text to convert into an array of words
     * @return text broken up into an array of words.
     */
    public static String[] toLowerCaseWordArray(String text) {
        if (text == null || text.length() == 0) {
            return new String[0];
        }

        List<String> wordList = new ArrayList<String>();
        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(text);
        int start = 0;

        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
            String tmp = text.substring(start, end).trim();
            // Remove characters that are not needed.
            tmp = replace(tmp, "+", "");
            tmp = replace(tmp, "/", "");
            tmp = replace(tmp, "\\", "");
            tmp = replace(tmp, "#", "");
            tmp = replace(tmp, "*", "");
            tmp = replace(tmp, ")", "");
            tmp = replace(tmp, "(", "");
            tmp = replace(tmp, "&", "");
            if (tmp.length() > 0) {
                wordList.add(tmp);
            }
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    /**
     * Pseudo-random number generator object for use with randomString(). The
     * Random class is not considered to be cryptographically secure, so only
     * use these random Strings for low to medium security applications.
     */
    private static Random randGen = new Random();

    /**
     * Array of numbers and letters of mixed case. Numbers appear in the list
     * twice so that there is a more equal chance that a number will be picked.
     * We can use the array to get a random number or letter by picking a random
     * array index.
     */
    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
            + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

    /**
     * Returns a random String of numbers and letters (lower and upper case) of
     * the specified length. The method uses the Random class that is built-in
     * to Java which is suitable for low to medium grade security uses. This
     * means that the output is only pseudo random, i.e., each number is
     * mathematically generated so is not truly random.
     * <p>
     * <p/>
     * The specified length must be at least one. If not, the method will return
     * null.
     *
     * @param length the desired length of the random String to return.
     * @return a random String of numbers and letters of the specified length.
     */
    public static String randomString(int length) {
        if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    /**
     * Intelligently chops a String at a word boundary (whitespace) that occurs
     * at the specified index in the argument or before. However, if there is a
     * newline character before <code>length</code>, the String will be chopped
     * there. If no newline or whitespace is found in <code>string</code> up to
     * the index <code>length</code>, the String will chopped at
     * <code>length</code>.
     * <p/>
     * For example, chopAtWord("This is a nice String", 10) will return
     * "This is a" which is the first word boundary less than or equal to 10
     * characters into the original String.
     *
     * @param string the String to chop.
     * @param length the index in <code>string</code> to start looking for a
     *            whitespace boundary at.
     * @return a substring of <code>string</code> whose length is less than or
     *         equal to <code>length</code>, and that is chopped at whitespace.
     */
    public static String chopAtWord(String string, int length) {
        if (string == null || string.length() == 0) {
            return string;
        }

        char[] charArray = string.toCharArray();
        int sLength = string.length();
        if (length < sLength) {
            sLength = length;
        }

        // First check if there is a newline character before length; if so,
        // chop word there.
        for (int i = 0; i < sLength - 1; i++) {
            // Windows
            if (charArray[i] == '\r' && charArray[i + 1] == '\n') {
                return string.substring(0, i + 1);
            }
            // Unix
            else if (charArray[i] == '\n') {
                return string.substring(0, i);
            }
        }
        // Also check boundary case of Unix newline
        if (charArray[sLength - 1] == '\n') {
            return string.substring(0, sLength - 1);
        }

        // Done checking for newline, now see if the total string is less than
        // the specified chop point.
        if (string.length() < length) {
            return string;
        }

        // No newline, so chop at the first whitespace.
        for (int i = length - 1; i > 0; i--) {
            if (charArray[i] == ' ') {
                return string.substring(0, i).trim();
            }
        }

        // Did not find word boundary so return original String chopped at
        // specified length.
        return string.substring(0, length);
    }

    /**
     * Escapes all necessary characters in the String so that it can be used in
     * SQL
     *
     * @param string the string to escape.
     * @return the string with appropriate characters escaped.
     */
    public static String escapeSQL(String string) {
        if (string == null) {
            return null;
        } else if (string.length() == 0) {
            return string;
        }

        char ch;
        char[] input = string.toCharArray();
        int i = 0;
        int last = 0;
        int len = input.length;
        StringBuilder out = null;
        for (; i < len; i++) {
            ch = input[i];

            if (ch == '\'') {
                if (out == null) {
                    out = new StringBuilder(len + 2);
                }
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append('\'').append('\'');
            }
        }

        if (out == null) {
            return string;
        } else if (i > last) {
            out.append(input, last, i - last);
        }

        return out.toString();
    }

    /**
     * Escapes all necessary characters in the String so that it can be used in
     * SQL
     *
     * @param  the string to escape.
     * @return the string with appropriate characters escaped.
     */
    public static String escapeForSQL(String str) {
        // System.out.println(str);
        // if (str.indexOf("'") >= 0)
        // str = str.replaceAll("'", "\\\\'");
        // if (str.indexOf("\"") >= 0)
        // str = str.replaceAll("\"", "\\\\\"");
        // System.out.println(str);

        return str;
    }

    /**
     * Escapes all necessary characters in the String so that it can be used in
     * an XML doc.
     *
     * @param string the string to escape.
     * @return the string with appropriate characters escaped.
     */
    public static String escapeForXML(String string) {
        if (string == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuilder out = new StringBuilder((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
            } else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            } else if (ch == '&') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(AMP_ENCODE);
            } else if (ch == '"') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(QUOTE_ENCODE);
            }
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * Unescapes the String by converting XML escape sequences back into normal
     * characters.
     *
     * @param string the string to unescape.
     * @return the string with appropriate characters unescaped.
     */
    public static String unescapeFromXML(String string) {
        string = replace(string, "&lt;", "<");
        string = replace(string, "&gt;", ">");
        string = replace(string, "&quot;", "\"");
        return replace(string, "&amp;", "&");
    }

    private static final char[] zeroArray = "0000000000000000000000000000000000000000000000000000000000000000"
            .toCharArray();

    /**
     * Pads the supplied String with 0's to the specified length and returns the
     * result as a new String. For example, if the initial String is "9999" and
     * the desired length is 8, the result would be "00009999". This type of
     * padding is useful for creating numerical values that need to be stored
     * and sorted as character data. Note: the current implementation of this
     * method allows for a maximum <tt>length</tt> of 64.
     *
     * @param string the original String to pad.
     * @param length the desired length of the new padded String.
     * @return a new String padded with the required number of 0's.
     */
    public static String zeroPadString(String string, int length) {
        if (string == null || string.length() > length) {
            return string;
        }
        StringBuilder buf = new StringBuilder(length);
        buf.append(zeroArray, 0, length - string.length()).append(string);
        return buf.toString();
    }

    /**
     * Formats a Date as a fifteen character long String made up of the Date's
     * padded millisecond value.
     *
     * @return a Date encoded as a String.
     */
    public static String dateToMillis(Date date) {
        return zeroPadString(Long.toString(date.getTime()), 15);
    }

    /**
     * Returns a formatted String from time.
     *
     * @param diff the amount of elapsed time.
     * @return the formatte String.
     */
    public static String getTimeFromLong(long diff) {
        final String HOURS = "h";
        final String MINUTES = "min";
        // final String SECONDS = "sec";

        final long MS_IN_A_DAY = 1000 * 60 * 60 * 24;
        final long MS_IN_AN_HOUR = 1000 * 60 * 60;
        final long MS_IN_A_MINUTE = 1000 * 60;
        final long MS_IN_A_SECOND = 1000;
        // Date currentTime = new Date();
        // long numDays = diff / MS_IN_A_DAY;
        diff = diff % MS_IN_A_DAY;
        long numHours = diff / MS_IN_AN_HOUR;
        diff = diff % MS_IN_AN_HOUR;
        long numMinutes = diff / MS_IN_A_MINUTE;
        diff = diff % MS_IN_A_MINUTE;
        // long numSeconds = diff / MS_IN_A_SECOND;
        diff = diff % MS_IN_A_SECOND;
        // long numMilliseconds = diff;

        StringBuffer buf = new StringBuffer();
        if (numHours > 0) {
            buf.append(numHours + " " + HOURS + ", ");
        }

        if (numMinutes > 0) {
            buf.append(numMinutes + " " + MINUTES);
        }

        // buf.append(numSeconds + " " + SECONDS);

        String result = buf.toString();

        if (numMinutes < 1) {
            result = "< 1 minute";
        }

        return result;
    }

    /**
     * Returns a collection of Strings as a comma-delimitted list of strings.
     *
     * @return a String representing the Collection.
     */
    public static String collectionToString(Collection<String> collection) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        String delim = "";
        for (String element : collection) {
            buf.append(delim);
            buf.append(element);
            delim = ",";
        }
        return buf.toString();
    }

    /**
     * Returns a comma-delimitted list of Strings as a Collection.
     *
     * @return a Collection representing the String.
     */
    public static Collection<String> stringToCollection(String string) {
        if (string == null || string.trim().length() == 0) {
            return Collections.emptyList();
        }
        Collection<String> collection = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(string, ",");
        while (tokens.hasMoreTokens()) {
            collection.add(tokens.nextToken().trim());
        }
        return collection;
    }

    /**
     * Abbreviates a string to a specified length and then adds an ellipsis if
     * the input is greater than the maxWidth. Example input:
     *
     * <pre>
     *      user1@jivesoftware.com/home
     * </pre>
     *
     * and a maximum length of 20 characters, the abbreviate method will return:
     *
     * <pre>
     *      user1@jivesoftware.c...
     * </pre>
     *
     * @param str the String to abbreviate.
     * @param maxWidth the maximum size of the string, minus the ellipsis.
     * @return the abbreviated String, or <tt>null</tt> if the string was
     *         <tt>null</tt>.
     */
    public static String abbreviate(String str, int maxWidth) {
        if (null == str) {
            return null;
        }

        if (str.length() <= maxWidth) {
            return str;
        }

        return str.substring(0, maxWidth) + "...";
    }

    /**
     * Returns true if the string passed in is a valid Email address.
     *
     * @param address Email address to test for validity.
     * @return true if the string passed in is a valid email address.
     */
//    public static boolean isValidEmailAddress(String address) {
//        if (address == null) {
//            return false;
//        }
//
//        if (!address.contains("@")) {
//            return false;
//        }
//
//        try {
//            InternetAddress.parse(address);
//            return true;
//        } catch (AddressException e) {
//            return false;
//        }
//    }

    /**
     * Removes characters likely to enable Cross Site Scripting attacks from the
     * provided input string. The characters that are removed from the input
     * string, if present, are:
     *
     * <pre>
     * &lt; &gt; &quot; ' % ; ) ( &amp; + -
     * </pre>
     *
     * @param input the string to be scrubbed
     * @return Input without certain characters;
     */
    public static String removeXSSCharacters(String input) {
        final String[] xss = {"<", ">", "\"", "'", "%", ";", ")", "(", "&", "+", "-"};
        for (int i = 0; i < xss.length; i++) {
            input = input.replace(xss[i], "");
        }
        return input;
    }

    /**
     * Returns the UTF-8 bytes for the given String, suppressing
     * UnsupportedEncodingException (in lieu of log message)
     *
     * @param input The source string
     * @return The UTF-8 encoding for the given string
     */
    public static byte[] getBytes(String input) {
        try {
            return input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException uee) {
            log.warn("Unable to encode string using UTF-8: " + input);
            return input.getBytes(); // default encoding
        }
    }

    /**
     * Returns the UTF-8 String for the given byte array, suppressing
     * UnsupportedEncodingException (in lieu of log message)
     *
     * @param input The source byte array
     * @return The UTF-8 encoded String for the given byte array
     */
    public static String getString(byte[] input) {
        try {
            return new String(input, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            String result = new String(input); // default encoding
            log.warn("Unable to decode byte array using UTF-8: " + result);
            return result;
        }
    }

    /**
     * MD5
     */
    public static String getMD5Str(final String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    public static boolean isNull(String str) {
        if (str != null && !str.isEmpty() && !"null".equals(str.toLowerCase()))
            return false;
        else
            return true;
    }

    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat dd = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = dd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*
     * 返回无减号的UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 字符串列表转换成以“，”分隔的字符串
     *
     * @param list
     * @return
     * @version:v1.0
     * @author:李大鹏
     * @date:2014-3-13 下午1:25:21
     */
    public static String listToString(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (String str : list) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 字符串转list
     *
     * @param str 被转字符串
     * @param rex 分隔符
     * @return
     * @version:v1.0
     * @author:李大鹏
     * @date:2014-3-13 下午2:50:53
     */
    public static List<String> stringToList(String str, String rex) {
        List<String> resList = new ArrayList<String>();
        String[] strArr = str.split(rex);
        for (String tmpStr : strArr) {
            resList.add(tmpStr);
        }
        return resList;
    }

    /**
     * 是否是支持群聊的版本
     *
     * @return
     * @version:v1.0
     * @author:李大鹏
     * @date:2014-4-8 下午8:59:24
     */
    public static boolean versionCompare(String userVersion, String supportVersion) {
        boolean isSupport = false;
        try {
            if (userVersion != null) {
                String[] userVerArr = userVersion.split("\\.");
                String[] supportVerArr = supportVersion.split("\\.");
                int userVerNum = 0, supportVerNum = 0;
                userVerNum = getVersionNum(userVerArr);
                supportVerNum = getVersionNum(supportVerArr);
                isSupport = userVerNum >= supportVerNum;
            }
        } catch (Exception e) {
        }
        return isSupport;
    }

    public static Integer getVersionNum(String[] versionArr) {
        Integer versionNum = 0;
        if (null != versionArr && 0 < versionArr.length && 5 > versionArr.length) {
            Integer step = 1000000;
            for (String version : versionArr) {
                if (StringUtils.isNull(version)) {
                    version = "0";
                }
                versionNum = Integer.valueOf(version) * step + versionNum;
                step = step / 100;
            }
        }
        return versionNum;
    }

    /**
     * 数组转化为字符串
     *
     * @param arr
     * @return
     * @version:v1.0
     * @author:高青柏
     * @date:2014-5-20 上午10:55:51
     */
    public static String arrToStr(String[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < arr.length; i++) {
                sb.append(arr[i]);
            }
            return sb.toString();
        }
    }

    /**
     * 检查fir字符串里面是否有字符“不存在”于sec，若有，返回false,否则返回true eg:fir=AB sec=ABC 则返回true
     * fir=AB sec=ACD 则返回false(因为B不存在于sec里面)
     *
     * @param fir
     * @param sec
     * @return
     * @version:v1.0
     * @author:高青柏
     * @date:2014-5-22 下午2:43:16
     */
    public static Boolean isExist(String fir, String sec) {
        if (fir == null || sec == null) {
            return false;
        }
        for (int index = 0; index < fir.length(); index++) {
            if (sec.indexOf(fir.charAt(index)) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int chineseLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 主要是处理“1.0”显示在为“1”
     *
     * @param d
     * @return
     * @version:v2.0
     * @author:高青柏
     * @date:2014-7-11 下午2:57:48
     */
    public static String doubleToStr(Double d) {
        String str = d.toString();
        if (str != null && !"".equals(str)) {
            if (str.endsWith(".0")) {
                str = str.substring(0, str.lastIndexOf(".0"));
                return str;
            }
            return str;
        }
        return "";
    }

    /**
     * 计算百分比
     *
     * @param numerator
     * @param denominator
     * @return
     * @version:v2.0
     * @author:高青柏
     * @date:2014-7-22 下午4:34:56
     */
    public static String calcPercent(double numerator, double denominator) {
        double fen = numerator / denominator;
        DecimalFormat df = new DecimalFormat("##0.00%"); // ##.00%
        return df.format(fen);
    }

    /**
     * 字符串转list
     *
     * @param str 被转的字符串
     * @param separator 分隔符
     * @return
     * @version:v1.0
     * @author:李大鹏
     * @date:2014-7-23 下午2:40:45
     */
    public static List<String> strToList(String str, String separator) {
        String[] strArr = str.split(separator);
        List<String> strList = new ArrayList<String>();
        for (String tmpStr : strArr) {
            strList.add(tmpStr);
        }
        return strList;
    }

    /**
     * 替换str里面的key，并用下划线从1开始标记
     *
     * @param str
     * @param key
     * @return
     * @version:v2.0
     * @author:高青柏
     * @date:2014-8-8 上午10:22:43
     */
    public static String replaceAllLine(String str, String key) {
        if (str == null || "".equals(str)) {
            return "";
        }
        int index = 1;
        while (str.indexOf(key) != -1) {
            str = str.replaceFirst(key, "<B><u>" + index + "</u></B>");
            index++;
        }
        return str;
    }

    public static String[] subarray(String[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return new String[0];
        }

        String[] subarray = new String[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * 判断num是否在奇偶数之间，比如:[2,7,12,18],num=4,则返回true，num=8，返回false
     *
     * @param arr
     * @param num
     * @return
     * @version:v2.0
     * @author:高青柏
     * @date:2014-8-28 上午11:19:46
     */
    public static Boolean isExist(String[] arr, int num) {
        if (arr == null || arr.length <= 0) {
            return false;
        }
        for (int i = 0; i < arr.length; i++) {
            if (i % 2 == 0) {
                try {
                    if (num >= Integer.parseInt(arr[i]) && i < arr.length - 1 && num <= Integer.parseInt(arr[i + 1])) {
                        return true;
                    }
                } catch (NumberFormatException nfe) {
                    return false;
                }
            }

        }

        return false;
    }

    /**
     * eg [2,7,12,16],num=7,返回true 即，num是数组下标为偶数的元素值，则返回true,否则返回false
     *
     * @param arr
     * @param num
     * @return
     * @version:v2.0
     * @author:高青柏
     * @date:2014-8-28 下午2:52:58
     */
    public static Boolean isInArr(String[] arr, int num) {
        if (arr == null || arr.length <= 0) {
            return false;
        }
        for (int i = 0; i < arr.length; i++) {
            if (i % 2 == 1) {
                if (num == Integer.parseInt(arr[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String right(String input, int count) {
        if (isNull(input)) {
            return "";
        }
        count = (count > input.length()) ? input.length() : count;
        return input.substring(input.length() - count, input.length());
    }

    public static String html(String content) {
        if (content == null)
            return "";
        String html = content;
        html = StringUtils.replace(html, "'", "&apos;");
        html = StringUtils.replace(html, "\"", "&quot;");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
        // html = StringUtils.replace(html, " ", "&nbsp;");// 替换空格
        html = StringUtils.replace(html, "<", "&lt;");
        html = StringUtils.replace(html, ">", "&gt;");
        return html;
    }

    /**
     * 还原库中的图片路径
     * 方法描述
     *
     * @param imgUrl
     * @return
     * @version:v1.0
     * @author:zxl
     * @date:2015-9-15 下午1:59:58
     */
    public static String restoreImgUrl(String imgUrl) {
        if (!StringUtils.isNull(imgUrl)) {
            StringBuffer returnStr = new StringBuffer();
            if (imgUrl.indexOf("http") == -1) {
                returnStr.append("http://");
            }
            returnStr.append(imgUrl.replace("@", XUEXIN_IMG_PARTURL));
            return returnStr.toString();
        } else {
            return imgUrl;
        }
    }

    public static void main(String[] args) {
        System.out.println(versionCompare("2.1.1", "2"));
        System.out.println(versionCompare("2.2.1", "2.2"));
        System.out.println(versionCompare("2.2.1", "2.2.2"));
        System.out.println(versionCompare("2.3", "2.2.2.2"));
        System.out.println(versionCompare("2.3.2", "2.2.."));
        System.out.println(versionCompare("2.2.1.", "2.2.."));
    }
}

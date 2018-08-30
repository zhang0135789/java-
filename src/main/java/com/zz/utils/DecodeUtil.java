package com.zz.utils;

/**
 * @Author: zz
 * @Description: DecodeUtil
 * @Date:  10:07 2018/5/29 0029
 * @Modified By
 */
public class DecodeUtil {

    /**
     * Unicode  decode
     * @param dataStr   Unicode
     * @return
     */
    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); //
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

}

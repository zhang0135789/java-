package com.zz.utils;

/**
 * @Author: zz
 * @Description:
 * @Date:  9:40 2018/6/22 0022
 * @Modified By
 */
public class CharsetUtil {

    /**
     * byte 2 string£¨byte £©
     * @param b byte[]
     * @param n  Î»Êý
     * @return
     */
    public static String byte2Str(byte b , int n){
        String result ="";
        byte a = b;
        for (int i = 0; i < n; i++){
            result = (a % 2) + result;
            a=(byte)(a>>1);
        }
        return result;
    }


    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    /**
     * byte[] 2 hex
     * @param bytes
     * @return
     */
    public static String bytes2Hex(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { //
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        return new String(buf);
    }

    /**
     * byte 2  int
     * @param b
     * @return
     */
    public static int byte2int(byte b) {
        return b & 0xff;
    }




}

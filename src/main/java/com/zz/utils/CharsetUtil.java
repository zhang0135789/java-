package com.zz.utils;

/**
 * @Author: zz
 * @Description:  字符转换工具类
 * @Date: 上午 9:40 2018/6/22 0022
 * @Modified By
 */
public class CharsetUtil {

    /**
     * 把byte数组转化成2进制字符串
     * @param b byte[]
     * @return
     */
    public static String bytes2Str(byte b){
        String result ="";
        byte a = b;
        for (int i = 0; i < 8; i++){
            result = (a % 2) + result;
            a=(byte)(a>>1);
        }
        return result;
    }


    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    /**
     * 把byte数组转换成16进制字符串
     * @param bytes
     * @return
     */
    public static String bytes2Hex(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        return new String(buf);
    }

    /**
     * byte 转换成  int
     * @param b
     * @return
     */
    public static int byte2int(byte b) {
        return b & 0xff;
    }




}

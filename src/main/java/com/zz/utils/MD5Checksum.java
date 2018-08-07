package com.zz.utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: zz
 * @Description:  generate MD5check code
 * @Date:  10:56 2018/7/20 0020
 * @Modified By
 */
public class MD5Checksum {
    private static Logger LOGGER = LoggerFactory.getLogger(MD5Checksum.class);

    private static byte[] createChecksum(String filename) {
        InputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead = -1;

            while ((numRead = fis.read(buffer)) != -1) {
                complete.update(buffer, 0, numRead);
            }
            return complete.digest();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return null;

    }

    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String getMD5Checksum(String filename) {

        if (!new File(filename).isFile()) {
            LOGGER.error("Error: " + filename
                    + " is not a valid file.");
            return null;
        }
        byte[] b = createChecksum(filename);
        if(null == b){
            LOGGER.error("Error:create md5 string failure!");
            return null;
        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return result.toString();

    }

    public static void main(String args[]) {
        try {
            long beforeTime = System.currentTimeMillis();
            String path = "C:\\Users\\user\\Desktop\\work_shedule.txt";
            String before = "999E42920C54CF7D66190731CD54F0E6".toLowerCase();
            String md5 = getMD5Checksum(path);
            System.out.println(md5);
            System.out.println(md5.equals(before));

            File file = new File(path);

            System.out.println(path+ "'s size is : " +file.length()+" bytes, it consumes " + (System.currentTimeMillis() - beforeTime) + " ms.");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}

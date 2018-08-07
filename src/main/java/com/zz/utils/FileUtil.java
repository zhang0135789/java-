package com.zz.utils;

import com.zz.beans.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @Author: zz
 * @Description:  fileUtil
 * @Date:  3:39 2018/5/28 0028
 * @Modified By
 */
public class FileUtil {


    public static void main(String[] args) {
        ObjectOutputStream oos = null;

        User o = new User();
        try {
            oos = new ObjectOutputStream(
                    new FileOutputStream("123.users"));

            oos.writeObject(o);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.zz.test;

import com.zz.utils.DecodeUtil;
import org.junit.Test;

/**
 * @Author: zz
 * @Description:
 * @Date: œ¬ŒÁ 6:04 2018/5/28 0028
 * @Modified By
 */
public class TestDemo {

    @Test
    public void test_Œª‘ÀÀ„() {
        System.out.println(8|4 );
//        System.out.println("6 & 3 = " + (6 & 3) );
    }

    @Test
    public void test_str() {
        String str2 = DecodeUtil.decodeUnicode("\\u5E10\\u53F7");
        System.out.println(str2);


    }


    public static void main(String[] args) {
        System.out.println("12 & 2 = " + (12 & 2) );
    }




}

package com.zz.test;

import com.zz.utils.CharsetUtil;
import org.junit.Test;

/**
 * @Author: zz
 * @Description:
 * @Date: 上午 9:29 2018/8/30 0030
 * @Modified By
 */
public class CharsetTest {

    @Test
    public void CharsetTest() {
        String st = CharsetUtil.byte2Str((byte)127 , 8);
        System.out.println(st);
    }
}

package com.zz.test;

import one.inve.mnemonic.Mnemonic;
import org.junit.Test;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 2:18 2018/8/7 0007
 * @Modified By
 */
public class MnemonicTest {

    @Test
    public void testAddress() throws Exception {
        Mnemonic mn = new Mnemonic("");
        mn.getxPrivKey("");
        String address = mn.getAddress();
        System.out.println(address);
    }
}

package com.zz.test;

import one.inve.mnemonic.Mnemonic;
import one.inve.utils.DSA;
import one.inve.utils.SignUtil;
import org.bitcoinj.core.ECKey;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 3:26 2018/9/22 0022
 * @Modified By
 */
public class SignUtilTest {
    @Test
    public void signTestDeno1() throws Exception {
        Mnemonic mn = new Mnemonic("","");
        byte[] privateKey = mn.getxPrivKey();
        System.out.println(Arrays.toString(privateKey));
        byte[] puKey = mn.getPubKey();


        BigInteger priKBig = mn.getxPrivKeyBig();
        System.out.println(priKBig);
        ECKey key = ECKey.fromPrivate(priKBig);

//        byte[] output = key.sign();


        String unit = "{\"unit\":{\"walletId\":\"anBEaWwydzN0M0RsSmdrWXZ4d0g5b0xYQXFvTDZzQWFHckRseG5Uc09HWWs=\",\"unit\":\"THA2MVMzZURPSDFkNmF0ZFlEd2pwS1hRdGxCeDYyb2x3WWlTT0hGTlZkZk0=\",\"alt\":\"3\",\"messages\":[{\"app\":\"payment\",\"payload_location\":\"inline\",\"payload\":{\"outputs\":[{\"amount\":300000,\"address\":\"MG1Y5TISRCVY7MBBFETRZGNMGGE8VMDR\"},{\"amount\":9877,\"address\":\"NZ7QOTJBSNP0W2HYN7LEDQUT3T1THSND\"}],\"inputs\":[{\"unit\":\"UVNOTTVDMT12K0tLR0ZXcG9Bb3RWZHRKMTVXMDdMK3hyUTJqZlBsN2xFaFE=\",\"message_index\":0,\"output_index\":1}]},\"payload_hash\":\"2r8sJ+JHbf8eMQM0TnvIbPYti733vKh6s14rHB1nJkA=\"}],\"payload_commission\":197,\"version\":\"1.0dev\",\"headers_commission\":262,\"authors\":[{\"authentifiers\":{\"r\":\"AN/2JSB0dbAVdQ1+W07QJz9lqdBRq8deyrhdn8GRHXwvXzvSbHboustuBm3BSfMP9Z/3y1huGYUb\\r\\njODUJKLwdDQ=\"},\"address\":\"NZ7QOTJBSNP0W2HYN7LEDQUT3T1THSND\",\"definition\":[\"sig\",{\"pubkey\":\"AvIYCi7oxbOxp1L2ek3a1Z5zJAKRTWPpoZmbuQWslx14\"}]}],\"timestamp\":1530839419}}";

//        byte[] pub = "AnvtSPZg8gWJQR5W0tV69DNoi6Bol30bo1RClBhpChH8".getBytes();

        System.out.println("用私钥对信息进行数字签名");


        //进行签名
        String sinStr = SignUtil.signStr(unit , key);

        System.out.println(sinStr);
        unit = "{\"unit\":{\"walletId\":\"aTVBQ1NMTkcyM2JEZXBMTkl5SmVNazloMDVtNTg1PXp5NXFWUk9RTWduV0g=\",\"unit\":\"M1ZWNTBoNUZqeFdpQllZTkpuMnlzTkZoNlJCeDVVdzNxYjBGZW9QRTdRRlU=\",\"alt\":\"3\",\"messages\":[{\"app\":\"payment\",\"payload_location\":\"inline\",\"payload\":{\"outputs\":[{\"amount\":300000,\"address\":\"LPBBQFI5WJ586KTE3530CNVDUZOA2RZR\"},{\"amount\":9877,\"address\":\"A4SZKLF3MGUHJDY8ME72CAN6TXRE61E4\"}],\"inputs\":[{\"unit\":\"dHc5NjhlY09jeHZZPXk1Z1dKVHpmUkpWRTlid29yYXJiczhEN0g9Z2c3Ymo=\",\"message_index\":0,\"output_index\":1}]},\"payload_hash\":\"2r8sJ+JHbf8eMQM0TnvIbPYti733vKh6s14rHB1nJkA=\"}],\"payload_commission\":197,\"version\":\"1.0dev\",\"headers_commission\":262,\"authors\":[{\"authentifiers\":{\"r\":\"ZYFM1Aqs0b32f3NVBtiI4BVfMlIrHzaR3nMYKK7aoTNduL6jFghML23Cz7C1zOGR56kxvit4Vpo/\\r\\nMFodWCQUrQ==\"},\"address\":\"A4SZKLF3MGUHJDY8ME72CAN6TXRE61E4\",\"definition\":[\"sig\",{\"pubkey\":\"A3tTpgEzx6DFJtXaE5QZILgh8ZWq/u3HiAhowR+9PRcs\"}]}],\"timestamp\":1530839419}}";


        //解签名
        boolean flag = SignUtil.Verify(unit);
        System.out.println(flag);
    }



    @Test
    public void signTest() throws Exception {
        Mnemonic mn = new Mnemonic("","");
        byte[] privateKey = mn.getxPrivKey();

        System.out.println(Arrays.toString(privateKey));
        byte[] puKey = mn.getPubKey();

        BigInteger priKBig = mn.getxPrivKeyBig();
        System.out.println(priKBig);

        ECKey key = ECKey.fromPrivate(privateKey);


        System.out.println("用私钥对信息进行数字签名");


        String str = "lee sang";
        //进行签名
        String sinStr = SignUtil.signStr(str , key);

        System.out.println(sinStr);

        byte[] pubkey = mn.getPubKey();
        String aaa = DSA.encryptBASE64(pubkey);
        ECKey pubk = ECKey.fromPublicOnly(pubkey);

        //解签名
        boolean flag = SignUtil.verifyStr(str , sinStr, pubk);
        System.out.println(flag);

    }

    @Test
    public void VerifyTest() throws Exception {
        String unit = "";
        unit =
                "{\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"toAddress\":\"KL3M65WEDDZ7VHBB2TT7PSDNBOK4TWAG\",\"amount\":\"1000000\",\"timestamp\":1539592739,\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"fee\":\"0\",\"type\":1,\"signature\":\"iM7XyUliWijLMkYpYMiYhUBhDl9juaFU0jyVqbcMSilyMcAiogEsc9+lHp9r/Bwke0tDT8Zrx1S6HQgZAGKV5w==\"}"
        ;
        Mnemonic mn = new Mnemonic("shield salmon sport horse cool hole pool panda embark wrap fancy equip","");
        mn.getAddress();


        boolean flag = false;

//        while (result) {
        try {
            flag = SignUtil.verify(unit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!flag)
            ;
        System.out.println(flag);
//        }

        System.out.println("over");

    }
}

package com.zz.ntru;

import ntrusign.Ntrusign;
import one.inve.utils.NTRU;
import org.junit.Test;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 2:14 2018/9/28 0028
 * @Modified By
 */
public class NtruTest {


    @Test
    public void demo1NtruTest() {

        System.out.println(System.getProperty("java.library.path"));

        Ntrusign nt = new Ntrusign();
        nt.InitialKey("");

        byte[] pri = nt.GetPrivateKey();
        byte[] pub = nt.GetPublicKey();
        String msg = "11111111";

        byte[] signature = NTRU.sign(msg.getBytes(),pri);
        boolean verify = NTRU.verify(signature, msg.getBytes(), pub);

        System.out.println(verify);


        String msgs = "Whatever is worth doing is worth doing well";



        NTRU.generateNtru("");
        byte[] pris = NTRU.getPrivateKey();
        byte[] pris2 = NTRU.getPrivateKey();
        byte[] pubs = NTRU.getPublicKey();
        byte[] buf_2_sign = msgs.getBytes();

        byte[] signatures = NTRU.sign(buf_2_sign,pris);
        boolean rs = NTRU.verify(signatures,buf_2_sign,pubs);
        System.out.println("===========");
        System.out.println(rs);

    }


}

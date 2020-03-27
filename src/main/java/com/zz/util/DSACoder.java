package com.zz.util;

import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Author: zz
 * @Description:
 * @Date: 8:35 PM 3/24/20
 * @Modified By
 */
public class DSACoder {


    public static void main(String[] args) throws Exception {
        String data = "跳梁小豆tlxd666";




        //创建秘钥生成器
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        kpg.initialize(512);
        KeyPair keypair = kpg.generateKeyPair();//生成秘钥对
        DSAPublicKey publickey = (DSAPublicKey)keypair.getPublic();
        DSAPrivateKey privatekey = (DSAPrivateKey)keypair.getPrivate();

        //签名和验证
        //签名
        Signature sign = Signature.getInstance("SHA1withDSA");
        sign.initSign(privatekey);//初始化私钥，签名只能是私钥
        sign.update(data.getBytes());//更新签名数据
        byte[] b = sign.sign();//签名，返回签名后的字节数组


        //验证
        sign.initVerify(publickey);//初始化公钥，验证只能是公钥
        sign.update(data.getBytes());//更新验证的数据
        boolean result = sign.verify(b);//签名和验证一致返回true  不一致返回false
        System.out.println(result);
    }
}

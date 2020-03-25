package com.zz.util;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;



/**
 * @Author: zz
 * @Description:  密码类工具类   在使用的时候  需要引入maven
 *               <!--比特币核心算法-->
 *         <dependency>
 *             <groupId>org.bitcoinj</groupId>
 *             <artifactId>bitcoinj-core</artifactId>
 *             <version>0.14.7</version>
 *             <scope>compile</scope>
 *         </dependency>
 *
 * @Date: 7:05 PM 3/22/20
 * @Modified By
 */
public class CryptoUtils {

    private static final String DEFAULT_SEED = "$%^*%^()(HJG8awfjas7";

    private static final String KEY_ALGORITHM = "DSA";

    private static final String SIGNATURE_ALGORITHM = "DSA";

    /**
     * 生成密钥
     * @param seed 种子
     * @return 密钥对
     * @throws Exception
     */
    public static KeyPair initDSAKey(String seed) throws Exception {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed.getBytes());
        //Modulus size must range from 512 to 1024 and be a multiple of 64
        keygen.initialize(768, secureRandom);

        KeyPair keys = keygen.genKeyPair();
        return keys;
    }

    /**
     * 生成默认密钥
     * @return 密钥对象
     * @throws Exception
     */
    public static KeyPair initDSAKey() throws Exception {
        return initDSAKey(DEFAULT_SEED);
    }


    /**
     * 使用DSA进行签名
     * @param data
     * @param privateKey
     * @return
     */
    public static String signByDSA(byte[] data , String privateKey) throws Exception {
        byte[] keyBytes = decryptBASE64(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = factory.generatePrivate(keySpec);//生成 私钥

        //用私钥对信息进行数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }

    /**
     * 使用DSA进行验证
     * @param data
     * @param publicKey
     * @param sign
     * @return
     */
    public static boolean verifyByDSA(byte[] data, String publicKey, String sign) throws Exception  {
        byte[] keyBytes = decryptBASE64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(decryptBASE64(sign)); //验证签名
    }


    /**
     * BASE64Encoder 加密
     * @param data 要加密的数据
     * @return 加密后的字符串
     */
    public static String encryptBASE64(byte[] data) {
        Base64.Encoder encoder = Base64.getEncoder();
        String encode = encoder.encodeToString(data).replace("\r\n","");
        return encode;
    }

    /**
     * BASE64Decoder 解密
     * @param data 要解密的字符串
     * @return 解密后的byte[]
     * @throws Exception
     */
    public static byte[] decryptBASE64(String data)  {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] buffer = decoder.decode(data.replace("\r\n",""));
//        BASE64Decoder decoder = new BASE64Decoder();
//        byte[] buffer = decoder.decodeBuffer(data);
        return buffer;
    }


//    public static void main(String[] args) throws Exception {
//        KeyPair keyPair = initDSAKey();
//        byte[] data = "hello world".getBytes();
//        byte[] privateKey = keyPair.getPrivate().getEncoded();
//        byte[] publicKey = keyPair.getPublic().getEncoded();
//
//
//        String signature = signByDSA(data , encryptBASE64(privateKey));
//
//
//        boolean flag = verifyByDSA(data , encryptBASE64(publicKey) , signature);
//
//        System.out.println(flag);
//    }


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

        //生成秘钥对
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
        keyPairGenerator.initialize(256, new SecureRandom());

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        return keyPair;

    }


    public static   byte[] sign(byte[] data,PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }


    public static boolean verify(byte[] data,PublicKey publicKey, byte[] sign) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(sign);
    }


        //获取公钥(Base64编码)
    public static String getPublicKey(KeyPair keyPair) {

        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();

        byte[] bytes = publicKey.getEncoded();

        return encryptBASE64(bytes);

    }

        //获取私钥(Base64编码)

    public static String getPrivateKey(KeyPair keyPair) {

        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();

        byte[] bytes = privateKey.getEncoded();

        return encryptBASE64(bytes);

    }

        //将Base64编码后的公钥转换成PublicKey对象

    public static ECPublicKey string2PublicKey(String pubStr) throws Exception {

        byte[] keyBytes = decryptBASE64(pubStr);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");

        ECPublicKey publicKey = (ECPublicKey) keyFactory.generatePublic(keySpec);

        return publicKey;

    }

        //将Base64编码后的私钥转换成PrivateKey对象

    public static ECPrivateKey string2PrivateKey(String priStr) throws Exception {

        byte[] keyBytes = decryptBASE64(priStr);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");

        ECPrivateKey privateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);

        return privateKey;

    }


    //公钥加密
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception {

        Cipher cipher = Cipher.getInstance("ECIES", "BC");

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytes = cipher.doFinal(content);

        return bytes;

    }


    //私钥解密
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {

        Cipher cipher = Cipher.getInstance("ECIES", "BC");

        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] bytes = cipher.doFinal(content);

        return bytes;

    }

//        public static void main(String[] args) throws Exception {
//
//            KeyPair keyPair = CryptoUtils.getKeyPair();
//
//            String publicKeyStr = CryptoUtils.getPublicKey(keyPair);
//
//            String privateKeyStr = CryptoUtils.getPrivateKey(keyPair);
//
//            System.out.println("ECC公钥Base64编码:" + publicKeyStr);
//
//            System.out.println("ECC私钥Base64编码:" + privateKeyStr);
//
//            ECPublicKey publicKey = string2PublicKey(publicKeyStr);
//
//            ECPrivateKey privateKey = string2PrivateKey(privateKeyStr);
//
//            byte[] publicEncrypt = publicEncrypt("hello world".getBytes(), publicKey);
//
//            byte[] privateDecrypt = privateDecrypt(publicEncrypt, privateKey);
//
//            System.out.println(new String(privateDecrypt));
//
//
//            byte[] data = "123456".getBytes();
//            byte[] sign = CryptoUtils.sign(data, keyPair.getPrivate());
//            boolean verify = CryptoUtils.verify(data, keyPair.getPublic(), sign);
//            System.out.println(verify);
//        }




}

package com.zz.util;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Author: zz
 * @Description:
 * @Date: 11:24 PM 3/24/20
 * @Modified By
 */
public class RSACoder {


    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    /**
     * 签名
     * @param plainText
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public static String sign(String plainText, PrivateKey privateKey) throws NoSuchAlgorithmException, UnsupportedEncodingException, SignatureException, InvalidKeyException {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes("UTF-8"));
        byte[] signature = privateSignature.sign();
        return CryptoUtils.encryptBASE64(signature);
    }

    /**
     * 验证
     * @param plainText
     * @param signature
     * @param publicKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     */
    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes("UTF-8"));
        byte[] signatureBytes = CryptoUtils.decryptBASE64(signature);
        return publicSignature.verify(signatureBytes);
    }

    /**
     * 转换PrivateKey
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes;
        keyBytes = CryptoUtils.decryptBASE64(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 转换PublicKey
     * @param publicKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(CryptoUtils.decryptBASE64(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }



    public static void main(String[] args) throws Exception {


        KeyPair pair = generateKeyPair();
        byte[] pribyte = pair.getPrivate().getEncoded();
        String pri = CryptoUtils.encryptBASE64(pribyte);
        String pub = CryptoUtils.encryptBASE64(pair.getPublic().getEncoded());

//        PrivateKey priKey = getPrivateKeyByStr(pair.getPrivate().ge);
//        PublicKey pubKey = getPublicKeyByStr(pub);

        String signature = sign("foobar", getPrivateKey(pri));
        System.out.println(signature);

        //Let's check the signature
        boolean isCorrect = verify("foobar", signature, getPublicKey(pub));
        System.out.println("Signature correct: " + isCorrect);

        System.out.println(CryptoUtils.encryptBASE64(pair.getPrivate().getEncoded()));
        System.out.println();
        System.out.println(CryptoUtils.encryptBASE64(pair.getPublic().getEncoded()));



    }
}

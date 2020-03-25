package com.zz.util;

import one.inve.utils.CryptoUtil;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
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
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    public static String sign(String plainText, PrivateKey privateKey) throws NoSuchAlgorithmException, UnsupportedEncodingException, SignatureException, InvalidKeyException {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes("UTF-8"));

        byte[] signature = privateSignature.sign();

        return CryptoUtils.encryptBASE64(signature);
    }



    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes("UTF-8"));

        byte[] signatureBytes = CryptoUtils.decryptBASE64(signature);

        return publicSignature.verify(signatureBytes);
    }


    public static void main(String[] args) throws Exception {


        KeyPair pair = generateKeyPair();

        String signature = sign("foobar", pair.getPrivate());

        byte[] publicBytes = pair.getPublic().getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);


//Let's check the signature
        boolean isCorrect = verify("foobar", signature, pubKey);
        System.out.println("Signature correct: " + isCorrect);
    }
}

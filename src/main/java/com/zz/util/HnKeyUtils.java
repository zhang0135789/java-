package com.zz.util;

import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class HnKeyUtils {
    public static final class HnKey {
        public String privKey;
        public String pubkey;

        public HnKey() {
        }

        private HnKey(Builder builder) {
            privKey = builder.privKey;
            pubkey = builder.pubkey;
        }


        public static final class Builder {
            private String privKey;
            private String pubkey;

            public Builder() {
            }

            public Builder privKey(String val) {
                privKey = val;
                return this;
            }

            public Builder pubkey(String val) {
                pubkey = val;
                return this;
            }

            public HnKey build() {
                return new HnKey(this);
            }
        }
    }

    public static String getString4PublicKey(PublicKey publicKey) {
        return (new BASE64Encoder()).encode(publicKey.getEncoded());
    }

    public static String getString4PrivateKey(PrivateKey privateKey) {
        return (new BASE64Encoder()).encode(privateKey.getEncoded());
    }

    public static PublicKey getPublicKey4String(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey getPrivateKey4String(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static void writeKey2File(KeyPair keyPair, String filePath) {
        try {
            HnKey hnKey = new HnKey.Builder()
                    .privKey(getString4PrivateKey(keyPair.getPrivate()))
                    .pubkey(getString4PublicKey(keyPair.getPublic()))
                    .build();
            FileOutputStream outputStream = new FileOutputStream(new File(filePath));
            outputStream.write(JSONObject.toJSONString(hnKey).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static KeyPair readKeyFromFile(String filePath) {
        KeyPair keyPair = null;
        try {
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();

            JSONObject object = JSONObject.parseObject(new String(data));
            keyPair = new KeyPair(getPublicKey4String(object.getString("pubkey")), getPrivateKey4String(object.getString("privKey")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyPair;
    }

    public static String getPri2File(String filePath) {
        try {
            FileInputStream in = new FileInputStream(new File(filePath));
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

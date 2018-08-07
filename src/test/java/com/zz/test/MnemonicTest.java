package com.zz.test;

import one.inve.mnemonic.Mnemonic;
import one.inve.ntrusign.sha3.sign.NtruSign;
import one.inve.ntrusign.sha3.sign.SignatureKeyPair;
import one.inve.ntrusign.sha3.sign.SignatureParameters;
import one.inve.ntrusign.sha3.sign.SignaturePrivateKey;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

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



    @Test
    public void signTest() throws NoSuchProviderException, NoSuchAlgorithmException {
        System.out.println("NTRU signature");

        //配置ntru参数
        SignatureParameters NtruSign_keccak_251 = new SignatureParameters(251, 256, 29, 1, SignatureParameters.BasisType.TRANSPOSE, 0.165f, 200, 80, false, true, SignatureParameters.KeyGenAlg.RESULTANT, "SHA3_512");
        // create an instance of NtruSign with a test parameter set
        //
        NtruSign ntru = new NtruSign(NtruSign_keccak_251);
        // NtruSign ntru = new NtruSign(SignatureParameters.TEST157);
        // create an signature key pair
        SignatureKeyPair kp = ntru.generateKeyPair();

        SignaturePrivateKey priKey = kp.getPrivate();
        System.out.println("===" + priKey);
        byte[] priByte = priKey.getEncoded();
        System.out.println("===" + Arrays.toString(priByte));
        SignaturePrivateKey pubKey = kp.getPrivate();
        System.out.println("===" + pubKey);
        byte[] pubByte = pubKey.getEncoded();
        System.out.println("===" + Arrays.toString(pubByte));


        String msg = "The quick brown fox1";
        String msg1 = "The quick brown fox1,,";
        System.out.println("Message: " + msg);

        //TODO 1签名
        //加载私钥  --SignatureKeyPair
        ntru.initSign(kp);
        // sign the message with the private key created above
//         ntru.update(msg.getBytes());
        byte[] sig = ntru.sign(msg.getBytes());



        //TODO 2 进行签名
//         byte[] sig = ntru.sign(msg.getBytes() ,kp);
        System.out.println(sig.length);
        String helloHex = DatatypeConverter.printHexBinary(sig);
        System.out.printf("Hello hex: 0x%s\n", helloHex);
        // verify the signature with the public key created above
        boolean valid = ntru.verify(msg.getBytes(), sig, kp.getPublic());

        System.out.println("  Signature valid? " + valid);
    }
}

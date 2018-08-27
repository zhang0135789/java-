package com.zz.test;

import com.zz.utils.QRcodeUtil;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 5:37 2018/8/9 0009
 * @Modified By
 */
public class QRcodeTest {

    @Test
    public void Test1() throws IOException {
        String filePath = "default/666230623579749460.jpg";
        File qrFile = new File(filePath);

        BufferedImage image = QRcodeUtil.generateQRcode("李海平");
        ImageIO.write(image,"PNG",qrFile);

    }


    public static void main(String[] args) throws IOException {
        String filePath = "default/666230623579749460.jpg";
//        String filePath2 = "签名.png";
        File qrFile = new File(filePath);

//        BufferedImage image = QRcodeUtil.generateQRcode("李海平");
//        ImageIO.write(image,"PNG",qrFile);


        String s = QRcodeUtil.readQRcode(new FileInputStream(new File(filePath)));
        System.out.println(s);
//        String s2 = QRcodeUtil.readQRcode(new FileInputStream(new File(filePath2)));
//        System.out.println(s2);

    }
}

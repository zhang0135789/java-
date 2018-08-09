package com.zz.utils;


import com.swetake.util.Qrcode;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 4:41 2018/8/9 0009
 * @Modified By
 */
public class QRcodeUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * read QRcode
     * @param is
     */
    public static String readQRcode(InputStream is) {
        String result = null;
        QRCodeDecoder decoder =null;
        BufferedImage image = null;

        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            log.error("read QR code file failed~!" ,e);
        }

        try {
            decoder = new QRCodeDecoder();
            result = new String(decoder.decode(new J2SEImage(image)));
        } catch (DecodingFailedException dfe) {
            log.error("analysis QR code failed~!" ,dfe);
        }
        return result;
    }




    /**
     * generate QR code
     * @param msg
     * @return
     */
    public static BufferedImage generateQRcode(String msg) {
        Qrcode qrcode = new Qrcode();
        qrcode.setQrcodeErrorCorrect('M');  // 纠错级别（L 7%、M 15%、Q 25%、H 30%）和版本有关
        qrcode.setQrcodeEncodeMode('B');
        qrcode.setQrcodeVersion(7);     // 设置Qrcode包的版本

        byte[] d = new byte[0]; // 字符集
//        try {
            d = msg.getBytes();
//        } catch (UnsupportedEncodingException e) {
//            log.error("get msg bytes failed~!",e);
//        }
        BufferedImage image = new BufferedImage(139, 139, BufferedImage.TYPE_INT_RGB);
        // createGraphics   // 创建图层
        Graphics2D g = image.createGraphics();

        g.setBackground(Color.WHITE);   // 设置背景颜色（白色）
        g.clearRect(0, 0, 139, 139);    // 矩形 X、Y、width、height
        g.setColor(Color.BLACK);    // 设置图像颜色（黑色）

        if (d.length > 0 && d.length < 123) {
            boolean[][] b = qrcode.calQrcode(d);
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b.length; j++) {
                    if (b[j][i]) {
                        g.fillRect(j * 3 + 2, i * 3 + 2, 3, 3);
                    }
                }
            }
        }

        g.dispose(); // 释放此图形的上下文以及它使用的所有系统资源。调用 dispose 之后，就不能再使用 Graphics 对象
        image.flush(); // 刷新此 Image 对象正在使用的所有可重构的资源

        return image;
    }


    private static class J2SEImage implements QRCodeImage {
        BufferedImage image;

        public J2SEImage(BufferedImage image) {
            this.image = image;
        }

        public int getWidth() {
            return image.getWidth();
        }

        public int getHeight() {
            return image.getHeight();
        }

        public int getPixel(int x, int y) {
            return image.getRGB(x, y);
        }
    }
}

package com.zz.utils;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 2:05 2018/8/7 0007
 * @Modified By
 */
public class MailUtil {

    public static final String SMTPSERVER = "smtp.exmail.qq.com";
    public static final String SMTPPORT = "465";
    public static final String ACCOUT = "email@inve.one";
    public static final String PWD = "Ppeng123";


    public static void main(String[] args) {

        MailUtil test=new MailUtil();
        try {
            test.testSendEmail("","");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void testSendEmail(String email ,String text) throws Exception {

        // 创建邮件配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", SMTPSERVER); // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.port", SMTPPORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
        props.setProperty("mail.smtp.ssl.enable", "true");// 开启ssl


        // 根据邮件配置创建会话，注意session别导错包
        Session session = Session.getDefaultInstance(props);
        // 开启debug模式，可以看到更多详细的输入日志
        session.setDebug(true);
        //创建邮件
        MimeMessage message = createEmail(session ,email ,text);
        //获取传输通道
        Transport transport = session.getTransport();
        transport.connect(SMTPSERVER,ACCOUT, PWD);
        //连接，并发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }


    public static MimeMessage  createEmail(Session session ,String email ,String text) throws Exception {
        // 根据会话创建邮件
        MimeMessage msg = new MimeMessage(session);
        // address邮件地址, personal邮件昵称, charset编码方式
        InternetAddress fromAddress = new InternetAddress(ACCOUT,
                "InterValue", "utf-8");
        // 设置发送邮件方
        msg.setFrom(fromAddress);
        InternetAddress receiveAddress = new InternetAddress(
                email, "test", "utf-8");
        // 设置邮件接收方
        msg.setRecipient(Message.RecipientType.TO, receiveAddress);
        // 设置邮件标题
        msg.setSubject("InterValue Team", "utf-8");
        msg.setText(text);
        // 设置显示的发件时间
        msg.setSentDate(new Date());
        // 保存设置
        msg.saveChanges();
        return msg;
    }

}

package com.zz.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;



/**
 * @Author: zz
 * @Description:  删除邮件
 * @Date: 下午 3:39 2018/5/28 0028
 * @Modified By
 */
public class MailDeleteUtil extends Thread {
    /**
     * 有一封邮件就需要建立一个ReciveMail对象
     */
    /**
     * 判断删除的数量
     */
    public static int deleteNUm=0;
    private MimeMessage mimeMessage = null;
    private String saveAttachPath = null; //附件下载后的存放目录
    private StringBuffer bodytext = new StringBuffer();//存放邮件内容
    private String dateformat = "yy-MM-dd HH:mm"; //默认的日前显示格式
    private String pop3 = null;
    private String smtps =null;
    private String mailusername = null;
    private String mailuserpassword = null;
    private String subject = null;
    /**
     * 线程调用前配置各个变量
     */
    public void setPop3(String pop){
        this.pop3=pop;
    }
    public void setSmtps(String smtp){
        this.smtps=smtp;
    }
    public void setMailusername(String name){
        this.mailusername=name;
    }
    public void setMailuserpassword(String password){
        this.mailuserpassword=password;
    }
    public void setSubject(String sub){
        this.subject=sub;
    }
    public void run(){
        //System.out.println("SENDER-" + this.getName() + ":/>" + "邮件正在放送中，请耐心等待");
        try {
            delete();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println("DELETE-" + this.getName() + ":/>" + "指定邮件已删除！");
    }

    public MailDeleteUtil() {
    }
    public MailDeleteUtil(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    /**
     * 获得发件人的地址和姓名
     */
    public String getFrom() throws Exception {
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
        String from = address[0].getAddress();
        if (from == null)
            from = "";
        String personal = address[0].getPersonal();
        if (personal == null)
            personal = "";
        String fromaddr = personal + "<" + from + ">";
        return fromaddr;
    }

    /**
     * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
     */
    public String getMailAddress(String type) throws Exception {
        String mailaddr = "";
        String addtype = type.toUpperCase();
        InternetAddress[] address = null;
        if (addtype.equals("TO") || addtype.equals("CC")|| addtype.equals("BCC")) {
            if (addtype.equals("TO")) {
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);
            } else if (addtype.equals("CC")) {
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);
            } else {
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);
            }
            if (address != null) {
                for (int i = 0; i < address.length; i++) {
                    String email = address[i].getAddress();
                    if (email == null)
                        email = "";
                    else {
                        email = MimeUtility.decodeText(email);
                    }
                    String personal = address[i].getPersonal();
                    if (personal == null)
                        personal = "";
                    else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    String compositeto = personal + "<" + email + ">";
                    mailaddr += "," + compositeto;
                }
                if(mailaddr.length()>0)
                    mailaddr = mailaddr.substring(1);
            }
        } else {
            throw new Exception("Error emailaddr type!");
        }
        return mailaddr;
    }

    /**
     * 获得邮件主题
     */
    public String getSubject() throws MessagingException {
        String subject = "";
        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
            if (subject == null)
                subject = "";
        } catch (Exception exce) {}
        return subject;
    }

    /**
     * 获得邮件发送日期
     */
    public String getSentDate() throws Exception {
        Date sentdate = mimeMessage.getSentDate();
        SimpleDateFormat format = new SimpleDateFormat(dateformat);
        return format.format(sentdate);
    }

    /**
     * 获得邮件正文内容
     */
    public String getBodyText() {
        return bodytext.toString();
    }

    /**
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
     */
    public void getMailContent(Part part) throws Exception {
        String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");
        boolean conname = false;
        if (nameindex != -1)
            conname = true;
        //System.out.println("NetOperation=== CONTENTTYPE: " + contenttype);
        if (part.isMimeType("text/plain") && !conname) {
            bodytext.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && !conname) {
            bodytext.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getMailContent(multipart.getBodyPart(i));
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part) part.getContent());
        } else {}
    }

    /**
     * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
     */
    public boolean getReplySign() throws MessagingException {
        boolean replysign = false;
        String needreply[] = mimeMessage
                .getHeader("Disposition-Notification-To");
        if (needreply != null) {
            replysign = true;
        }
        return replysign;
    }

    /**
     * 获得此邮件的Message-ID
     */
    public String getMessageId() throws MessagingException {
        return mimeMessage.getMessageID();
    }

    /**
     * 【判断此邮件是否已读，如果未读返回返回false,反之返回true】
     */
    public boolean isNew() throws MessagingException {
        boolean isnew = false;
        Flags flags = ((Message) mimeMessage).getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        //System.out.println("NetOperation=== flags's length: " + flag.length);
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;
                //System.out.println("NetOperation=== seen Message.......");
                break;
            }
        }
        return isnew;
    }

    /**
     * 判断此邮件是否包含附件
     */
    public boolean isContainAttach(Part part) throws Exception {
        boolean attachflag = false;
        // String contentType = part.getContentType();
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                        .equals(Part.INLINE))))
                    attachflag = true;
                else if (mpart.isMimeType("multipart/*")) {
                    attachflag = isContainAttach((Part) mpart);
                } else {
                    String contype = mpart.getContentType();
                    if (contype.toLowerCase().indexOf("application") != -1)
                        attachflag = true;
                    if (contype.toLowerCase().indexOf("name") != -1)
                        attachflag = true;
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            attachflag = isContainAttach((Part) part.getContent());
        }
        return attachflag;
    }

    /**
     * 【保存附件】
     */

    public String saveAttachMent(Part part) throws Exception {
        String fileNames = "";
        String fileName = "";
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
                    fileName = mpart.getFileName();
                    //System.out.println("fileName_1: "+fileName);
                    //编码转换GBb2312或者GBK的情况
                    if ( fileName.indexOf("=?application/msword") >=0 ){
                        fileName = fileName.replaceAll("application/msword","gbk" ); // 将编码方式的信息由UNKNOWN改为GBK
                        fileName = MimeUtility.decodeText( fileName ); //再重新解码
                        //System.out.println("fileName_2: "+fileName);
                    }


                    if (fileName.toLowerCase().indexOf("gb") != -1) {
                        fileName = MimeUtility.decodeText(fileName);
                        //System.out.println("fileName_3 :"+fileName);
                    }

                    saveFile(fileName, mpart.getInputStream());
                } else if (mpart.isMimeType("multipart/*")) {
                    saveAttachMent(mpart);
                } else {
                    fileName = mpart.getFileName();
                    if ((fileName != null)
                            && (fileName.toLowerCase().indexOf("gb") != -1)) {
                        fileName = MimeUtility.decodeText(fileName);
                        //System.out.println("filename_4 :"+fileName);
                        saveFile(fileName, mpart.getInputStream());
                    }
                }
                fileNames += fileName + ",";
                //System.out.println("filenames_1 :"+fileNames);
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachMent((Part) part.getContent());
        }
        if(fileNames !=null && !"".equals(fileNames)) {
            fileNames = fileNames.substring(0,fileNames.length()-1);
            //System.out.println("filenames_2 :"+fileNames);
        }
        return fileNames;
    }
    /**
     * 【设置附件存放路径】
     */

    public void setAttachPath(String attachpath) {
        this.saveAttachPath = attachpath;
    }

    /**
     * 【设置日期显示格式】
     */
    public void setDateFormat(String format) throws Exception {
        this.dateformat = format;
    }

    /**
     * 【获得附件存放路径】
     */
    public String getAttachPath() {
        return saveAttachPath;
    }

    /**
     * 【真正的保存附件到指定目录里】
     */

    private void saveFile(String fileName, InputStream in) throws Exception {
        String osName = System.getProperty("os.name");
        String storedir = getAttachPath();
        //System.out.println("Doenload in :"+storedir);
        String separator = "";
        if (osName == null)
            osName = "";
        if (osName.toLowerCase().indexOf("win") != -1) {
            separator = "\\";
            if (storedir == null || storedir.equals(""))
                storedir = "data";
        } else {
            separator = "\\";
            storedir = "data";
        }
        File storefile = new File(storedir + separator + fileName);
        if(!storefile.exists()){

            storefile.getParentFile().mkdirs();
            storefile.createNewFile();

        }

        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(storefile));
            bis = new BufferedInputStream(in);
            int c;
            while ((c = bis.read()) != -1) {
                bos.write(c);
                bos.flush();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new Exception("文件保存失败!");
        } finally {
            bos.close();
            bis.close();
        }
    }
    //接受特定的邮件信息的附件 该信息从tempObj里读取
    @SuppressWarnings("null")
    private void delete() throws Exception {
        // TODO Auto-generated method stub
        //设置必要的头信息
        MailDeleteUtil pmm =null;
        Properties props = System.getProperties();
        //创建一个属性对象
        //props.setProperty("mail.transport.protocol","smtp");    //设置使用smtp协议
        //props.setProperty("mail.smtp.host",this.smtps);  //设置SMTP服务器地址
        //props.setProperty("mail.smtp.port",""+2525); //设置SMTP端口号
        //props.setProperty("mail.smtp.auth","true");   //SMTP服务用户认证
        //创建一个过程对象
        props.put("mail.smtp.host", this.smtps);   //此处因根据不同的邮箱设置不同的服务器配置
        props.put("mail.smtp.port",""+25); //设置SMTP端口号
        //props.put("mail.smtp.port",""+2525); //设置SMTP端口号 本地测试用
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, null);
        URLName urln = new URLName("pop3", this.pop3, 110, null,this.mailusername,this.mailuserpassword);
        //URLName urln = new URLName("pop3", this.pop3, 1101, null, this.mailusername, this.mailuserpassword);//本地测试pop3端口110110
        Store store = session.getStore(urln);
        store.connect();
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        Message message[] = folder.getMessages();
        //System.out.println("NetOperation=== Messages's length: " + message.length);
        //开始有根据有选择的删除邮件

        for (int i = 0; i < message.length; i++) {
            //System.out.println("======================");
            pmm = new MailDeleteUtil((MimeMessage) message[i]);
            //获取邮件主题
            //System.out.println("NetOperation=== Message " + i + " subject: " + pmm.getSubject());
            // 获得邮件内容
            pmm.getMailContent((Part) message[i]);
            //System.out.println("NetOperation=== Message " + i + " bodycontent: \r\n" + pmm.getBodyText());
            //删除指定的邮件
            //if(true){
            if(this.subject.equals(pmm.getSubject())){
                //message[i].setFlag(Flags.Flag.DRAFT,true);//删除至垃圾箱
                message[i].setFlag(Flags.Flag.DELETED,true);
                this.deleteNUm++;
            }
        }
        folder.close(true);
        //System.out.println("DELETED Successed !");
    }
    /**
     * @throws Exception
     * PraseMimeMessage类测试
     * @throws
     */
    @SuppressWarnings("null")
    public static void main(String args[]) throws Exception {
        MailDeleteUtil pmm =new MailDeleteUtil();
        pmm.setMailusername("filetest4@sina.com");
        pmm.setMailuserpassword("123456");
        pmm.setSmtps("smtp.sina.com.cn");
        pmm.setPop3("pop3.sina.com.cn");
        pmm.setSubject("This is a local mail test");
        pmm.setAttachPath("E:\\test\\testdown");
        pmm.start();
        pmm.join();
        //System.out.println("DELETE-" + "Main Thread" + ":/>" + "指定邮件已删除！");
    }

}





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
 * @Description:  ɾ���ʼ�
 * @Date: ���� 3:39 2018/5/28 0028
 * @Modified By
 */
public class MailDeleteUtil extends Thread {
    /**
     * ��һ���ʼ�����Ҫ����һ��ReciveMail����
     */
    /**
     * �ж�ɾ��������
     */
    public static int deleteNUm=0;
    private MimeMessage mimeMessage = null;
    private String saveAttachPath = null; //�������غ�Ĵ��Ŀ¼
    private StringBuffer bodytext = new StringBuffer();//����ʼ�����
    private String dateformat = "yy-MM-dd HH:mm"; //Ĭ�ϵ���ǰ��ʾ��ʽ
    private String pop3 = null;
    private String smtps =null;
    private String mailusername = null;
    private String mailuserpassword = null;
    private String subject = null;
    /**
     * �̵߳���ǰ���ø�������
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
        //System.out.println("SENDER-" + this.getName() + ":/>" + "�ʼ����ڷ����У������ĵȴ�");
        try {
            delete();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println("DELETE-" + this.getName() + ":/>" + "ָ���ʼ���ɾ����");
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
     * ��÷����˵ĵ�ַ������
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
     * ����ʼ����ռ��ˣ����ͣ������͵ĵ�ַ�����������������ݵĲ����Ĳ�ͬ "to"----�ռ��� "cc"---�����˵�ַ "bcc"---�����˵�ַ
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
     * ����ʼ�����
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
     * ����ʼ���������
     */
    public String getSentDate() throws Exception {
        Date sentdate = mimeMessage.getSentDate();
        SimpleDateFormat format = new SimpleDateFormat(dateformat);
        return format.format(sentdate);
    }

    /**
     * ����ʼ���������
     */
    public String getBodyText() {
        return bodytext.toString();
    }

    /**
     * �����ʼ����ѵõ����ʼ����ݱ��浽һ��StringBuffer�����У������ʼ� ��Ҫ�Ǹ���MimeType���͵Ĳ�ִͬ�в�ͬ�Ĳ�����һ��һ���Ľ���
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
     * �жϴ��ʼ��Ƿ���Ҫ��ִ�������Ҫ��ִ����"true",���򷵻�"false"
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
     * ��ô��ʼ���Message-ID
     */
    public String getMessageId() throws MessagingException {
        return mimeMessage.getMessageID();
    }

    /**
     * ���жϴ��ʼ��Ƿ��Ѷ������δ�����ط���false,��֮����true��
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
     * �жϴ��ʼ��Ƿ��������
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
     * �����渽����
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
                    //����ת��GBb2312����GBK�����
                    if ( fileName.indexOf("=?application/msword") >=0 ){
                        fileName = fileName.replaceAll("application/msword","gbk" ); // �����뷽ʽ����Ϣ��UNKNOWN��ΪGBK
                        fileName = MimeUtility.decodeText( fileName ); //�����½���
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
     * �����ø������·����
     */

    public void setAttachPath(String attachpath) {
        this.saveAttachPath = attachpath;
    }

    /**
     * ������������ʾ��ʽ��
     */
    public void setDateFormat(String format) throws Exception {
        this.dateformat = format;
    }

    /**
     * ����ø������·����
     */
    public String getAttachPath() {
        return saveAttachPath;
    }

    /**
     * �������ı��渽����ָ��Ŀ¼�
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
            throw new Exception("�ļ�����ʧ��!");
        } finally {
            bos.close();
            bis.close();
        }
    }
    //�����ض����ʼ���Ϣ�ĸ��� ����Ϣ��tempObj���ȡ
    @SuppressWarnings("null")
    private void delete() throws Exception {
        // TODO Auto-generated method stub
        //���ñ�Ҫ��ͷ��Ϣ
        MailDeleteUtil pmm =null;
        Properties props = System.getProperties();
        //����һ�����Զ���
        //props.setProperty("mail.transport.protocol","smtp");    //����ʹ��smtpЭ��
        //props.setProperty("mail.smtp.host",this.smtps);  //����SMTP��������ַ
        //props.setProperty("mail.smtp.port",""+2525); //����SMTP�˿ں�
        //props.setProperty("mail.smtp.auth","true");   //SMTP�����û���֤
        //����һ�����̶���
        props.put("mail.smtp.host", this.smtps);   //�˴�����ݲ�ͬ���������ò�ͬ�ķ���������
        props.put("mail.smtp.port",""+25); //����SMTP�˿ں�
        //props.put("mail.smtp.port",""+2525); //����SMTP�˿ں� ���ز�����
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, null);
        URLName urln = new URLName("pop3", this.pop3, 110, null,this.mailusername,this.mailuserpassword);
        //URLName urln = new URLName("pop3", this.pop3, 1101, null, this.mailusername, this.mailuserpassword);//���ز���pop3�˿�110110
        Store store = session.getStore(urln);
        store.connect();
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        Message message[] = folder.getMessages();
        //System.out.println("NetOperation=== Messages's length: " + message.length);
        //��ʼ�и�����ѡ���ɾ���ʼ�

        for (int i = 0; i < message.length; i++) {
            //System.out.println("======================");
            pmm = new MailDeleteUtil((MimeMessage) message[i]);
            //��ȡ�ʼ�����
            //System.out.println("NetOperation=== Message " + i + " subject: " + pmm.getSubject());
            // ����ʼ�����
            pmm.getMailContent((Part) message[i]);
            //System.out.println("NetOperation=== Message " + i + " bodycontent: \r\n" + pmm.getBodyText());
            //ɾ��ָ�����ʼ�
            //if(true){
            if(this.subject.equals(pmm.getSubject())){
                //message[i].setFlag(Flags.Flag.DRAFT,true);//ɾ����������
                message[i].setFlag(Flags.Flag.DELETED,true);
                this.deleteNUm++;
            }
        }
        folder.close(true);
        //System.out.println("DELETED Successed !");
    }
    /**
     * @throws Exception
     * PraseMimeMessage�����
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
        //System.out.println("DELETE-" + "Main Thread" + ":/>" + "ָ���ʼ���ɾ����");
    }

}





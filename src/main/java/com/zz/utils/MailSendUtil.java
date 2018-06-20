package com.zz.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import sun.misc.BASE64Encoder;


/**
 * @Author: zz
 * @Description:   ����ʹ��Socket���ӵ��ʼ�������
 *                 ��ʵ������ָ�����䷢���ʼ��������Ĺ��ܡ�
 * @Date: ���� 3:39 2018/5/28 0028
 * @Modified By
 */
public class MailSendUtil extends Thread{
    public void run(){
        //System.out.println("SENDER-" + this.getName() + ":/>" + "�ʼ����ڷ����У������ĵȴ�");
        send();
        //System.out.println("SENDER-" + this.getName() + ":/>" + "�ʼ��ѷ�����ϣ�");
    }
    /**
     * �ж��ϴ�������
     */
    public static int uploadNum=0;
    /**
     * ���з�
     */
    private static final String LINE_END = "\r\n";

    /**
     * ֵΪ��true������߶���Ϣ��������������Ӧ��Ϣ����ֵΪ��
     * false�������������Ϣ��
     */
    private boolean isDebug = true;

    /**
     * ֵΪ��true�����ڷ����ʼ�{@link MailSendUtil#send()}
     * �����л��ȡ�������˷��ص���Ϣ��
     * �����ʼ�������Ϻ���Щ��Ϣ���ظ��û���
     */
    private boolean isAllowReadSocketInfo = true;

    /**
     * �ʼ���������ַ
     */
    private String host;

    /**
     * �����������ַ
     */
    private String from;

    /**
     * �ռ��������ַ
     */
    private List<String> to;

    /**
     * ���͵�ַ
     */
    private List<String> cc;

    /**
     * ���͵�ַ
     */
    private List<String> bcc;

    /**
     * �ʼ�����
     */
    private String subject;

    /**
     * �û���
     */
    private String user;

    /**
     * ����
     */
    private String password;

    /**
     * MIME�ʼ�����
     */
    private String contentType;

    /**
     * �����󶨶���ʼ���Ԫ{@link #partSet}
     * �ķָ���ʶ�����ǿ��Խ��ʼ������ļ�ÿһ��������������һ���ʼ���Ԫ
     * ��
     */
    private String boundary;

    /**
     * �ʼ���Ԫ�ָ���ʶ���������Խ��������ʼ�����Ϊ�ָ�����ʼ���Ԫ�ı�ʶ
     * ��
     */
    private String boundaryNextPart;

    /**
     * �����ʼ������õı���
     */
    private String contentTransferEncoding;

    /**
     * �����ʼ��������õ��ַ���
     */
    private String charset;

    /**
     * ��������
     */
    private String contentDisposition;

    /**
     * �ʼ�����
     */
    private String content;

    /**
     * �����ʼ����ڵ���ʾ��ʽ
     */
    private String simpleDatePattern;

    /**
     * ������Ĭ��MIME����
     */
    private String defaultAttachmentContentType;

    /**
     * �ʼ���Ԫ�ļ��ϣ�����������ĵ�Ԫ�����еĸ�����Ԫ��
     */
    private List<MailPart> partSet;

    /**
     * ��ͬ�����ļ���Ӧ��{@link } ����ӳ�䡣����Ӹ���
     * {@link #addAttachment(String)}
     * ʱ������������ӳ���в��Ҷ�Ӧ�ļ��� {@link }
     * ���ͣ����û�У� ��ʹ��
     * {@link #defaultAttachmentContentType}
     * ����������͡�
     */
    private static Map<String, String> contentTypeMap;

    static {
        // MIME Media Types
        contentTypeMap = new HashMap<String, String>();
        contentTypeMap.put("xls", "application/vnd.ms-excel");
        contentTypeMap.put("xlsx", "application/vnd.ms-excel");
        contentTypeMap.put("xlsm", "application/vnd.ms-excel");
        contentTypeMap.put("xlsb", "application/vnd.ms-excel");
        contentTypeMap.put("doc", "application/msword");
        contentTypeMap.put("dot", "application/msword");
        contentTypeMap.put("docx", "application/msword");
        contentTypeMap.put("docm", "application/msword");
        contentTypeMap.put("dotm", "application/msword");
        contentTypeMap.put("txt", "application/msword");
    }

    /**
     * ��������ʵ����һ�����ĵ�Ԫ�򸽼���Ԫ�������̳���
     * {@link MailSendUtil}
     * ���������������������Ҫ��Ϊ�������ʼ���Ԫ������ʼ��������
     * ��ʹ�����׶�һЩ�� ��Щ�ʼ���Ԫȫ����ŵ�partSet
     * �У��ڷ����ʼ� {@link #send()}ʱ, ��������
     * {@link #getAllParts()}
     * ���������еĵ�Ԫ�ϲ���һ������MIME��ʽ���ַ�����
     *
     * @author Zhong Lizhi
     */
    private class MailPart extends MailSendUtil {
        public MailPart() {
        }
    }
    /**
     * Ĭ�Ϲ��캯��
     */
    public MailSendUtil() {
        defaultAttachmentContentType = "application/octet-stream";
        simpleDatePattern = "yyyy-MM-dd HH:mm:ss";
        boundary = "--=_NextPart_zlz_3907_" + System.currentTimeMillis();
        boundaryNextPart = "--" + boundary;
        contentTransferEncoding = "base64";
        contentType = "multipart/alternative";
        charset = Charset.defaultCharset().name();
        partSet = new ArrayList<MailPart>();
        to = new ArrayList<String>();
        cc = new ArrayList<String>();
        bcc = new ArrayList<String>();
    }

    /**
     * ����ָ���������ļ�����
     * {@link #contentTypeMap}
     * �в�������Ӧ��MIME���ͣ� ���û�ҵ����򷵻�
     * {@link #defaultAttachmentContentType}
     * ��ָ����Ĭ�����͡�
     *
     * @param fileName �ļ���
     * @return �����ļ���Ӧ��MIME���͡�
     */
    private String getPartContentType(String fileName) {
        String ret = null;
        if (null != fileName) {
            int flag = fileName.lastIndexOf(".");
            if (0 <= flag && flag < fileName.length() - 1) {
                fileName = fileName.substring(flag + 1);
            }
            ret = contentTypeMap.get(fileName);
        }

        if (null == ret) {
            ret = defaultAttachmentContentType;
        }
        return ret;
    }

    /**
     * �������ַ���ת��Ϊbase64������ַ���
     *
     * @param str
     *            ��Ҫת����ַ���
     * @param charset
     *            ԭ�ַ����ı����ʽ
     * @return base64�����ʽ���ַ�
     */
    private String toBase64(String str, String charset) {
        if (null != str) {
            try {
                return toBase64(str.getBytes(charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * ��ָ�����ֽ�����ת��Ϊbase64��ʽ���ַ���
     *
     * @param bs
     *            ��Ҫת����ֽ�����
     * @return base64�����ʽ���ַ�
     */
    private String toBase64(byte[] bs) {
        return new BASE64Encoder().encode(bs);
    }

    /**
     * �������ַ���ת��Ϊbase64������ַ���
     *
     * @param str
     *            ��Ҫת����ַ���
     * @return base64�����ʽ���ַ�
     */
    private String toBase64(String str) {
        return toBase64(str, Charset.defaultCharset().name());
    }

    /**
     * �����е��ʼ���Ԫ���ձ�׼��MIME��ʽҪ��ϲ���
     *
     * @return ����һ�����е�Ԫ�ϲ�����ַ�����
     */
    private String getAllParts() {
        int partCount = partSet.size();
        StringBuilder sbd = new StringBuilder(LINE_END);
        for (int i = partCount - 1; i >= 0; i--) {
            MailSendUtil attachment = partSet.get(i);
            String attachmentContent = attachment.getContent();
            if (null != attachmentContent && 0 < attachmentContent.length()) {
                sbd.append(getBoundaryNextPart()).append(LINE_END);
                sbd.append("Content-Type: ");
                sbd.append(attachment.getContentType());
                sbd.append(LINE_END);
                sbd.append("Content-Transfer-Encoding: ");
                sbd.append(attachment.getContentTransferEncoding());
                sbd.append(LINE_END);
                if (i != partCount - 1) {
                    sbd.append("Content-Disposition: ");
                    sbd.append(attachment.getContentDisposition());
                    sbd.append(LINE_END);
                }

                sbd.append(LINE_END);
                sbd.append(attachment.getContent());
                sbd.append(LINE_END);
            }
        }

        sbd.append(LINE_END);
        sbd.append(LINE_END);
        // sbd.append(boundaryNextPart).
        // append(LINE_END);
        partSet.clear();
        return sbd.toString();
    }

    /**
     * ����ʼ����ĵ�Ԫ
     */
    private void addContent() {
        if (null != content) {
            MailPart part = new MailPart();
            part.setContent(toBase64(content));
            part.setContentType("text/plain;charset=\"" + charset + "\"");
            partSet.add(part);
        }
    }

    private String listToMailString(List<String> mailAddressList) {
        StringBuilder sbd = new StringBuilder();
        if (null != mailAddressList) {
            int listSize = mailAddressList.size();
            for (int i = 0; i < listSize; i++) {
                if (0 != i) {
                    sbd.append(";");
                }
                sbd.append("<").append(mailAddressList.get(i)).append(">");
            }
        }
        return sbd.toString();
    }

    private List<String> getrecipient() {
        List<String> list = new ArrayList<String>();
        list.addAll(to);
        list.addAll(cc);
        list.addAll(bcc);
        return list;
    }

    /**
     * ���һ��������Ԫ
     *
     * @param filePath
     *            �ļ�·��
     */
    public void addAttachment(String filePath) {
        addAttachment(filePath, null);
    }
    /**
     * ���һ��������Ԫ
     *
     * @param fileName
     *            �ļ�����
     *@param attachmentStream
     *            �ļ���
     */
    public void addAttachmentStream(String fileName,InputStream attachmentStream) {
        addAttachment(fileName,attachmentStream, null);
    }


    public void addTo(String mailAddress) {
        this.to.add(mailAddress);
    }

    public void addCc(String mailAddress) {
        this.cc.add(mailAddress);
    }

    public void addBcc(String mailAddress) {
        this.bcc.add(mailAddress);
    }


    /**
     * ���һ��������Ԫ
     *
     * @param filePath
     *            �ļ�·��
     * @param charset
     *            �ļ������ʽ
     */
    public void addAttachment(String filePath, String charset) {
        if (null != filePath && filePath.length() > 0) {
            File file = new File(filePath);
            try {
                addAttachment(file.getName(), new FileInputStream(file),
                        charset);
            } catch (FileNotFoundException e) {
                //System.out.println("����" + e.getMessage());
                System.exit(1);
            }
        }
    }

    /**
     * ���һ��������Ԫ
     *
     * @param fileName
     *            �ļ���
     * @param attachmentStream
     *            �ļ���
     * @param charset
     *            �ļ������ʽ
     */
    public void addAttachment(String fileName, InputStream attachmentStream,
                              String charset) {
        try {

            byte[] bs = null;
            if (null != attachmentStream) {
                int buffSize = 1024;
                byte[] buff = new byte[buffSize];
                byte[] temp;
                bs = new byte[0];
                int readTotal = 0;
                while (-1 != (readTotal = attachmentStream.read(buff))) {
                    temp = new byte[bs.length];
                    System.arraycopy(bs, 0, temp, 0, bs.length);
                    bs = new byte[temp.length + readTotal];
                    System.arraycopy(temp, 0, bs, 0, temp.length);
                    System.arraycopy(buff, 0, bs, temp.length, readTotal);
                }
            }

            if (null != bs) {
                MailPart attachmentPart = new MailPart();
                charset = null != charset ? charset : Charset.defaultCharset()
                        .name();
                String contentType = getPartContentType(fileName)
                        + ";name=\"=?" + charset + "?B?" + toBase64(fileName)
                        + "?=\"";
                attachmentPart.setCharset(charset);
                attachmentPart.setContentType(contentType);
                attachmentPart.setContentDisposition("attachment;filename=\"=?"
                        + charset + "?B?" + toBase64(fileName) + "?=\"");
                attachmentPart.setContent(toBase64(bs));
                partSet.add(attachmentPart);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != attachmentStream) {
                try {
                    attachmentStream.close();
                    attachmentStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
        }
    }

    /**
     * �����ʼ�
     *
     * @return �ʼ����������ص���Ϣ
     */
    public String send() {

        // ��������
        // ���ʼ�������Ϻ�������������Socket��
        // PrintWriter,
        // BufferedReader����Ҫ�رա�
        Socket socket = null;
        PrintWriter pw = null;
        BufferedReader br = null;

        try {
            socket = new Socket(host, 25);
            //socket = new Socket(host, 2525); //���ز����ö˿�
            pw = new PrintWriter(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));

            StringBuilder infoBuilder = new StringBuilder(
                    "\nServer info: \n------------\n");

            // ���������������
            pw.write("HELO ".concat(host).concat(LINE_END)); // ���ӵ��ʼ�����
            this.join(1000);
            if (!readResponse(pw, br, infoBuilder, "220"))
                return infoBuilder.toString();

            pw.write("AUTH LOGIN".concat(LINE_END)); // ��¼
            this.join(1000);
            if (!readResponse(pw, br, infoBuilder, "250"))
                return infoBuilder.toString();

            pw.write(toBase64(user).concat(LINE_END)); // �����û���
            this.join(1000);
            if (!readResponse(pw, br, infoBuilder, "334"))
                return infoBuilder.toString();

            pw.write(toBase64(password).concat(LINE_END)); // ��������
            this.join(1000);
            if (!readResponse(pw, br, infoBuilder, "334"))
                return infoBuilder.toString();

            pw.write("MAIL FROM:<" + from + ">" + LINE_END); // �����������ַ
            this.join(1000);
            if (!readResponse(pw, br, infoBuilder, "235"))
                return infoBuilder.toString();

            List<String> recipientList = getrecipient();
            // �ռ������ַ
            for (int i = 0; i < recipientList.size(); i++) {
                pw.write("RCPT TO:<" + recipientList.get(i) + ">" + LINE_END);
                this.join(1000);
                if (!readResponse(pw, br, infoBuilder, "250"))
                    return infoBuilder.toString();
            }
            // //System.out.println(
            // getAllSendAddress());

            pw.write("DATA" + LINE_END); // ��ʼ�����ʼ�
            this.join(1000);
            if (!readResponse(pw, br, infoBuilder, "250"))
                return infoBuilder.toString();

            flush(pw);

            // �����ʼ�ͷ��Ϣ
            StringBuffer sbf = new StringBuffer("From: <" + from + ">"
                    + LINE_END); // ������
            sbf.append("To: " + listToMailString(to) + LINE_END);// �ռ���
            sbf.append("Cc: " + listToMailString(cc) + LINE_END);// �ռ���
            sbf.append("Bcc: " + listToMailString(bcc) + LINE_END);// �ռ���
            sbf.append("Subject: " + subject + LINE_END);// �ʼ�����
            SimpleDateFormat sdf = new SimpleDateFormat(simpleDatePattern);
            sbf.append("Date: ").append(sdf.format(new Date()));
            sbf.append(LINE_END); // ����ʱ��
            sbf.append("Content-Type: ");
            sbf.append(contentType);
            sbf.append(";");
            sbf.append("boundary=\"");
            sbf.append(boundary).append("\""); // �ʼ���������
            sbf.append(LINE_END);
            sbf.append("This is a multi-part message in MIME format.");
            sbf.append(LINE_END);

            // ����ʼ����ĵ�Ԫ
            addContent();

            // �ϲ����е�Ԫ�����ĺ͸�����
            sbf.append(getAllParts());

            // ����
            sbf.append(LINE_END).append(".").append(LINE_END);
            pw.write(sbf.toString());
            readResponse(pw, br, infoBuilder, "354");
            flush(pw);

            // QUIT�˳�
            pw.write("QUIT" + LINE_END);
            this.join(1000);
            if (!readResponse(pw, br, infoBuilder, "250"))
                return infoBuilder.toString();
            flush(pw);
            this.uploadNum++;
            return infoBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception:>" + e.getMessage();
        } finally {
            // �ͷ���Դ
            try {
                if (null != socket)
                    socket.close();
                if (null != pw)
                    pw.close();
                if (null != br)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // this.to.clear();
            // this.cc.clear();
            // this.bcc.clear();
            this.partSet.clear();

        }

    }

    /**
     * ��SMTP����͵��ʼ�������
     *
     * @param pw
     *            �ʼ�������������
     */
    private void flush(PrintWriter pw) {
        if (!isAllowReadSocketInfo) {
            pw.flush();
        }
    }

    /**
     * ��ȡ�ʼ�����������Ӧ��Ϣ
     *
     * @param pw
     *            �ʼ�������������
     * @param br
     *            �ʼ������������
     * @param infoBuilder
     *            ������ŷ�������Ӧ��Ϣ���ַ�������
     * @param msgCode
     * @return
     * @throws IOException
     */
    private boolean readResponse(PrintWriter pw, BufferedReader br,
                                 StringBuilder infoBuilder, String msgCode) throws IOException {
        if (isAllowReadSocketInfo) {
            pw.flush();
            String message = br.readLine();
            infoBuilder.append("SERVER:/>");
            infoBuilder.append(message).append(LINE_END);
            if (null == message || 0 > message.indexOf(msgCode)) {
                //System.out.println("ERROR: " + message);
                pw.write("QUIT".concat(LINE_END));
                pw.flush();
                return false;
            }
            if (isDebug) {
                //System.out.println("DEBUG:/>" + msgCode + "/" + message);
            }
        }
        return true;
    }

    public String getBoundaryNextPart() {
        return boundaryNextPart;
    }

    public void setBoundaryNextPart(String boundaryNextPart) {
        this.boundaryNextPart = boundaryNextPart;
    }

    public String getDefaultAttachmentContentType() {
        return defaultAttachmentContentType;
    }

    public void setDefaultAttachmentContentType(
            String defaultAttachmentContentType) {
        this.defaultAttachmentContentType = defaultAttachmentContentType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }

    public void setContentTransferEncoding(String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getSimpleDatePattern() {
        return simpleDatePattern;
    }

    public void setSimpleDatePattern(String simpleDatePattern) {
        this.simpleDatePattern = simpleDatePattern;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAllowReadSocketInfo() {
        return isAllowReadSocketInfo;
    }

    public void setAllowReadSocketInfo(boolean isAllowReadSocketInfo) {
        this.isAllowReadSocketInfo = isAllowReadSocketInfo;
    }
    //��ȡ�û���profile�е�������Ϣ���������� pop3 ������
    //��ȡ�û��ϴ����ļ�����ϢtempObj

    public static void main(String[] args) throws InterruptedException {
        MailSendUtil mail1 = new MailSendUtil();
        mail1.setName("mail1 Thread");
        mail1.setHost("smtp.mxhichina.com"); // �ʼ���������ַ
        mail1.setFrom("wenzhengzhang@chenhaninfo.com"); // ����������
        mail1.addTo("wenzhengzhang@chenhaninfo.com"); // �ռ�������
        mail1.setUser("wenzhengzhang@chenhaninfo.com"); // �û���
        mail1.setPassword("Chenhan123"); // ����
        //�˴��ص��������Ҫ����Ϣ
        mail1.setSubject("This is test 1"); // �ʼ�����
        mail1.setContent("Test 1"); // �ʼ�����
//	    mail1.addAttachment( "E:\\test\\1_test.txt","application/msword");// ��Ӹ���

//	    MailSend mail2 = new MailSend();
//    	mail2.setName("mail2");
//	    mail2.setHost("smtp.163.com"); // �ʼ���������ַ
//	    mail2.setFrom("myfiletest@163.com"); // ����������
//	    mail2.addTo("myfiletest@163.com"); // �ռ�������
//	    mail2.setUser("myfiletest@163.com"); // �û���
//	    mail2.setPassword("123456"); // ����
//	    //�˴��ص��������Ҫ����Ϣ
//	    mail2.setSubject("This is a Test Mail!!"); // �ʼ�����
//	    mail2.setContent("����һ�����ԣ��벻Ҫ�ظ���"); // �ʼ�����
//	    mail2.addAttachment( "E:\\test\\2_test.txt","application/msword");// ��Ӹ���
        System.out.println("sending....");
        mail1.start();

        //�����̵߳ȴ������߳�ִ�н���������
        mail1.join();


        //System.out.println("SENDER-" + "Main Thread" + ":/>" + "�����ʼ��ѷ�����ϣ�");

    }


//    public static void main(String[] args) throws InterruptedException {
//
//    	MailSend mail1 = new MailSend();
//    	mail1.setName("mail1 Thread");
//	    mail1.setHost("smtp.sina.com"); // �ʼ���������ַ
//	    mail1.setFrom("filetest4@sina.com"); // ����������
//	    mail1.addTo("filetest4@sina.com"); // �ռ�������
//	    mail1.setUser("filetest4@sina.com"); // ����
//	    mail1.setPassword("123456"); // ����
//	    mail1.setSubject("This is a local mail test"); // �ʼ�����
//	    mail1.setContent("This is a local mail test"); // �ʼ�����
//	    mail1.addAttachment( "E:\\test\\������Ϣ.txt","application/msword");// ��Ӹ���
//
//	    mail1.start();
//	    //�����̵߳ȴ������߳�ִ�н���������
//	    mail1.join();
//
//	    //System.out.println("SENDER-" + "Main Thread" + ":/>" + "�����ʼ��ѷ�����ϣ�");
//
//    }
//
}


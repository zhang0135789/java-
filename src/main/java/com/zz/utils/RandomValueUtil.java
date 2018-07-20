package com.zz.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


/****
 *
 * Project Name:recruit-helper-util
 * <p>��������ɹ�����,��Ҫ����<br>
 *  �����������Ա�Email���ֻ��ţ�סַ
 * @ClassName: RandomValueUtil
 * @date 2018��5��23��  ����2:11:12
 *
 * @author youqiang.xiong
 * @version 1.0
 * @since
 */
public class RandomValueUtil {

    public static String base = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static String firstName="��Ǯ��������������������������������������������������������������������������������������������������������������������������������������������֣��������������������������ʩ�ſײ��ϻ���κ�ս���л������ˮ��������˸��ɷ�����³Τ������ﻨ������Ԭ��ۺ��ʷ�Ʒ����Ѧ�׺����������ޱϺ�����������ʱ��Ƥ���뿵����Ԫ";
    private static String girl="���Ӣ���������Ⱦ���������֥��Ƽ�����ҷ���ʴ��������÷���������滷ѩ�ٰ���ϼ����ݺ�����𷲼Ѽ�������������Ҷ�������������ɺɯ������ٻ�������ӱ¶������������Ǻɵ���ü������ޱݼ���Է�ܰ�������԰��ӽ�������ع���ѱ�ˬ������ϣ����Ʈ�����������������������ܿ�ƺ������˿ɼ���Ӱ��֦˼�� ";
    public static String boy="ΰ�����㿡��ǿ��ƽ�����Ļ�������������־��������ɽ�ʲ���������Ԫȫ��ʤѧ��ŷ���������ɱ�˳���ӽ��β��ɿ��ǹ���ﰲ����ï�����м�ͱ벩���Ⱦ�����׳��˼Ⱥ���İ�����ܹ����ƺ���������ԣ���ܽ���������ǫ�����֮�ֺ��ʲ����������������ά�������������󳿳�ʿ�Խ��������׵���ʱ̩ʢ��衾��ڲ�����ŷ纽��";
    public static final String[] email_suffix="@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");

    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }

    /***
     *
     * Project Name: recruit-helper-util
     * <p>�������Email
     *
     * @author youqiang.xiong
     * @date 2018��5��23��  ����2:13:06
     * @version v1.0
     * @since
     * @param lMin
     *         ��С����
     * @param lMax
     *         ��󳤶�
     * @return
     */
    public static String getEmail(int lMin,int lMax) {
        int length=getNum(lMin,lMax);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = (int)(Math.random()*base.length());
            sb.append(base.charAt(number));
        }
        sb.append(email_suffix[(int)(Math.random()*email_suffix.length)]);
        return sb.toString();
    }

    private static String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    /***
     *
     * Project Name: recruit-helper-util
     * <p>��������ֻ�����
     *
     * @author youqiang.xiong
     * @date 2018��5��23��  ����2:14:17
     * @version v1.0
     * @since
     * @return
     */
    public static String getTelephone() {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String thrid=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+thrid;
    }

    /***
     *
     * Project Name: recruit-helper-util
     * <p>�������8λ�绰����
     *
     * @author youqiang.xiong
     * @date 2018��5��23��  ����2:15:31
     * @version v1.0
     * @since
     * @return
     */
    public static String getLandline() {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String thrid=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+thrid;
    }



    /**
     * ������������
     */
    public static String name_sex = "";

    /***
     *
     * Project Name: recruit-helper-util
     * <p>������������
     *
     * @author youqiang.xiong
     * @date 2018��5��23��  ����2:16:16
     * @version v1.0
     * @since
     * @return
     */
    public static String getChineseName() {
        int index = getNum(0, firstName.length() - 1);
        String first = firstName.substring(index, index + 1);
        int sex = getNum(0, 1);
        String str = boy;
        int length = boy.length();
        if (sex == 0) {
            str = girl;
            length = girl.length();
            name_sex = "Ů";
        } else {
            name_sex = "��";
        }
        index = getNum(0, length - 1);
        String second = str.substring(index, index + 1);
        int hasThird = getNum(0, 1);
        String third = "";
        if (hasThird == 1) {
            index = getNum(0, length - 1);
            third = str.substring(index, index + 1);
        }
        return first + second + third;
    }


    public static void main(String[] args) throws IOException {


//       String asd =  getStringRandom(24);
//        System.out.println(asd);
       File file = new File("name.txt");
        FileWriter writer = new FileWriter(file);
        StringBuilder str = new StringBuilder();
        for(int i= 0 ;i<3000 ;i++) {
            str.append(getChineseName()).append("\r\n");
        }
        writer.write(str.toString());
//
//
//
//        File file2 = new File("email.txt");
//        FileWriter writer2 = new FileWriter(file2);
//        StringBuffer str2 = new StringBuffer();
//        for(int i= 0 ;i<3000 ;i++) {
//            str2.append(getEmail(3,16)).append("\r\n");
//        }
//        writer2.write(str2.toString());
    }


        /** �����������
         * n �� ��Ҫ�ĳ���
         * @return
                 */
        public static String getItemID( int n )
        {
            String val = "";
            Random random = new Random();
            for ( int i = 0; i < n; i++ ) {
                String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
                if ( "char".equalsIgnoreCase( str ) )
                { // ������ĸ
                    int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
                    // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                    val += (char) ( nextInt + random.nextInt( 26 ) );
                }
                else if ( "num".equalsIgnoreCase( str ) )
                { // ��������
                    val += String.valueOf( random.nextInt( 10 ) );
                }
            }
            return val;
        }


    public static String getStringRandom(int length) {
                 String val = "";
                Random random = new Random();
               //����length����ʾ���ɼ�λ�����
                for(int i = 0; i < length; i++) {
                        String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
                        //�����ĸ��������
                        if( "char".equalsIgnoreCase(charOrNum) ) {
                                //����Ǵ�д��ĸ����Сд��ĸ
                                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                                val += (char)(random.nextInt(26) + temp);
                            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                                 val += String.valueOf(random.nextInt(10));
                             }
                }
                return val;
             }

}
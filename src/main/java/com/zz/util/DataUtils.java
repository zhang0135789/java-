package com.zz.util;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Clare lau (Clarelau61803@gmail.com) on 2018/6/29 0029.
 */
public class DataUtils {
    /**
     * 转化参数
     * @param formData
     * @return
     */
    public static Map<String,String> form2Map(String formData ) {
        Map<String,String> result = (Map<String, String>) new HashMap<String,String>();
        //没参数拉倒
        if(formData== null || formData.trim().length() == 0) {
            return result;
        }
        //有参数解析
        final String[] items = formData.split("&");
        Arrays.stream(items).forEach(item ->{
            final String[] keyAndVal = item.split("=");
            if( keyAndVal.length == 2) {
                try{
                    final String key = URLDecoder.decode( keyAndVal[0],"utf8");
                    final String val = URLDecoder.decode( keyAndVal[1],"utf8");
                    result.put(key,val);
                }catch (UnsupportedEncodingException e) {}
            }
        });
//        System.out.println(JSONObject.toJSONString(formData));
        return result;
    }



    /**
     * 响应方法
     * @param exchange
     * @param response
     * @throws Exception
     */
    public static void responsePrint(HttpExchange exchange, String response)throws Exception {
        //设置响应头
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html;charset=utf-8");
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
        writer.write(response);
        writer.close();
        os.close();
        System.out.println("response finished.");
    }
}

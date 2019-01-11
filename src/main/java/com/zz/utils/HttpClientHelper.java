package com.zz.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;


/**
 * HTTP POST和GET处理工具类
 */
public class HttpClientHelper {

    private static Logger log = LoggerFactory.getLogger(HttpClientHelper.class);

    private static final MediaType JSON = MediaType.parse("application/json");
    private static Proxy proxy = null;

    private OkHttpClient httpClient = null;


    public HttpClientHelper() {
        this.httpClient = getHttpClient();
    }

    public HttpClientHelper(Proxy proxy) {
        this.proxy = proxy;
        this.httpClient = getHttpClient();
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }


    private OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS);
        return proxy == null ? builder.proxy(proxy).build() : builder.build();
    }



    public JSONObject doPost(String url, Map<String,String> params) throws IOException {
        JSONObject s = post(url, params, new TypeReference<JSONObject>() {});
        return s;
    }







    // send a GET request.
    <T> T get(String url, Map<String, String> params, TypeReference<T> ref) throws IOException {
        if (params == null) {
            params = new HashMap<>();
        }
        return call("GET", url, null, params, ref);
    }


    // send a POST request.
    <T> T post(String url, Object object, TypeReference<T> ref) throws IOException {
        return call("POST", url, object, new HashMap<String, String>(), ref);
    }

    // call api by endpoint.
    <T> T call(String method, String url, Object object, Map<String, String> params, TypeReference<T> ref) throws IOException {
        Request.Builder builder = null;
        if ("POST".equals(method)) {
            String json = JsonUtil.toJson(object);
            RequestBody body = RequestBody.create(JSON, json);
            builder = new Request.Builder().url(url + "?" + parseParams(params)).post(body);
        } else {
            builder = new Request.Builder().url(url + "?" + parseParams(params)).get();
        }
        Request request = builder.build();
        Response response = httpClient.newCall(request).execute();
        String s = response.body().string();
        return JsonUtil.fromJson(s, ref);
    }



    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url    发送请求的URL
     * @param params 请求参数
     * @param header 请求头
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, HashMap<String, String> params, HashMap<String, String> header) {
        String result = "";
        BufferedReader in = null;
        try {
            /**组装参数**/
            String param = parseParams(params);
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            /**打开和URL之间的连接**/
            URLConnection connection = realUrl.openConnection();
            /**设置通用的请求属性**/
            if (header != null) {
                header.forEach((key, value) -> {
                    connection.setRequestProperty(key, value);
                });
            }
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            /**建立实际的连接**/
            connection.connect();
            /**定义 BufferedReader输入流来读取URL的响应**/
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error("发送GET请求出现异常~!", e);
        } finally {/**使用finally块来关闭输入流**/
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 将HashMap参数组装成字符串
     *
     * @param map
     * @return
     */
    private static String parseParams(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        if (map != null && map.size() != 0) {
            for (Entry<String, String> e : map.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }


}//end
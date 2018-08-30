package com.zz.test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @Author: zz
 * @Description:
 * @Date: 下午 8:31 2018/8/29 0029
 * @Modified By
 */
public class HttpTest {

    private static final Logger log = LoggerFactory.getLogger(HttpTest.class);



    @Test
    public void http_test() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://zhang123.vip") //请求接口。如果需要传参拼接到接口后面。
                .build(); //创建Request 对象
        Response response = null;
        response = client.newCall(request).execute(); //得到resoinse对象
        if (response.isSuccessful()) {
            log.info("response.code()==" + response.code());
            log.info("response.message()==" + response.message());
            log.info("res==" + response.body().string());
            //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
        }

    }

//    public static void main(String[] args) throws IOException {

//    }
}

package com.zz.socket.socketio;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

import java.net.URISyntaxException;

/**
 * @Author: zz
 * @Description:
 * @Date: 上午 10:03 2019/1/12 0012
 * @Modified By
 */
public class SocketIoClient {


    public static void main(String[] args) throws URISyntaxException {
        IO.Options options = new IO.Options();
        options.forceNew = true;

        final OkHttpClient client = new OkHttpClient();
        options.webSocketFactory = client;
        options.callFactory = client;

        final Socket socket = IO.socket("http://192.168.5.149:" + "4000", options);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("connect");
                socket.close();
            }
        });
        socket.io().on(io.socket.engineio.client.Socket.EVENT_CLOSE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("engine close");
                client.dispatcher().executorService().shutdown();
            }
        });
        socket.open();
    }
}

package com.vzard.doutu.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;


@Component
@Slf4j
@ServerEndpoint(value = "/websocket")
public class CustomWebSocket {

    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<CustomWebSocket> webSockets = new CopyOnWriteArraySet<>();

    private Session session;

    @OnOpen
    public void onOpen(Session session) {

        this.session = session;

        webSockets.add(this);

        addOnlineCount();

        log.info("新连接接入,当前在线人数为：" + getOnlineCount());

        try {
            while (true) {
                sendMessage(new Date(System.currentTimeMillis()).toString());
                Thread.sleep(1000 * 3);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);

        subOnlineCount();

        log.info("有连接关闭,当前在线人数为：" + getOnlineCount());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("有异常了...");
        error.printStackTrace();
    }


    /**
     * 群发信息
     *
     * @param message
     */
    private static void sendAll(String message) {
        Arrays.asList(webSockets.toArray()).forEach(it -> {
            CustomWebSocket customWebSocket = (CustomWebSocket) it;
            try {
                customWebSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 减少在线人数
     */
    private void subOnlineCount() {
        CustomWebSocket.onlineCount--;

    }

    /**
     * 添加在线人数
     */
    private void addOnlineCount() {
        CustomWebSocket.onlineCount++;
    }

    /**
     * 当前在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 发送信息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


}

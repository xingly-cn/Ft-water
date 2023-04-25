package com.asugar.ftwaterdelivery.controller.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/1 14:56
 */
@ServerEndpoint("/order")
@Component
@Slf4j
public class OrderWebSocket {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.sendMessage("连接成功");
        log.info("连接成功");
    }

    @OnClose
    public void onClose(Session session) {
        this.sendMessage("连接关闭");
        log.info("连接关闭");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        this.sendMessage(message);
        log.info("收到：" + message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息
     * @param message 消息-字符串
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
            log.info("发送：" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     * @param message 消息-字符数组
     */
    public void sendMessage(byte[] message) {
        try {
            this.session.getBasicRemote().sendBinary(ByteBuffer.wrap(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

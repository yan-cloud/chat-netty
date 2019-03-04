package com.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.chat.client.NettyClient;
import io.netty.channel.Channel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;

import java.net.InetSocketAddress;

public class ChatStageController {

    @FXML
    TextArea message;

    @FXML
    TextArea say;

    @FXML
    Button send;


    public void onKeyPress(){
        say.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                try {
                    sendMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        say.clear();
    }

    /**
     * 消息发送
     */
    public void send() throws InterruptedException {
        sendMessage();
    }

    private void sendMessage() throws InterruptedException {
        String sendMessage = say.getText().trim();
        Channel channel = NettyClient.channel;
        message.appendText("I say:" + sendMessage+"\r\n");
        message.setFont(new Font("Black",16));
        message.setScrollLeft(1);
        if (channel == null || !channel.isActive()) {
            NettyClient.start(new InetSocketAddress("127.0.0.1", 9527));
            Thread.sleep(10000);
        }
        JSONObject object = new JSONObject();
        object.put("message",sendMessage);
        object.put("to","");
        object.put("from","");
        object.put("type","1");
        String s = object.toJSONString();
        channel.writeAndFlush(s+"\r\n");
        say.clear();
    }


}

package com.chat.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.chat.client.client.NettyClient;
import de.felixroske.jfxsupport.FXMLController;
import io.netty.channel.Channel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 聊天界面
 */

@FXMLController
public class ChatStageController {


    @FXML
    TextArea message;

    @FXML
    TextArea say;

    @FXML
    Button send;

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void onKeyPress() {
        say.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String sendMessage = say.getText().trim();
                message.appendText("I say:" + sendMessage + "\r\n");
                message.setFont(new Font("Black", 16));
                message.setScrollLeft(1);
                sendMessage(sendMessage);
                say.clear();
            }
        });
    }

    /**
     * 消息发送
     */
    public void send() throws InterruptedException {
        String sendMessage = say.getText().trim();
        sendMessage(sendMessage);
        message.appendText("I say:" + sendMessage + "\r\n");
        message.setFont(new Font("Black", 16));
        message.setScrollLeft(1);
        say.clear();
    }

    @Async
    public void sendMessage(String sendMessage) {
        executorService.submit(() -> {
            Channel channel = NettyClient.channel;
            while (channel != null || channel.isActive()) {
                JSONObject object = new JSONObject();
                object.put("message", sendMessage);
                object.put("to", "");
                object.put("from", "");
                object.put("type", "1");
                String s = object.toJSONString();
                channel.writeAndFlush(s + "\r\n");
            }

        });

    }


}

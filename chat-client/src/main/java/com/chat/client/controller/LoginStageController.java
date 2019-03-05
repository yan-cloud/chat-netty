package com.chat.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.chat.client.client.NettyClient;
import com.chat.client.common.StageContext;
import de.felixroske.jfxsupport.FXMLController;
import io.netty.channel.Channel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * @author ywd
 * 登录界面处理类
 */

@FXMLController
public class LoginStageController implements Initializable {

    private Logger log = LoggerFactory.getLogger(LoginStageController.class);

    @FXML
    private Button login;
    @FXML
    private Button cancel;

    @FXML
    private TextField username;
    @FXML
    private TextField passwd;

    @FXML
    private Button register;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
    }

    @FXML
    public void login() throws IOException {
        String name = username.getText().trim();
        String password = passwd.getText().trim();
        log.info("登录的用户名：{},密码：{}", name, password);
        Stage chatStage = StageContext.factory.createStage("会话", 600, 400, "view/ChatStage.fxml");
        Stage loginStage = StageContext.stageManager.getStage("loginStage");
        loginStage.close();
        chatStage.show();
        Channel channel = NettyClient.channel;
        //TODO 通过HTTP请求获取用户登录信息以及授权信息
        //tcp鉴权 发送个人信息
        JSONObject auth = new JSONObject();
        auth.put("userId", name);
        auth.put("type", "0");
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(auth.toJSONString());
        }
    }

    @FXML
    public void cancel() {
        username.setText(null);
        passwd.setText(null);
        log.info("清空用户名密码");
    }

    @FXML
    public void register() throws IOException {
        Stage register = StageContext.factory.createStage("注册", 600, 400, "view/RegisterStage.fxml");
        Stage loginStage = StageContext.stageManager.getStage("loginStage");
        loginStage.close();
        register.show();
        log.info("跳转用户注册页面");
    }
}

package com.chat.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.springframework.util.StringUtils;

public class RegisterStageController{
    @FXML
    private Button submit;
    @FXML
    private Button cancel;
    @FXML
    private Label showErrorMessage;

    @FXML
    private TextField name;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;

    public void submit (){
        String nameValue = name.getText().trim();
        String passwordValue = password.getText().trim();
        String confirmPasswordValue = confirmPassword.getText().trim();
        if(StringUtils.isEmpty(nameValue)){
            showErrorMessage.setText("用户名不能为空");
            showErrorMessage.setTextFill(Color.web("red"));
        }else {
            showErrorMessage.setText(null);
        }
        if(passwordValue!=null && !passwordValue.equals(confirmPasswordValue)){
            showErrorMessage.setText(null);
            showErrorMessage.setText("两次密码输入不一致");
            showErrorMessage.setTextFill(Color.web("red"));
        }else if(StringUtils.isEmpty(passwordValue) || StringUtils.isEmpty(confirmPasswordValue)){
            showErrorMessage.setText(null);
            showErrorMessage.setText("密码不能为空");
            showErrorMessage.setTextFill(Color.web("red"));
        }
    }

}

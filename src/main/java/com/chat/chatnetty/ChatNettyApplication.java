package com.chat.chatnetty;

import com.chat.chatnetty.common.StageContext;
import com.chat.chatnetty.view.LoginStageView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

/**
 * @author y19477
 * 启动类
 */
@SpringBootApplication
public class ChatNettyApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(ChatNettyApplication.class, LoginStageView.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ChatNettyApplication.class.getClassLoader().getResource("view/LoginStage.fxml"));
        Parent content = loader.load();
        Scene scene = new Scene(content);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("聊天");
        Pane pane = new Pane();
        ImageView splashImage = new ImageView(new Image(new File("/image/icon.jpg").toURI().toString()));
        splashImage.setEffect(new DropShadow(4, Color.GREY));
        pane.getChildren().add(splashImage);
        stage.setScene(scene);
        stage.show();
        StageContext.stageManager.addStage("loginStage",stage);
    }

    @Override
    public void beforeShowingSplash(Stage splashStage) {
        splashStage.setFullScreen(true);
    }
}

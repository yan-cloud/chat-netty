package com.chat.client.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * stage管理类实体
 * @author y19477
 */

public class StageFactory {

    private StageFactory() {
    }
    private static class SingletonHolder{
        private static StageFactory stageFactory = new StageFactory();
    }
    public static StageFactory getInstance(){
        return SingletonHolder.stageFactory;
    }
    private Logger log = LoggerFactory.getLogger(StageFactory.class);
    public Stage createStage(String title, int width, int height, String file) throws IOException {
        URL url = StageFactory.class.getClassLoader().getResource(file);
        Parent root = FXMLLoader.load(url);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        stage.setResizable(false);
        //设置在stage关闭时需要进行的操作
        stage.setOnCloseRequest(event -> {
            //此处当stage关闭时，同时结束程序，避免stage关闭后，程序界面关闭了，但后台线程却依然运行的问题
            log.info("关闭当前页面：{}",stage.getTitle());
            //System.exit(0);
        });
        StageContext.stageManager.addStage(file.split("\\.")[0], stage);
        return stage;
    }
}

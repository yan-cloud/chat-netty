package com.chat.chatnetty.common;

import javafx.stage.Stage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StageManager {
    private StageManager() {
    }
    private static class SingletonHolder{
        private static StageManager stageFactory = new StageManager();
    }
    public static StageManager getInstance(){
        return StageManager.SingletonHolder.stageFactory;
    }

    /**
     * 存放所有的Stage实例
     */
    private Map<String, Stage> stageMap = new ConcurrentHashMap<>();

    public void addStage(String name, Stage stage){
        stageMap.put(name, stage);
    }

    public Stage getStage(String name){
        return stageMap.get(name);
    }

    public void closeStage(String name){
        stageMap.get(name).close();
    }

    public void jump(String currentStageName, String targetStageName){
        stageMap.get(currentStageName).close();
        stageMap.get(targetStageName).show();
    }

    public void release(String name){
        stageMap.remove(name);
    }

}

package com.chat.common;


import de.felixroske.jfxsupport.SplashScreen;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class CustomSplash extends SplashScreen {
    private static String DEFAULT_IMAGE = "/image/icon.jpg";

    @Override
    public Parent getParent() {
        final ImageView imageView = new ImageView(DEFAULT_IMAGE);
        final ProgressBar splashProgressBar = new ProgressBar();
        splashProgressBar.setPrefWidth(imageView.getImage().getWidth());
        final VBox vbox = new VBox();
        vbox.getChildren().addAll(imageView, splashProgressBar);
        return vbox;
    }

    @Override
    public boolean visible() {
        return true;
    }

    @Override
    public String getImagePath() {
        return DEFAULT_IMAGE;
    }
}

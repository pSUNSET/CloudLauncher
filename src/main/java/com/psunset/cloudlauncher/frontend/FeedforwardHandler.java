package com.psunset.cloudlauncher.frontend;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import com.psunset.cloudlauncher.Launcher;

public class FeedforwardHandler {

    private static final Launcher LAUNCHER = Launcher.getInstance();

    public static void initFinished(){
        new Timeline(
                new KeyFrame(Duration.seconds(2.0), e -> Platform.runLater(() ->
                        LAUNCHER.webView.getEngine().executeScript("javascript:initFinished()")),
                        new KeyValue[0]
                )
        ).play();
    }

    public static void installStart(){
        new Timeline(
                new KeyFrame(Duration.seconds(2.0), e -> Platform.runLater(() ->
                        LAUNCHER.webView.getEngine().executeScript("javascript:installStart()")),
                        new KeyValue[0]
                )
        ).play();
    }

    public static void setInstallIndex(String text){
        new Timeline(
                new KeyFrame(Duration.seconds(2.0), e -> Platform.runLater(() ->
                        LAUNCHER.webView.getEngine().executeScript("javascript:setInstallIndex(\"" + text + "\")")),
                        new KeyValue[0]
                )
        ).play();
    }

    public static void installFinished(){
        new Timeline(
                new KeyFrame(Duration.seconds(2.0), e -> Platform.runLater(() ->
                        LAUNCHER.webView.getEngine().executeScript("javascript:installFinished()")),
                        new KeyValue[0]
                )
        ).play();
    }

    public static void repairStart(){
        new Timeline(
                new KeyFrame(Duration.seconds(2.0), e -> Platform.runLater(() ->
                        LAUNCHER.webView.getEngine().executeScript("javascript:repairStart()")),
                        new KeyValue[0]
                )
        ).play();
    }

    public static void repairFinished(){
        new Timeline(
                new KeyFrame(Duration.seconds(2.0), e -> Platform.runLater(() ->
                        LAUNCHER.webView.getEngine().executeScript("javascript:repairFinished()")),
                        new KeyValue[0]
                )
        ).play();
    }

    public static void clientClosed(){
        new Timeline(
                new KeyFrame(Duration.seconds(2.0), e -> Platform.runLater(() ->
                        LAUNCHER.webView.getEngine().executeScript("javascript:clientClosed()")),
                        new KeyValue[0]
                )
        ).play();
    }

    public static void clientStart(){
        new Timeline(
                new KeyFrame(Duration.seconds(2.0), e -> Platform.runLater(() ->
                        LAUNCHER.webView.getEngine().executeScript("javascript:clientStart()")),
                        new KeyValue[0]
                )
        ).play();
    }
}

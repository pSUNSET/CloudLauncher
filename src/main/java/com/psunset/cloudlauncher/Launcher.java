package com.psunset.cloudlauncher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import com.psunset.cloudlauncher.frontend.FeedbackHandler;
import com.psunset.cloudlauncher.util.Constants;
import com.psunset.cloudlauncher.util.Reference;

import javax.swing.*;
import java.util.TimeZone;

public class Launcher extends Application {

    public static void main(String[] args){
        launch(Launcher.class, args);
        Reference.ISO_8601.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private static Launcher INSTANCE;
    public final FeedbackHandler feedbackHandler = new FeedbackHandler();
    public final WebView webView = new WebView();

    public static Launcher getInstance() {
        return INSTANCE;
    }

    /**
     * Installer gui start.
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        INSTANCE = this;

        final VBox layout = new VBox();

        layout.getChildren().add(webView);
        stage.setScene(new Scene(layout));
        stage.setTitle("Cloud Launcher -v" + Constants.getLauncherVersion());
        stage.getIcons().add(new Image(Constants.getIcon()));
        stage.setResizable(false);
        stage.setWidth(1063);
        stage.setHeight(638);
        webView.setContextMenuEnabled(false);

        // Connect to index.html
        connectFrontend();

        stage.show();
    }

    /**
     * Connect to index.html
     */
    private void connectFrontend(){
        Platform.runLater(() -> {
            webView.getEngine().load(ClassLoader.getSystemResource("theme/index.html").toExternalForm());
            webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED){
                    ((JSObject)webView.getEngine().executeScript("window")).setMember("feedback", feedbackHandler);
                    if (webView.getEngine().getLocation().toLowerCase().contains("theme/index.html")){
                        System.out.println("Successfully connect to frontend.");
                    }
                }
            });
        });
    }


    /**
     * Launcher die
     * @param e
     */
    public void die(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, e.getMessage(), "An error occurred.", JOptionPane.ERROR_MESSAGE);
        stopApplication();
    }

    public void stopApplication() {
        System.exit(0);
    }
}
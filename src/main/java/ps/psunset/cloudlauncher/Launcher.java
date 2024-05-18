package ps.psunset.cloudlauncher;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import ps.psunset.cloudlauncher.js.FeedbackHandler;
import ps.psunset.cloudlauncher.util.Constants;

import javax.swing.*;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(Launcher.class, args);
    }

    private static Launcher INSTANCE;
    private final FeedbackHandler feedbackHandler = new FeedbackHandler();
    private final WebView webView = new WebView();

    public static String NAME = "CloudClient";
    public static String VERSION = Constants.getLauncherVersion();
    public static String GAME_VERSION = Constants.getGameVersion();
    public static String TITLE = "Cloud Client -v" + VERSION;
    public static String NAME_VERSION = NAME.toLowerCase() + "-" + GAME_VERSION;

    private final int totalIndex = 3;
    private int currentIndex;

    public static Launcher getInstance() {
        return INSTANCE;
    }

    @Override
    public void start(Stage stage) throws Exception {
        INSTANCE = this;

        final VBox layout = new VBox();

        layout.getChildren().add(webView);
        stage.setScene(new Scene(layout));
        stage.setTitle(TITLE);
        if (Constants.getIcon() != null){
            stage.getIcons().add(new Image(Constants.getIcon()));
        }
        stage.setResizable(false);
        stage.setWidth(1063);
        stage.setHeight(620);
        webView.setContextMenuEnabled(false);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webView.getEngine().load(ClassLoader.getSystemResource("index.html").toExternalForm());
                webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED){
                        ((JSObject)webView.getEngine().executeScript("window")).setMember("feedback", feedbackHandler);
                        if (webView.getEngine().getLocation().toLowerCase().contains("index.html")){
                            registerWorkers();
                        }
                    }

                });
            }
        });

        stage.show();

    }

    private void registerWorkers(){
        new LauncherThread(this).start();
        progressPlus();
        new Timeline(new KeyFrame[] {
            new KeyFrame(Duration.minutes(2), e -> {
                if (this.currentIndex >= totalIndex){
                    die(new Exception("Installer timed out."));
                }
            },
            new KeyValue[0])
        }).play();
    }

    /**
     * Ensure to set totalIndex.
     */
    public void progressPlus() {
        this.currentIndex++;
        if (currentIndex >= totalIndex){
            new Timeline(
                    new KeyFrame(Duration.seconds(4.0), e -> Platform.runLater(() ->
                                    this.webView.getEngine().executeScript("javascript:finished()")),
                            new KeyValue[0]
                    )
            ).play();
        }
    }

    public void die(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, e.getMessage(), "An error occurred", JOptionPane.ERROR_MESSAGE);
        stopApplication();
    }

    public void stopApplication() {
        System.exit(0);
    }
}
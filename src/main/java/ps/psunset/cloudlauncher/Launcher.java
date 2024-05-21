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
    public final FeedbackHandler feedbackHandler = new FeedbackHandler();
    public final WebView webView = new WebView();

    public static Launcher getInstance() {
        return INSTANCE;
    }

    /**
     * Installer gui start.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        INSTANCE = this;

        final VBox layout = new VBox();

        layout.getChildren().add(webView);
        stage.setScene(new Scene(layout));
        stage.setTitle(Constants.getLauncherTitle());
        if (Constants.getIcon() != null){
            stage.getIcons().add(new Image(Constants.getIcon()));
        }
        stage.setResizable(false);
        stage.setWidth(1063);
        stage.setHeight(620);
        webView.setContextMenuEnabled(false);

        // Connect to index.html
        connectHTML();

        stage.show();
    }

    /**
     * Connect to index.html
     */
    private void connectHTML(){
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
    }

    private void registerWorkers(){
        new LauncherThread(this).start();

        // Make sure whether helper is timed out
        new Timeline(new KeyFrame[] {
                new KeyFrame(Duration.minutes(2), e -> {
                    if (notWorking()){
                        die(new Exception("Installer timed out."));
                    }
                },
                new KeyValue[0])
        }).play();
    }

    /**
     * @return is code running or idle
     */
    public boolean notWorking(){
        return false;
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
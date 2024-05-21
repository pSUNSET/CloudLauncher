package ps.psunset.cloudlauncher.js;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import ps.psunset.cloudlauncher.Launcher;

public class Javascript {

    private static final Launcher launcher = Launcher.getInstance();

    public static void installStart(){
        new Timeline(
                new KeyFrame(Duration.seconds(4.0), e -> Platform.runLater(() ->
                        launcher.webView.getEngine().executeScript("javascript:installStart()")),
                        new KeyValue[0]
                )
        ).play();
    }

    public static void installFinished(){
        new Timeline(
                new KeyFrame(Duration.seconds(4.0), e -> Platform.runLater(() ->
                        launcher.webView.getEngine().executeScript("javascript:installFinished()")),
                        new KeyValue[0]
                )
        ).play();
    }

    public static void installShutdown(){
        new Timeline(
                new KeyFrame(Duration.seconds(4.0), e -> Platform.runLater(() ->
                        launcher.webView.getEngine().executeScript("javascript:installShutdown()")),
                        new KeyValue[0]
                )
        ).play();
    }
}

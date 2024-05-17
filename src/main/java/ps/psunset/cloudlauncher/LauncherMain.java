package ps.psunset.cloudlauncher;

import javafx.application.Application;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class LauncherMain extends Application {
    public static void main(String[] args) {
        Application.launch(LauncherMain.class);
    }

    private static LauncherMain INSTANCE;
    private final FeedbackHandler feedbackHandler = new FeedbackHandler();
    private final WebView webView = new WebView();

    private int currentIndex;

    @Override
    public void start(Stage stage) throws Exception {
        INSTANCE = this;


    }
}
package ps.psunset.cloudlauncher.js;

import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.client.LaunchHandler;

/**
 * Used in "assets/script.js"
 */
public class FeedbackHandler {

    public void install(){
        InstallHandler.install();
    }

    public void launch(){
        LaunchHandler.launch();
    }
}

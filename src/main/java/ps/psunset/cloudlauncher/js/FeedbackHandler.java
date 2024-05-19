package ps.psunset.cloudlauncher.js;

import ps.psunset.cloudlauncher.Launcher;

/**
 * Used in "assets/script.js"
 */
public class FeedbackHandler {

    // Called from the javascript
    public void close(){
        Launcher.getInstance().stopApplication();
    }
}

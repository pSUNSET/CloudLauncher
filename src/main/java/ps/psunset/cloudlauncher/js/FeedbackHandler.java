package ps.psunset.cloudlauncher.js;

import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.client.LaunchHandler;
import ps.psunset.cloudlauncher.client.RepairHandler;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.LaunchOption;
import ps.psunset.cloudlauncher.util.MCPathHelper;

/**
 * Used in "assets/script.js"
 */
public class FeedbackHandler {

    public void install(){
        InstallHandler.install();
    }

    public void launch() throws Exception {
        LaunchHandler.launch();
    }

    public void repair() {
        RepairHandler.repair();
    }

    public void setGameVersion(String gameVersion) {
        Constants.setGameVersion(gameVersion);
    }

    public void setMaximumRamMb(int ram) {
        if (ram <= 0){
            LaunchOption.customMaximumRamMb = 0;
        } else {
            LaunchOption.customMaximumRamMb = ram;
        }
    }

    public void setMcDir(String mcDir) {
        MCPathHelper.setMcDir(mcDir);
    }
}

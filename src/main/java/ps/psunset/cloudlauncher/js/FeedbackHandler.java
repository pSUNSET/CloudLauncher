package ps.psunset.cloudlauncher.js;

import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.client.LaunchHandler;
import ps.psunset.cloudlauncher.client.RepairHandler;
import ps.psunset.cloudlauncher.util.ConfigHelper;
import ps.psunset.cloudlauncher.util.Constants;

import java.io.IOException;

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

    public String getGameVersion() {
        return Constants.getGameVersion();
    }

    public void setConfig(String key, String value) {
        ConfigHelper.setConfig(key, value);
    }

    public String getConfig(String key){
        if (ConfigHelper.getConfig(key).isEmpty()){
            return "";
        }
        return ConfigHelper.getConfig(key);
    }
}

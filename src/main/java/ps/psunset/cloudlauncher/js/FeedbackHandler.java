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

    public void setConfig(Object key, Object value) {
        ConfigHelper.setConfig(key.toString(), value.toString());

        System.out.println(key.toString() + ": " + value.toString());
    }

    public String getConfig(Object key){
        if (ConfigHelper.getConfig(key.toString()).isEmpty()){
            return "";
        }

        System.out.println(key + ": " + ConfigHelper.getConfig(key.toString()));

        return ConfigHelper.getConfig(key.toString());
    }

    public void print(Object str){
        System.out.println(str);
    }
}

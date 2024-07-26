package com.psunset.cloudlauncher.js;

import com.psunset.cloudlauncher.client.InitHandler;
import com.psunset.cloudlauncher.client.InstallHandler;
import com.psunset.cloudlauncher.client.LaunchHandler;
import com.psunset.cloudlauncher.client.RepairHandler;
import com.psunset.cloudlauncher.util.Constants;

/**
 * Used in "assets/script.theme"
 */
public class FeedbackHandler {

    public void init(){
        InitHandler.init();
    }

    public void install(){
        InstallHandler.install();
    }

    public void launch() throws Exception {
        LaunchHandler.launch();
    }

    public void repair() {
        RepairHandler.repair();
    }
}

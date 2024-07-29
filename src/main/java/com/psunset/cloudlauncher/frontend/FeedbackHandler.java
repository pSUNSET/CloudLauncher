package com.psunset.cloudlauncher.frontend;

import com.psunset.cloudlauncher.client.InitHandler;
import com.psunset.cloudlauncher.client.InstallHandler;
import com.psunset.cloudlauncher.client.LaunchHandler;
import com.psunset.cloudlauncher.client.RepairHandler;

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

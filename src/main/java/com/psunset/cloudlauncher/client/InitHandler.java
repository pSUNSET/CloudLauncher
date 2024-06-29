package com.psunset.cloudlauncher.client;


import com.psunset.cloudlauncher.js.FeedforwardHandler;
import com.psunset.cloudlauncher.util.database.ConfigHelper;

public class InitHandler {

    /**
     * Init Client
     */
    public static void init(){
        new Thread(() -> {
            ConfigHelper.update();

            FeedforwardHandler.initFinished();
        }).start();
    }
}

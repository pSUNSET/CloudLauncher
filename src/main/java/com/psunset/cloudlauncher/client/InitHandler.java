package com.psunset.cloudlauncher.client;


import com.psunset.cloudlauncher.frontend.FeedforwardHandler;
import com.psunset.cloudlauncher.database.ConfigHelper;

public class InitHandler {

    /**
     * Init Client
     */
    public static void init(){
        new Thread(() -> {
            if (ConfigHelper.init()){
                FeedforwardHandler.initFinished();
            }
        }).start();
    }
}

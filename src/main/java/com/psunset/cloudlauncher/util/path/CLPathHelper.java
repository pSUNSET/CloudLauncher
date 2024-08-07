package com.psunset.cloudlauncher.util.path;

import java.io.File;

/**
 * Check out User's OS system and get cloud launcher dictionary.
 */
public enum CLPathHelper {
    WINDOWS("Cloud Launcher"),
    MAC("Cloud Launcher"),
    LINUX("Cloud Launcher");

    private final String cl;

    public String getCl(){
        return cl;
    }

    private CLPathHelper(String cl){
        this.cl = cl + File.separator;
    }

    public static final CLPathHelper getOS(){
        final String correctOS = System.getProperty("os.name").toLowerCase();

        if (correctOS.startsWith("windows")){
            return WINDOWS;
        } else if (correctOS.startsWith("mac")){
            return MAC;
        } else if (correctOS.startsWith("linux")){
            return LINUX;
        }
        return null;
    }
}

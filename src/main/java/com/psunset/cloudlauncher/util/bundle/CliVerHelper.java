package com.psunset.cloudlauncher.util.bundle;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Find Client Version List in .properties file
 */
public class CliVerHelper {


    /**
     * Find Version in /data/cli-version.properties file
     * @param gameVersion
     * @return
     */
    public static String getClientVersion(String gameVersion) {
        ResourceBundle messages = ResourceBundle.getBundle("data/cli-version", Locale.getDefault());
        MessageFormat formatter = new MessageFormat(messages.getString(gameVersion));
        return formatter.toPattern();
    }
}

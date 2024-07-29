package com.psunset.cloudlauncher.util;

import com.psunset.cloudlauncher.database.ConfigHelper;

import java.io.*;
import java.util.Locale;

/**
 * Get launcher details.
 */
public class Constants {

    private static final String launcherName = "CloudClient";
    private static final String launcher_Name = "Cloud Client";
    private static final String launcherTitle = "Cloud Client -v" + getLauncherVersion();

    private static String launcherVersion = "0.0.1";
    private static String clientVersion;
    private static Locale locale;

    public static String getLauncherName(){
        return launcherName;
    }

    public static String getLauncher_Name(){
        return launcher_Name;
    }

    /**
     * @return { LauncherName } -v{ Version }
     */
    public static String getLauncherTitle(){
        return launcherTitle;
    }

    /**
     * @return { LauncherName }-{ GameVersion }
     */
    public static String getLauncherNameVersion(){
        return getLauncherName().toLowerCase() + "-" + ConfigHelper.Type.SELECT_VERSION.getValue();
    }

    public static String getLauncherVersion() {
        return launcherVersion;
    }

    public static String getGameVersion(){
        ConfigHelper.refresh();
        return ConfigHelper.Type.SELECT_VERSION.getValue();
    }

    public static String getLoaderVersion() {
        String gameVersion = getGameVersion();
        return switch (gameVersion){
            case "1.20.6" -> "0.15.11";

            default -> throw new IllegalStateException("Unexpected value: " + gameVersion);
        };
    }

    public static InputStream getIcon(){
        return ClassLoader.getSystemResourceAsStream("theme/assets/icon_16x16.png");
    }

    public static void setLocale(Locale locale) {
        Constants.locale = locale;
    }

    public static Locale getLocale(){
        if (locale == null){
            return Locale.getDefault();
        }
        return locale;
    }
}

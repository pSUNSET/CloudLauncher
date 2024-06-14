package ps.psunset.cloudlauncher.util;

import ps.psunset.cloudlauncher.Launcher;

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
    private static String gameVersion = "1.20.6";
    private static String loaderVersion = "0.15.11";
    private static String clientVersion = null;
    private static Locale locale = null;

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
        return getLauncherName().toLowerCase() + "-" + getGameVersion();
    }

    public static String getLauncherVersion() {
        return launcherVersion;
    }

    public static void setGameVersion(String version){
        gameVersion = version;
    }

    public static String getGameVersion(){
        return gameVersion;
    }

    public static void setLoaderVersion(String version) {
        Constants.loaderVersion = version;
    }

    public static String getLoaderVersion() {
        return loaderVersion;
    }

    public static InputStream getIcon(){
        return ClassLoader.getSystemResourceAsStream("assets/launcher/icon_16x16.png");
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

    public static String getClientVersion(){
        if (clientVersion == null){
            clientVersion = BundleHelper.getClientVersion("1.20.6");
        }
        return clientVersion;
    }
}

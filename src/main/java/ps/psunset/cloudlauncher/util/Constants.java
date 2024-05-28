package ps.psunset.cloudlauncher.util;

import ps.psunset.cloudlauncher.Launcher;

import java.io.*;
import java.util.Locale;
import java.util.Objects;

/**
 * Get launcher details.
 */
public class Constants {

    private static final String launcherName = "CloudClient";
    private static final String launcher_Name = "Cloud Client";
    private static final String launcherTitle = "Cloud Client -v" + getLauncherVersion();

    private static String launcherVersion = null;
    private static String gameVersion = "1.20.6";
    private static String loader = null;
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
        if (launcherVersion == null){
            try{
                InputStream stream = ClassLoader.getSystemResourceAsStream("assets/version.txt");
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader buffReader = new BufferedReader(reader);
                launcherVersion = buffReader.readLine();
                buffReader.close();
                reader.close();
                stream.close();
            } catch (IOException e){
                e.printStackTrace();
                Launcher.getInstance().die(e);
            }
        }
        return launcherVersion;
    }

    public static void setGameVersion(String version){
        gameVersion = version;
    }

    public static String getGameVersion(){
        return gameVersion;
    }

    public static boolean isOldVersion(){
        return Objects.equals(getGameVersion(), "1.8.9");
    }

    public static void setLoaderVersion(String version) {
        Constants.loaderVersion = version;
    }

    public static String getLoaderVersion() {
        return loaderVersion;
    }

    public static InputStream getIcon() throws IOException {
        return ClassLoader.getSystemResourceAsStream("assets/icon_1024x1024.png");
    }

    public static void setLocale(Locale locale) {
        Constants.locale = locale;
    }

    public static Locale getLocale(){
        if (locale == null){
            return Locale.US;
        }
        return locale;
    }

    public static String getClientVersion(){
        if (clientVersion == null){
            clientVersion = VersionHelper.getClientVersion("1.20.6");
        }
        return clientVersion;
    }
}

package ps.psunset.cloudlauncher.util;

import java.io.*;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

/**
 * Get launcher details.
 */
public class Constants {

    private static String launcherVersion = null;
    private static String gameVersion = "1.20.6";
    private static String loader = null;
    private static String loaderVersion = "0.15.11";
    private static Locale locale = null;

    public static String getLauncherVersion(){
        if (launcherVersion == null){
            /*try{
                InputStream stream = FileHelper.getStreamFromURL(LAUNCHER_URL + "version.txt");
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader buffReader = new BufferedReader(reader);
                version = buffReader.readLine();
                buffReader.close();
                reader.close();
                stream.close();
            } catch (IOException e){
                e.printStackTrace();
                Launcher.getInstance().die(e);
            }*/
        }
        launcherVersion = "0.0.1";
        return launcherVersion;
    }

    private static void setGameVersion(String version){
        gameVersion = version;
    }

    public static String getGameVersion(){
        return gameVersion;
    }

    public static boolean isOldVersion(){
        return Objects.equals(getGameVersion(), "1.8.9");
    }



    public static String getLoader() {
        loader = isOldVersion() ? "forge" : "fabric";
        return loader;
    }

    public static void setLoaderVersion(String version) {
        Constants.loaderVersion = version;
    }

    public static String getLoaderVersion() {
        return loaderVersion;
    }

    public static Path getPath(String path){
        return Path.of(path);
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
}

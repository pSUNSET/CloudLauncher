package ps.psunset.cloudlauncher.util;

import java.io.File;

/**
 * Check out User's OS system.
 */
public enum MCPathHelper {
    WINDOWS("Appdata" + File.separator + "Roaming" + File.separator + ".minecraft"),
    MAC("Library" + File.separator + "Application Support" + File.separator + "minecraft"),
    LINUX(".minecraft");

    private final String mc;
    private static String customMc = "";

    private MCPathHelper(String mc){
        this.mc = File.separator + mc + File.separator;
    }

    /**
     * @return Minecraft directory
     */
    public String getMc() {
        if (customMc != "") {
            return customMc;
        } else {
            return System.getProperty("user.home") + mc;
        }
    }

    /**
     * @return User's OS
     */
    public static final MCPathHelper getOS(){
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

    /**
     * @return "Minecraft Directory/assets" directory
     */
    public String getAssetsDir(){
        return getMc() + "assets" + File.separator;
    }

    /**
     * @return "Minecraft Directory/versions/cloudclient-?.?.?" directory
     */
    public String getVersionDir(){
        return getMc() + "versions" + File.separator + Constants.getLauncherNameVersion() + File.separator;
    }

    /**
     * @return "Minecraft Directory/cloudclient" directory
     */
    public String getClientDir(){
        return getMc() + Constants.getLauncherName().toLowerCase() + File.separator;
    }

    /**
     * @return "Minecraft Directory/cloudclient/libraries" directory
     */
    public String getLibrariesDir(){
        return getClientDir() + "libraries" + File.separator;
    }

    /**
     * @return "Minecraft Directory/bin/cloudclient" directory
     */
    public String getNativesDir(){
        return getMc() + "bin" + File.separator + Constants.getLauncherName().toLowerCase() + File.separator;
    }

    /**
     * @return "Minecraft Directory/cloudclient/mods" directory
     */
    public String getModsDir(){
        return getClientDir() + "mods" + File.separator;
    }

    public static void setMcDir(String mc){
        customMc = mc;
    }
}

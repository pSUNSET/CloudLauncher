package com.psunset.cloudlauncher.util.path;

import com.psunset.cloudlauncher.database.ConfigHelper;
import com.psunset.cloudlauncher.util.Constants;

import java.io.File;

/**
 * Check out User's OS system and get mc dictionary.
 */
public enum MCPathHelper {
    WINDOWS("Appdata" + File.separator + "Roaming" + File.separator + ".minecraft"),
    MAC("Library" + File.separator + "Application Support" + File.separator + "minecraft"),
    LINUX(".minecraft");

    private final String mc;

    private MCPathHelper(String mc){
        this.mc = File.separator + mc + File.separator;
    }

    /**
     * @return Minecraft directory
     */
    public String getMc() {
        return System.getProperty("user.home") + mc;
    }

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
        final String customMc = ConfigHelper.Type.MC_PATH.getValue();

        if (customMc.isEmpty()) {
            return getMc() + Constants.getLauncherName().toLowerCase() + File.separator;
        } else {
            return customMc + File.separator;
        }
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
}

package ps.psunset.cloudlauncher.util;

import ps.psunset.cloudlauncher.Launcher;

import java.io.File;

/**
 * Check out User's OS system.
 */
public enum OSHelper {
    WINDOWS("Appdata" + File.separator + "Roaming" + File.separator + ".minecraft"),
    MAC("Library" + File.separator + "Application Support" + File.separator + "minecraft"),
    LINUX(".minecraft");

    private final String mc;

    private OSHelper(String mc){
        this.mc = File.separator + mc + File.separator;
    }

    /**
     * @return Minecraft directory
     */
    public String getMc() {
        return System.getProperty("user.home") + mc;
    }

    /**
     * @return User's OS
     */
    public static final OSHelper getOS(){
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
     * @return "Minecraft Directory/cloudclient" directory
     */
    public String getClientDir(){
        return getMc() + Constants.getLauncherName().toLowerCase() + File.separator;
    }

    /**
     * @return "Minecraft Directory/cloudclient/mods" directory
     */
    public String getModsDir(){
        return getClientDir() + "mods" + File.separator;
    }
}

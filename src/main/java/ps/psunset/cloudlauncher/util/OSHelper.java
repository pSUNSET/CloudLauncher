package ps.psunset.cloudlauncher.util;

import java.io.File;

public enum OSHelper {
    WINDOWS("Appdata" + File.separator + "Roaming" + File.separator + ".minecraft"),
    MAC("Library" + File.separator + "Application Support" + File.separator + "minecraft"),
    LINUX(".minecraft");

    private final String mc;

    private OSHelper(String mc){
        this.mc = File.separator + mc + File.separator;
    }

    public String getMc() {
        return System.getProperty("user.home") + mc;
    }

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
}
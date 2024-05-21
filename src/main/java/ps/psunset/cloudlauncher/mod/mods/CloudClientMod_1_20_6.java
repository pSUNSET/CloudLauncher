package ps.psunset.cloudlauncher.mod.mods;

import ps.psunset.cloudlauncher.mod.ModFile;
import ps.psunset.cloudlauncher.util.Constants;

import java.io.InputStream;

public class CloudClientMod_1_20_6 extends ModFile {
    public static String modVersion;

    public CloudClientMod_1_20_6() {
        super("1.20.6",
                "liquidbounce(cloudaddon)",
                Constants.getClientVersion(),
                "https://github.com/pSUNSET/CloudClient/releases/download/CloudClient/liquidbounce.cloudaddon.-" + Constants.getClientVersion() + ".jar");
    }
}

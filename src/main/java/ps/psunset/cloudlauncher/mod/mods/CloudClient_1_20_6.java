package ps.psunset.cloudlauncher.mod.mods;

import ps.psunset.cloudlauncher.mod.ModFile;
import ps.psunset.cloudlauncher.util.Constants;

public class CloudClient_1_20_6 extends ModFile {

    public CloudClient_1_20_6() {
        super("1.20.6",
                "cloudclient",
                Constants.getClientVersion(),
                "https://github.com/pSUNSET/CloudClient/releases/download/CloudClient/cloudclient-" + Constants.getClientVersion() + ".jar");
    }
}

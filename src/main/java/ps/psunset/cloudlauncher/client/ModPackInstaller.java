package ps.psunset.cloudlauncher.client;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.mod.ModFile;
import ps.psunset.cloudlauncher.mod.ModPackManager;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.OSHelper;
import ps.psunset.cloudlauncher.util.OutputHelper;

import java.io.File;
import java.io.IOException;

public class ModPackInstaller {

    /**
     * Install mod pack
     */
    public static void install(String gameVersion, Launcher launcher) throws IOException {
        System.out.println(OutputHelper.getMessage("progress.installing.modpack"));
        File modsDir = new File(OSHelper.getOS().getModsDir());
        for (ModFile mod : ModPackManager.getMods(Constants.getGameVersion())) {
            FileUtils.copyURLToFile(mod.getURL(), new File(modsDir + mod.getFileName()));
        }
        System.out.println(OutputHelper.getMessage("progress.finished.modpack"));
        launcher.progressPlus();
    }
}

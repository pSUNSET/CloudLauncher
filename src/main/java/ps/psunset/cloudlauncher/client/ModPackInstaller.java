package ps.psunset.cloudlauncher.client;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.mod.ModFile;
import ps.psunset.cloudlauncher.mod.ModPackManager;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.OSHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

public class ModPackInstaller {

    public static void install(String gameVersion, Launcher launcher) throws IOException {
        System.out.println("Installing mod pack");
        File modsDir = new File(OSHelper.getOS().getModsDir());
        for (ModFile mod : ModPackManager.getJar(Constants.getGameVersion())) {
            FileUtils.copyURLToFile(mod.getURL(), new File(modsDir + mod.getFileName()));
        }
        System.out.println("Mod pack installing finished");
        launcher.progressPlus();
    }
}

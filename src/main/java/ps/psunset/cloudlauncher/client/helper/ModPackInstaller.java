package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.js.Javascript;
import ps.psunset.cloudlauncher.mod.ModFile;
import ps.psunset.cloudlauncher.mod.ModPackManager;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.OSHelper;
import ps.psunset.cloudlauncher.util.OutputHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ModPackInstaller {

    /**
     * Install mod pack
     */
    public static void install(String gameVersion) throws IOException {
        System.out.println(OutputHelper.getMessage("progress.installing.modpack"));

        File modsDir = new File(OSHelper.getOS().getModsDir());

        for (ModFile latestMod : ModPackManager.getMods(gameVersion)) {
            System.out.print(latestMod.getName());

            boolean needUpdate = true;

            if (Path.of(OSHelper.getOS().getModsDir()).toFile().listFiles() != null){
                for (File mod : Path.of(OSHelper.getOS().getModsDir()).toFile().listFiles()) {

                    if (mod.getName().contains(latestMod.getFileName())) {
                        if (!mod.getName().equals(latestMod.getName())) {
                            System.out.println(", need to update");
                            FileUtils.delete(new File(modsDir + "/" + mod.getName()));
                        } else {
                            needUpdate = false;
                            System.out.println(", no need to update");
                            break;
                        }
                    }
                }
            }

            if (needUpdate){
                FileUtils.copyURLToFile(latestMod.getURL(), new File(modsDir + "/" + latestMod.getName()));
            }
        }
        System.out.println(OutputHelper.getMessage("progress.finished.modpack"));
        InstallHandler.progressPlus();
    }
}

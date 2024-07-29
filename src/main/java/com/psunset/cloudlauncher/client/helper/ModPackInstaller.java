package com.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import com.psunset.cloudlauncher.frontend.FeedforwardHandler;
import com.psunset.cloudlauncher.mod.ModFile;
import com.psunset.cloudlauncher.mod.ModPackManager;
import com.psunset.cloudlauncher.util.path.MCPathHelper;
import com.psunset.cloudlauncher.util.bundle.OutputHelper;

import java.io.File;
import java.nio.file.Path;

public class ModPackInstaller {

    /**
     * Install mod pack
     */
    public static void install(String gameVersion) throws Exception {
        System.out.println("Installing modpack");
        FeedforwardHandler.setInstallIndex(OutputHelper.getOutputMessage("progress.installing.modpack"));

        File modsDir = new File(MCPathHelper.getOS().getModsDir());

        for (ModFile latestMod : ModPackManager.getMods(gameVersion)) {
            System.out.println("|-- " + latestMod.getName());

            boolean needUpdate = true;
            File[] mods = modsDir.listFiles();

            if (mods != null){
                for (File mod : mods) {

                    if (mod.getName().contains(latestMod.getFileName())) {
                        System.out.println("    |-- now version: " + mod.getName());
                        if (!mod.getName().equals(latestMod.getName())) {
                            System.out.println("    |-- need to update");
                            FileUtils.delete(new File(modsDir + "/" + mod.getName()));
                        } else if (!mod.getName().isEmpty()){
                            needUpdate = false;
                            System.out.println("    |-- no need to update");
                            break;
                        }
                    }
                }
            }

            if (needUpdate){
                FileUtils.copyURLToFile(latestMod.getURL(), new File(modsDir + "/" + latestMod.getName()));
            }
        }
    }

    /**
     * Forcibly install mod pack
     */
    public static void forceInstall(String gameVersion) throws Exception {
        System.out.println("Installing modpack");
        FeedforwardHandler.setInstallIndex(OutputHelper.getOutputMessage("progress.installing.modpack"));

        File modsDir = new File(MCPathHelper.getOS().getModsDir());

        File[] mods = Path.of(MCPathHelper.getOS().getModsDir()).toFile().listFiles();

        if (mods != null){
            for (File mod : mods) {

                FileUtils.delete(new File(modsDir + "/" + mod.getName()));
            }
        }

        for (ModFile latestMod : ModPackManager.getMods(gameVersion)) {
            System.out.println("|--" + latestMod.getName());
            FileUtils.copyURLToFile(latestMod.getURL(), new File(modsDir + "/" + latestMod.getName()));
        }
    }
}

package com.psunset.cloudlauncher.client.helper;

import com.psunset.cloudlauncher.database.ConfigHelper;
import org.apache.commons.io.FileUtils;
import com.psunset.cloudlauncher.frontend.FeedforwardHandler;
import com.psunset.cloudlauncher.util.*;
import com.psunset.cloudlauncher.util.bundle.OutputHelper;
import com.psunset.cloudlauncher.util.path.MCPathHelper;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

public class NativesInstall {

    static File zipDir = new File(MCPathHelper.getOS().getClientDir() + "natives-" + ConfigHelper.Type.SELECT_VERSION.getValue() + ".zip");
    static File nativesDir = new File(MCPathHelper.getOS().getNativesDir());

    /**
     * Download native files
     * @param gameVersion
     * @throws Exception
     */
    public static void install(String gameVersion) throws Exception {
        System.out.println("Installing native files");
        FeedforwardHandler.setInstallIndex(OutputHelper.getOutputMessage("progress.installing.natives"));

        switch (gameVersion){

            // 1.20.6
            case "1.20.6":
                if (zipDir.createNewFile()){
                    FileUtils.copyURLToFile(new URL(Reference.NATIVES_1_20_6_URL), zipDir);
                    FileHelper.unzip(zipDir.toString(), nativesDir.toString());
                } else {
                    System.out.println("|-- no need to update");
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }

    }

    /**
     * Forcibly Download native files
     * @param gameVersion
     * @throws Exception
     */
    public static void forceDownload(String gameVersion) throws Exception {
        if (nativesDir.listFiles() != null) {
            for (File file: nativesDir.listFiles()) {
                FileUtils.delete(file);
            }
        }

        Files.deleteIfExists(zipDir.toPath());
        Files.deleteIfExists(nativesDir.toPath());

        install(gameVersion);
    }
}

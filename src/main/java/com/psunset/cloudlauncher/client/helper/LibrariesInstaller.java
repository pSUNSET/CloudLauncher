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

/**
 * Download libraries
 */
public class LibrariesInstaller {
    static File librariesDir = new File(MCPathHelper.getOS().getLibrariesDir());
    static File zipDir = new File(MCPathHelper.getOS().getClientDir() + "libraries-" + ConfigHelper.Type.SELECT_VERSION.getValue() + ".zip");

    /**
     * Download libraries
     * @param gameVersion
     * @throws Exception
     */
    public static void install(String gameVersion) throws Exception{
        System.out.println("Installing libraries");
        FeedforwardHandler.setInstallIndex(OutputHelper.getOutputMessage("progress.installing.libraries"));

        switch (gameVersion){

            // 1.20.6
            case "1.20.6":
                if (zipDir.createNewFile()){
                    FileUtils.copyURLToFile(new URL(Reference.LIBRARIES_1_20_6_URL), zipDir);
                    FileHelper.unzip(zipDir.toString(), librariesDir.toString());
                } else {
                    System.out.println("|-- no need to update");
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }
    }

    /**
     * Forcibly download libraries
     * @param gameVersion
     * @throws Exception
     */
    public static void forceDownload(String gameVersion) throws Exception{
        if (librariesDir.listFiles() != null) {
            for (File file: librariesDir.listFiles()) {
                FileUtils.delete(file);
            }
        }

        Files.deleteIfExists(librariesDir.toPath());
        Files.deleteIfExists(zipDir.toPath());

        install(gameVersion);
    }
}

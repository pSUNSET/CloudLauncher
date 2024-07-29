package com.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import com.psunset.cloudlauncher.frontend.FeedforwardHandler;
import com.psunset.cloudlauncher.util.*;
import com.psunset.cloudlauncher.util.bundle.OutputHelper;
import com.psunset.cloudlauncher.util.path.MCPathHelper;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

public class ClientInstaller {
    static File zipDir = new File(MCPathHelper.getOS().getVersionDir() + Constants.getLauncherNameVersion() + ".jar");

    /**
     * Install Client (jar file)
     */
    public static void install(String gameVersion) throws Exception{
        System.out.println("Installing cloud client");
        FeedforwardHandler.setInstallIndex(OutputHelper.getOutputMessage("progress.installing.client", new Object[]{ Constants.getLauncherTitle() }));


        switch (gameVersion){
            // 1.20.6
            case "1.20.6":
                if (zipDir.createNewFile()) {
                    FileUtils.copyURLToFile(new URL(Reference.FABRIC_1_20_6_CLIENT_URL), zipDir);
                } else {
                    System.out.println("|-- no need to update");
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }
    }

    /**
     * Forcibly install Client (jar file)
     */
    public static void forceInstall(String gameVersion) throws Exception{
        Files.deleteIfExists(zipDir.toPath());

        install(gameVersion);
    }
}

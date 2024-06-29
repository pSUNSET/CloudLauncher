package com.psunset.cloudlauncher.client;

import com.psunset.cloudlauncher.Launcher;
import com.psunset.cloudlauncher.client.helper.*;
import com.psunset.cloudlauncher.js.FeedforwardHandler;
import com.psunset.cloudlauncher.util.Constants;
import com.psunset.cloudlauncher.util.path.MCPathHelper;
import com.psunset.cloudlauncher.util.bundle.OutputHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RepairHandler{

    private static final Launcher LAUNCHER = Launcher.getInstance();

    public static boolean isRunning = false;

    /**
     * Repair game start
     */
    public static void repair(){
        // Avoid re-repairing
        if (!canRepair() || InstallHandler.isRunning){
            return;
        }

        isRunning = true;
        System.out.println("Repairing start");
        FeedforwardHandler.repairStart();

        final String mc = MCPathHelper.getOS().getMc();
        Path mcPath = Paths.get(mc, new String[0]);
        final String gameVersion = Constants.getGameVersion();
        final String loaderVersion = Constants.getLoaderVersion();

        new Thread(() -> {
            try{

                if (!Files.exists(mcPath, new java.nio.file.LinkOption[0])) {
                    throw new RuntimeException(OutputHelper.getOutputMessage("progress.exception.no.launcher.directory"));
                }

                // Install mod loader
                FabricInstaller.forceInstall(mcPath, gameVersion, loaderVersion);

                // Install modpack
                ModPackInstaller.forceInstall(gameVersion);

                // Install client
                ClientInstaller.forceInstall(gameVersion);

                // Download libraries
                LibrariesDownloader.forceDownload(gameVersion);

                // Download Asset Index
                AssetIndexDownloader.forceDownload(gameVersion);

                // Download natives
                NativesDownloader.forceDownload(gameVersion);

                FeedforwardHandler.setInstallIndex("");
                FeedforwardHandler.repairFinished();
                isRunning = false;

            }catch (Exception e){
                System.err.println("Fail to download the client. Shutting down!");
                LAUNCHER.die(e);
            }
        }).start();
    }

    public static boolean canRepair(){
        return !isRunning;
    }


}

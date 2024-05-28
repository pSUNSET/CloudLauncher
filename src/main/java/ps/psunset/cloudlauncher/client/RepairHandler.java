package ps.psunset.cloudlauncher.client;

import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.client.helper.*;
import ps.psunset.cloudlauncher.js.FeedforwardHandler;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.MCPathHelper;
import ps.psunset.cloudlauncher.util.OutputHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RepairHandler {

    private static final Launcher launcher = Launcher.getInstance();

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
        FeedforwardHandler.repairStart();

        final String mc = MCPathHelper.getOS().getMc();
        Path mcPath = Paths.get(mc, new String[0]);
        final String gameVersion = Constants.getGameVersion();
        final String loaderVersion = Constants.getLoaderVersion();

        (new Thread(() -> {
            try{
                if (!Files.exists(mcPath, new java.nio.file.LinkOption[0])) {
                    throw new RuntimeException(OutputHelper.getMessage("progress.exception.no.launcher.directory"));
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

            }catch (Exception e){
                System.err.println("Failure to download the client. Shutting down!");
                launcher.die(e);
            }
        })).start();
    }

    public static boolean canRepair(){
        return !isRunning;
    }
}
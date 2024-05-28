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

public class InstallHandler {

    private static final Launcher launcher = Launcher.getInstance();

    public static boolean isRunning = false;

    /**
     * Install game.
     */
    public static void install(){
        // Avoid reinstalling
        if (!canInstall() || RepairHandler.isRunning){
            return;
        }

        isRunning = true;
        FeedforwardHandler.installStart();

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
                FabricInstaller.install(mcPath, gameVersion, loaderVersion);

                // Install modpack
                ModPackInstaller.install(gameVersion);

                // Install client
                ClientInstaller.install(gameVersion);

                // Download libraries
                LibrariesDownloader.download(gameVersion);

                // Download Asset Index
                AssetIndexDownloader.download(gameVersion);

                // Download natives
                NativesDownloader.download(gameVersion);

                FeedforwardHandler.setInstallIndex("");
                FeedforwardHandler.installFinished();

            }catch (Exception e){
                System.err.println("Failure to download the client. Shutting down!");
                launcher.die(e);
            }
        })).start();
    }

    public static boolean canInstall(){
        return !isRunning;
    }
}

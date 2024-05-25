package ps.psunset.cloudlauncher.client;

import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.client.helper.*;
import ps.psunset.cloudlauncher.js.Javascript;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.OSHelper;
import ps.psunset.cloudlauncher.util.OutputHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InstallHandler {

    private static final Launcher launcher = Launcher.getInstance();

    private static boolean isRunning = false;
    private static int currentIndex = 0;

    /**
     * Install start.
     */
    public static void install(){
        // Avoid reinstalling
        if (!canInstall()){
            return;
        }

        isRunning = true;
        Javascript.installStart();

        final String mc = OSHelper.getOS().getMc();
        Path mcPath = Paths.get(mc, new String[0]);
        final String gameVersion = Constants.getGameVersion();
        final String loaderVersion = Constants.getLoaderVersion();

        (new Thread(() -> {
            try{
                if (!Files.exists(mcPath, new java.nio.file.LinkOption[0])) {
                    throw new RuntimeException(OutputHelper.getMessage("progress.exception.no.launcher.directory"));
                }

                // Install fabric loader
                String profileName = FabricInstaller.install(mcPath, gameVersion, loaderVersion, launcher);

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

            }catch (Exception e){
                System.err.println("Failure to download the client. Shutting down!");
                launcher.die(e);
            }
        })).start();

        // Download Fabric Library
        (new Thread(() -> {
            try {

            } catch (Exception e) {
                System.err.println("Failure to download Fabric. Shutting down!");
                launcher.die(e);
            }
        })).start();
    }

    public static void progressPlus(){
        currentIndex++;

        if (currentIndex >= 6){ // totalIndex
            Javascript.installFinished();
        }
    }

    public static boolean canInstall(){
        return !isRunning;
    }
}

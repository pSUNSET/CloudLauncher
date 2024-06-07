package ps.psunset.cloudlauncher.client;

import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.client.helper.*;
import ps.psunset.cloudlauncher.js.FeedforwardHandler;
import ps.psunset.cloudlauncher.util.ConfigHelper;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.path.MCPathHelper;
import ps.psunset.cloudlauncher.util.BundleHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class InstallHandler {

    private static final Launcher LAUNCHER = Launcher.getInstance();

    public static boolean isRunning = false;

    /**
     * Install game.
     */
    public static void install(){

        // Avoid reinstalling
        if (!canInstall() || RepairHandler.isRunning){
            return;
        }

        // Check is launch in different game version
        if (!ConfigHelper.getConfig("lastGameVersion").isEmpty() && ConfigHelper.getConfig("lastGameVersion") != null) {
            if (!ConfigHelper.getConfig("lastGameVersion").equals(Constants.getGameVersion())){
                System.out.println("Chosen version: " + Constants.getGameVersion());
                System.out.println("Last used version: " + ConfigHelper.getConfig("lastGameVersion"));
                installDifferentVersion();
                return;
            }
        }

        isRunning = true;
        System.out.println("Installing start");
        FeedforwardHandler.installStart();

        final String mc = MCPathHelper.getOS().getMc();
        Path mcPath = Paths.get(mc, new String[0]);
        final String gameVersion = Constants.getGameVersion();
        final String loaderVersion = Constants.getLoaderVersion();

        (new Thread(() -> {
            try{

                if (!Files.exists(mcPath, new java.nio.file.LinkOption[0])) {
                    throw new RuntimeException(BundleHelper.getOutputMessage("progress.exception.no.launcher.directory"));
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
                LAUNCHER.die(e);
            }
        })).start();
    }

    public static void installDifferentVersion(){

        isRunning = true;
        System.out.println("installing start(in diff ver)");
        FeedforwardHandler.installStart();

        final String mc = MCPathHelper.getOS().getMc();
        Path mcPath = Paths.get(mc, new String[0]);
        final String gameVersion = Constants.getGameVersion();
        final String loaderVersion = Constants.getLoaderVersion();

        (new Thread(() -> {
            try{

                if (!Files.exists(mcPath, new java.nio.file.LinkOption[0])) {
                    throw new RuntimeException(BundleHelper.getOutputMessage("progress.exception.no.launcher.directory"));
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
                FeedforwardHandler.installFinished();

            }catch (Exception e){
                System.err.println("Failure to download the client. Shutting down!");
                LAUNCHER.die(e);
            }
        })).start();
    }

    public static boolean canInstall(){
        return !isRunning;
    }
}

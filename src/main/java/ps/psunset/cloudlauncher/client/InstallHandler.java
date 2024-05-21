package ps.psunset.cloudlauncher.client;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.client.helper.DirectoryGenerator;
import ps.psunset.cloudlauncher.client.helper.FabricInstaller;
import ps.psunset.cloudlauncher.client.helper.ModPackInstaller;
import ps.psunset.cloudlauncher.client.helper.ProfileInstaller;
import ps.psunset.cloudlauncher.js.Javascript;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.OSHelper;
import ps.psunset.cloudlauncher.util.OutputHelper;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

        try{

            // Generate directory
            DirectoryGenerator.generate();

            // Install modpack
            ModPackInstaller.install(gameVersion);

        }catch (Exception e){
            System.err.println("Failure to download the client. Shutting down!");
            launcher.die(e);
        }

        // Download loader library
        if (Constants.isOldVersion()){
            // Download Forge Library

        } else {
            // Download Fabric Library

            (new Thread(() -> {
                try {
                    if (!Files.exists(mcPath, new java.nio.file.LinkOption[0]))
                        throw new RuntimeException(OutputHelper.getMessage("progress.exception.no.launcher.directory"));
                    ProfileInstaller profileInstaller = new ProfileInstaller(mcPath);
                    ProfileInstaller.LauncherType launcherType = null;
                    //if (this.createProfile.isSelected()) {
                    List<ProfileInstaller.LauncherType> types = profileInstaller.getInstalledLauncherTypes();
                    if (types.size() == 0)
                        throw new RuntimeException(OutputHelper.getMessage("progress.exception.no.launcher.profile"));
                    if (types.size() == 1) {
                        launcherType = types.get(0);
                    } else {
                        launcherType = ProfileInstaller.showLauncherTypeSelection();
                        if (launcherType == null) {
                            // Ready to Install
                            return;
                        }
                    }

                    String profileName = FabricInstaller.install(mcPath, gameVersion, loaderVersion, launcher);
                    //if (this.createProfile.isSelected()) {
                    if (launcherType == null)
                        throw new RuntimeException(OutputHelper.getMessage("progress.exception.no.launcher.profile"));
                    profileInstaller.setupProfile(profileName, gameVersion, launcherType, launcher);

                    SwingUtilities.invokeLater(() -> {});
                } catch (Exception e) {
                    System.err.println("Failure to download Fabric. Shutting down!");
                    launcher.die(e);
                }
            })).start();
        }

    }

    public static void progressPlus(){
        currentIndex++;

        if (currentIndex >= 4){ // totalIndex
            Javascript.installFinished();
        }
    }

    public static boolean canInstall(){
        return !isRunning;
    }
}

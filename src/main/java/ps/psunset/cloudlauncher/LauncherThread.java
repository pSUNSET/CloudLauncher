package ps.psunset.cloudlauncher;

import ps.psunset.cloudlauncher.client.ClientInstaller;
import ps.psunset.cloudlauncher.client.FabricInstaller;
import ps.psunset.cloudlauncher.client.ModPackInstaller;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.client.ProfileInstaller;
import ps.psunset.cloudlauncher.util.OSHelper;
import ps.psunset.cloudlauncher.util.OutputHelper;
import ps.psunset.cloudlauncher.util.Reference;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TimeZone;

public class LauncherThread extends Thread{

    private final Launcher launcher;

    public LauncherThread(Launcher launcher){
        this.launcher = launcher;
        Reference.ISO_8601.setTimeZone(TimeZone.getTimeZone("UTC"));

        final File mcDir = new File(OSHelper.getOS().getMc());
        if (!mcDir.exists()){
            mcDir.mkdirs();
        }
    }


    /**
     * Installer start.
     */
    @Override
    public void run() {
        final String mc = OSHelper.getOS().getMc();
        final String gameVersion = Constants.getGameVersion();
        final String loaderVersion = Constants.getLoaderVersion();

        try{
            ClientInstaller.install(gameVersion, launcher);
            ModPackInstaller.install(gameVersion, launcher);
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
                    Path mcPath = Paths.get(mc, new String[0]);
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

}

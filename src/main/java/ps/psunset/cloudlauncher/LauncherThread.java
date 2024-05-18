package ps.psunset.cloudlauncher;

import ps.psunset.cloudlauncher.client.ClientInstaller;
import ps.psunset.cloudlauncher.client.ModPackInstaller;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.client.ProfileInstaller;
import ps.psunset.cloudlauncher.util.OSHelper;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.TimeZone;

import static ps.psunset.cloudlauncher.util.Constants.*;

public class LauncherThread extends Thread{

    private final Launcher launcher;

    public LauncherThread(Launcher launcher){
        this.launcher = launcher;
        Constants.ISO_8601.setTimeZone(TimeZone.getTimeZone("UTC"));

        final File mcDir = new File(OSHelper.getOS().getMc());
        if (!mcDir.exists()){
            mcDir.mkdirs();
        }
    }

    @Override
    public void run() {
        final String mc = OSHelper.getOS().getMc();

        try{
            ModPackInstaller.install(launcher);
        }catch (Exception e){
            System.err.println("Failure to download the client. Shutting down!");
            launcher.die(e);
        }

        if (Objects.equals(Launcher.GAME_VERSION, "1.8.9")){
            // Forge
        } else {
            (new Thread(() -> {
                try {
                    Path mcPath = Paths.get(OSHelper.getOS().getMc(), new String[0]);
                    if (!Files.exists(mcPath, new java.nio.file.LinkOption[0]))
                        throw new RuntimeException("Can't find the directory.");
                    ProfileInstaller profileInstaller = new ProfileInstaller(mcPath);
                    String profileName = ClientInstaller.install(mcPath, getGameVersion(), getLoadVersion());
                    profileInstaller.setupProfile(profileName, getGameVersion(), ProfileInstaller.LauncherType.WIN32);
                    SwingUtilities.invokeLater(() -> {});
                    launcher.progressPlus();
                } catch (Exception e) {
                    System.err.println("Failure to download Fabric. Shutting down!");
                    launcher.die(e);
                }
            })).start();
        }
    }

}

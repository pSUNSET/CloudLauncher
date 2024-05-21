package ps.psunset.cloudlauncher;

import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.client.LaunchHandler;
import ps.psunset.cloudlauncher.util.OSHelper;
import ps.psunset.cloudlauncher.util.Reference;

import java.io.File;
import java.util.TimeZone;

public class LauncherThread extends Thread{

    private final Launcher launcher;

    public LauncherThread(Launcher launcher){
        this.launcher = launcher;
        Reference.ISO_8601.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Run after helper gui start.
     * Launcher.registerWorkers()
     * @see Launcher
     */
    @Override
    public void run() {

    }
}

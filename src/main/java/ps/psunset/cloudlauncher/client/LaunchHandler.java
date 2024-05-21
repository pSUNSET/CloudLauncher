package ps.psunset.cloudlauncher.client;

import ps.psunset.cloudlauncher.Launcher;

public class LaunchHandler {

    private static final Launcher launcher = Launcher.getInstance();

    public static boolean isRunning = false;

    public static void launch(){
        // Avoid relaunching
        if(!canLaunch()){
            return;
        }

        isRunning = true;
        System.out.println("Client is launching");

        // Launching
    }

    public static boolean canLaunch(){
        return !isRunning;
    }
}

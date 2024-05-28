package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.js.FeedforwardHandler;
import ps.psunset.cloudlauncher.util.*;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

public class ClientInstaller {

    /**
     * Install Client (jar file)
     */
    public static void install(String gameVersion) throws Exception{
        System.out.println("Installing Cloud Client");
        FeedforwardHandler.setInstallIndex(OutputHelper.getMessage("progress.installing.client", new Object[]{ Constants.getLauncherTitle() }));

        File zipDir = new File(MCPathHelper.getOS().getVersionDir() + Constants.getLauncherNameVersion() + ".jar");

        switch (gameVersion){
            // 1.20.6
            case "1.20.6":
                FileUtils.copyURLToFile(new URL(Reference.FABRIC_1_20_6_CLIENT_URL), zipDir);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }
    }

    /**
     * Forcibly install Client (jar file)
     */
    public static void forceInstall(String gameVersion) throws Exception{
        File zipDir = new File(MCPathHelper.getOS().getVersionDir() + Constants.getLauncherNameVersion() + ".jar");
        Files.deleteIfExists(zipDir.toPath());

        install(gameVersion);
    }
}

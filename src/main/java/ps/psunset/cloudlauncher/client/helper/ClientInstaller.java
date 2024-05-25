package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.util.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class ClientInstaller {

    /**
     * Install Client (jar file)
     */
    public static void install(String gameVersion) throws IOException {
        System.out.println(OutputHelper.getMessage("progress.installing.client", new Object[]{Constants.getLauncherTitle()}));

        File zipDir = new File(OSHelper.getOS().getVersionDir() + Constants.getLauncherNameVersion() + ".jar");

        switch (gameVersion){
            case "1.20.6":
                FileUtils.copyURLToFile(new URL(Reference.FABRIC_1_20_6_CLIENT_URL), zipDir);
                break;

            case "1.8.9":

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }


        System.out.println(OutputHelper.getMessage("progress.finished.client"));
        InstallHandler.progressPlus();
    }
}

package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.util.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

/**
 * Download libraries
 */
public class LibrariesDownloader {
    public static void download(String gameVersion) throws IOException {
        System.out.println(OutputHelper.getMessage("progress.installing.libraries"));

        File librariesDir = new File(OSHelper.getOS().getLibrariesDir());
        File zipDir = new File(OSHelper.getOS().getClientDir() + "libraries-" + Constants.getGameVersion() + ".zip");

        switch (gameVersion){
            case "1.20.6":
                if (!zipDir.exists()){
                    FileUtils.copyURLToFile(new URL(Reference.LIBRARIES_1_20_6_URL), zipDir);
                }
                break;

            case "1.8.9":

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }

        FileHelper.unzip(zipDir.toString(), librariesDir.toString());

        InstallHandler.progressPlus();
    }
}

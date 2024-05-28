package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.js.FeedforwardHandler;
import ps.psunset.cloudlauncher.util.*;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

/**
 * Download libraries
 */
public class LibrariesDownloader {
    static File librariesDir = new File(MCPathHelper.getOS().getLibrariesDir());
    static File zipDir = new File(MCPathHelper.getOS().getClientDir() + "libraries-" + Constants.getGameVersion() + ".zip");

    /**
     * Download libraries
     * @param gameVersion
     * @throws Exception
     */
    public static void download(String gameVersion) throws Exception{
        System.out.println("Downloading libraries");
        FeedforwardHandler.setInstallIndex(OutputHelper.getMessage("progress.installing.libraries"));

        switch (gameVersion){

            // 1.20.6
            case "1.20.6":
                if (!zipDir.exists()){
                    FileUtils.copyURLToFile(new URL(Reference.LIBRARIES_1_20_6_URL), zipDir);
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }

        FileHelper.unzip(zipDir.toString(), librariesDir.toString());
    }

    /**
     * Forcibly download libraries
     * @param gameVersion
     * @throws Exception
     */
    public static void forceDownload(String gameVersion) throws Exception{
        File librariesDir = new File(MCPathHelper.getOS().getLibrariesDir());
        File zipDir = new File(MCPathHelper.getOS().getClientDir() + "libraries-" + Constants.getGameVersion() + ".zip");

        Files.deleteIfExists(librariesDir.toPath());
        Files.deleteIfExists(zipDir.toPath());

        download(gameVersion);
    }
}

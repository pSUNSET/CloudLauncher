package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.js.FeedforwardHandler;
import ps.psunset.cloudlauncher.util.*;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

public class NativesDownloader {

    static File zipDir = new File(MCPathHelper.getOS().getClientDir() + "natives-" + Constants.getGameVersion() + ".zip");
    static File nativesDir = new File(MCPathHelper.getOS().getNativesDir());

    /**
     * Download native files
     * @param gameVersion
     * @throws Exception
     */
    public static void download(String gameVersion) throws Exception {
        System.out.println("Downloading native files");
        FeedforwardHandler.setInstallIndex(OutputHelper.getMessage("progress.installing.natives"));

        switch (gameVersion){

            // 1.20.6
            case "1.20.6":
                if (!zipDir.exists()){
                    FileUtils.copyURLToFile(new URL(Reference.NATIVES_1_20_6_URL), zipDir);
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }

        FileHelper.unzip(zipDir.toString(), nativesDir.toString());
    }

    /**
     * Forcibly Download native files
     * @param gameVersion
     * @throws Exception
     */
    public static void forceDownload(String gameVersion) throws Exception {
        Files.deleteIfExists(zipDir.toPath());
        Files.deleteIfExists(nativesDir.toPath());

        download(gameVersion);
    }
}

package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.js.FeedforwardHandler;
import ps.psunset.cloudlauncher.util.*;
import ps.psunset.cloudlauncher.util.path.MCPathHelper;

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
        FeedforwardHandler.setInstallIndex(BundleHelper.getOutputMessage("progress.installing.natives"));

        switch (gameVersion){

            // 1.20.6
            case "1.20.6":
                if (zipDir.createNewFile()){
                    FileUtils.copyURLToFile(new URL(Reference.NATIVES_1_20_6_URL), zipDir);
                    FileHelper.unzip(zipDir.toString(), nativesDir.toString());
                } else {
                    System.out.println("|--no need to update");
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }

    }

    /**
     * Forcibly Download native files
     * @param gameVersion
     * @throws Exception
     */
    public static void forceDownload(String gameVersion) throws Exception {
        if (nativesDir.listFiles() != null) {
            for (File file: nativesDir.listFiles()) {
                FileUtils.delete(file);
            }
        }

        Files.deleteIfExists(zipDir.toPath());
        Files.deleteIfExists(nativesDir.toPath());

        download(gameVersion);
    }
}

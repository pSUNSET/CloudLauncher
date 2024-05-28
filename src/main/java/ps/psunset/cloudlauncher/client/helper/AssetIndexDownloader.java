package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.js.FeedforwardHandler;
import ps.psunset.cloudlauncher.util.MCPathHelper;
import ps.psunset.cloudlauncher.util.OutputHelper;
import ps.psunset.cloudlauncher.util.Reference;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

public class AssetIndexDownloader {
    static File json_1_20_6_Dir = new File(MCPathHelper.getOS().getAssetsDir() + "indexes/1.20.6-16.json");

    /**
     * Download asset index (.json file)
     */
    public static void download(String gameVersion) throws Exception{
        System.out.println("Downloading asset index file");
        FeedforwardHandler.setInstallIndex(OutputHelper.getMessage("progress.installing.assetindex"));

        switch (gameVersion){

            // 1.20.6
            case "1.20.6":
                FileUtils.copyURLToFile(new URL(Reference.ASSETS_1_20_6_JSON_URL), json_1_20_6_Dir);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }
    }

    /**
     * Forcibly download asset index (.json file)
     */
    public static void forceDownload(String gameVersion) throws Exception{

        switch (gameVersion){

            case "1.20.6":
                Files.deleteIfExists(json_1_20_6_Dir.toPath());
                break;
        }

        download(gameVersion);
    }
}

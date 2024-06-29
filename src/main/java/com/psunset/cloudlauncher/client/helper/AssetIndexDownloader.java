package com.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import com.psunset.cloudlauncher.js.FeedforwardHandler;
import com.psunset.cloudlauncher.util.path.MCPathHelper;
import com.psunset.cloudlauncher.util.bundle.OutputHelper;
import com.psunset.cloudlauncher.util.Reference;

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
        FeedforwardHandler.setInstallIndex(OutputHelper.getOutputMessage("progress.installing.assetindex"));

        switch (gameVersion){

            // 1.20.6
            case "1.20.6":
                if (json_1_20_6_Dir.createNewFile()){
                    FileUtils.copyURLToFile(new URL(Reference.ASSETS_1_20_6_JSON_URL), json_1_20_6_Dir);
                } else {
                    System.out.println("|-- no need to update");
                }
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

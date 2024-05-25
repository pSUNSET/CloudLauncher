package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.OSHelper;
import ps.psunset.cloudlauncher.util.OutputHelper;
import ps.psunset.cloudlauncher.util.Reference;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AssetIndexDownloader {

    /**
     * Download asset index (.json file)
     */
    public static void download(String gameVersion) throws IOException {


        switch (gameVersion){
            case "1.20.6":
                File jsonDir = new File(OSHelper.getOS().getAssetsDir() + "indexes/1.20.6-16.json");
                FileUtils.copyURLToFile(new URL(Reference.ASSETS_1_20_6_JSON_URL), jsonDir);
                break;

            case "1.8.9":

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }

        InstallHandler.progressPlus();
    }
}

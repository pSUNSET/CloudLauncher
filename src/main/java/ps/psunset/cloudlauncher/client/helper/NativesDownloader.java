package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.util.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class NativesDownloader {
    public static void download(String gameVersion) throws IOException {
        File zipDir = new File(OSHelper.getOS().getClientDir() + "natives-" + Constants.getGameVersion() + ".zip");
        File nativesDir = new File(OSHelper.getOS().getNativesDir());

        switch (gameVersion){
            case "1.20.6":
                if (!zipDir.exists()){
                    FileUtils.copyURLToFile(new URL(Reference.NATIVES_1_20_6_URL), zipDir);
                }
                break;

            case "1.8.9":

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }

        FileHelper.unzip(zipDir.toString(), nativesDir.toString());


        System.out.println(OutputHelper.getMessage("progress.finished.libraries"));
        InstallHandler.progressPlus();
    }
}

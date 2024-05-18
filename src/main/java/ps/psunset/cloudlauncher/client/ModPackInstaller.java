package ps.psunset.cloudlauncher.client;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.OSHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModPackInstaller {

    public static void install(Launcher launcher) throws IOException {
        System.out.println("Generating directory and installing " + Launcher.TITLE);
        String modsDir = OSHelper.getOS().getModsDir();
        FileUtils.cleanDirectory(new File(modsDir));
        for (URL mod : Constants.getJars()) {
            FileUtils.copyURLToFile(mod, new File(modsDir + Constants.getFileName()));
        }
        System.out.println(Launcher.TITLE + " installing finished");
        launcher.progressPlus();
    }
}

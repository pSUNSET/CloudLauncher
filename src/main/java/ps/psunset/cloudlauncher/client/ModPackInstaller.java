package ps.psunset.cloudlauncher.client;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.util.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ModPackInstaller {

    public static void install(Launcher launcher) throws IOException {
        System.out.println("Generating directory and installing " + Launcher.TITLE);
        for (URL mod : Constants.getJars()) {
            FileUtils.copyURLToFile(mod, new File(Constants.getClientPath() + "/mods/" + Constants.getFileName()));
        }
        System.out.println(Launcher.TITLE + " installing finished");
        launcher.progressPlus();
    }
}

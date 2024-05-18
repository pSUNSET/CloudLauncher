package ps.psunset.cloudlauncher.client;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.util.OSHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

public class ClientInstaller {

    public static void install(String gameVersion, Launcher launcher) throws IOException {

        generateDirectory(gameVersion, launcher);

        System.out.println("Installing " + Launcher.TITLE);

        // Client Installing

        System.out.println(Launcher.TITLE + " installing finished");
        launcher.progressPlus();
    }

    public static void generateDirectory(String gameVersion, Launcher launcher) throws IOException {
        System.out.println("Generating directory.");

        File cliDir = new File(OSHelper.getOS().getClientDir());
        if (!cliDir.exists()){
            Files.createDirectory(cliDir.toPath(), new FileAttribute[0]);
        }

        File modsDir = new File(OSHelper.getOS().getModsDir());
        if (modsDir.exists()){
            FileUtils.cleanDirectory(modsDir);
        }else {
            Files.createDirectory(modsDir.toPath(), new FileAttribute[0]);
        }

        launcher.progressPlus();
        System.out.println("Generating directory done.");
    }
}

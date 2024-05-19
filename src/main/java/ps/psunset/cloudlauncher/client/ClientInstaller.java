package ps.psunset.cloudlauncher.client;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.util.OSHelper;
import ps.psunset.cloudlauncher.util.OutputHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

public class ClientInstaller {

    /**
     * Install cloud client option
     */
    public static void install(String gameVersion, Launcher launcher) throws IOException {

        generateDirectory(gameVersion, launcher);

        System.out.println(OutputHelper.getMessage("progress.installing.client", new Object[]{ Launcher.TITLE }));

        // Client Installing

        System.out.println(OutputHelper.getMessage("progress.finished.client"));
        launcher.progressPlus();
    }

    /**
     * Generate client directory
     */
    public static void generateDirectory(String gameVersion, Launcher launcher) throws IOException {
        System.out.println(OutputHelper.getMessage("progress.generating.directory"));

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
        System.out.println(OutputHelper.getMessage("progress.finished.directory"));
    }
}

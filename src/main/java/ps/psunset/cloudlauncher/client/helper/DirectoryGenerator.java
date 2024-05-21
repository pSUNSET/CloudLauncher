package ps.psunset.cloudlauncher.client.helper;

import org.apache.commons.io.FileUtils;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.util.OSHelper;
import ps.psunset.cloudlauncher.util.OutputHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

public class DirectoryGenerator {
    /**
     * Generate client directory
     */
    public static void generate() throws IOException {
        System.out.println(OutputHelper.getMessage("progress.generating.directory"));

        File mcDir = new File(OSHelper.getOS().getMc());
        if (!mcDir.exists()){
            new Exception(OutputHelper.getMessage("progress.exception.no.mc.directory"));
        }

        File cliDir = new File(OSHelper.getOS().getClientDir());
        if (!cliDir.exists()){
            Files.createDirectory(cliDir.toPath(), new FileAttribute[0]);
        }

        File modsDir = new File(OSHelper.getOS().getModsDir());
        if (!modsDir.exists()){
            Files.createDirectory(modsDir.toPath(), new FileAttribute[0]);
        }

        System.out.println(OutputHelper.getMessage("progress.finished.directory"));
        InstallHandler.progressPlus();
    }
}

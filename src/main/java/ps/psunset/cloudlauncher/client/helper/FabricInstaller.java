package ps.psunset.cloudlauncher.client.helper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

import mjson.Json;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.FabricService;
import ps.psunset.cloudlauncher.util.Library;
import ps.psunset.cloudlauncher.util.OutputHelper;

public class FabricInstaller {

    /**
     * Install fabric loader
     *
     * @param mcDir
     * @param gameVersion
     * @param loaderVersion
     * @param launcher
     * @return Profile Name (default: Launcher.NAME_VERSION)
     * @throws IOException
     */
    public static String install(Path mcDir, String gameVersion, String loaderVersion, Launcher launcher) throws IOException {
        System.out.println(OutputHelper.getMessage("progress.installing.fabric", new Object[]{loaderVersion}));
        String profileName = Constants.getLauncherNameVersion();
        Path versionsDir = mcDir.resolve("versions");
        Path profileDir = versionsDir.resolve(profileName);
        Path profileJson = profileDir.resolve(profileName + ".json");
        if (!Files.exists(profileDir, new java.nio.file.LinkOption[0])) {
            Files.createDirectories(profileDir, (FileAttribute<?>[]) new FileAttribute[0]);
        }
        Path profileJar = profileDir.resolve(profileName + ".jar");
        Files.deleteIfExists(profileJar);
        Files.createFile(profileJar).resolve(profileJar);
        Json json = FabricService.queryMetaJson(String.format("v2/versions/loader/%s/%s/profile/json", new Object[] { gameVersion, loaderVersion}));
        json.set("id", Constants.getLauncherNameVersion());
        Files.write(profileJson, json.toString().getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
        Path libsDir = mcDir.resolve("libraries");
        for (Json libraryJson : json.at("libraries").asJsonList()) {
            Library library = new Library(libraryJson);
            Path libraryFile = libsDir.resolve(library.getPath());
            String url = library.getURL();
            FabricService.downloadSubstitutedMaven(url, libraryFile);
        }

        System.out.println(OutputHelper.getMessage("progress.finished.fabric", new Object[]{loaderVersion}));
        InstallHandler.progressPlus();
        return profileName;
    }
}
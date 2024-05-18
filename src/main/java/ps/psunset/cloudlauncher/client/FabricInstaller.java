package ps.psunset.cloudlauncher.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

import mjson.Json;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.util.FabricService;
import ps.psunset.cloudlauncher.util.Library;

public class FabricInstaller {
    public static String install(Path mcDir, String gameVersion, String loaderVersion, Launcher launcher) throws IOException {
        System.out.println("Installing " + gameVersion + " with fabric " + loaderVersion);
        String profileName = Launcher.NAME_VERSION;
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
        json.set("id", Launcher.NAME_VERSION);
        Files.write(profileJson, json.toString().getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
        Path libsDir = mcDir.resolve("libraries");
        for (Json libraryJson : json.at("libraries").asJsonList()) {
            Library library = new Library(libraryJson);
            Path libraryFile = libsDir.resolve(library.getPath());
            String url = library.getURL();
            FabricService.downloadSubstitutedMaven(url, libraryFile);
        }

        launcher.progressPlus();
        System.out.println("Installing " + gameVersion + " with fabric " + loaderVersion + " finished");
        return profileName;
    }
}
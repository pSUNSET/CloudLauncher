package ps.psunset.cloudlauncher.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import mjson.Json;
import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.FileHelper;
import ps.psunset.cloudlauncher.util.OSHelper;

public class ProfileInstaller {
    private final Path mcDir;

    public ProfileInstaller(Path mcDir) {
        this.mcDir = mcDir;
    }

    public List<LauncherType> getInstalledLauncherTypes() {
        return (List<LauncherType>)Arrays.<LauncherType>stream(LauncherType.values())
                .filter(launcherType -> Files.exists(this.mcDir.resolve(launcherType.profileJsonName), new java.nio.file.LinkOption[0]))
                .collect(Collectors.toList());
    }

    public void setupProfile(String name, String gameVersion, LauncherType launcherType) throws IOException {
        Path launcherProfiles = this.mcDir.resolve(launcherType.profileJsonName);
        if (!Files.exists(launcherProfiles, new java.nio.file.LinkOption[0]))
            throw new FileNotFoundException("Could not find " + launcherType.profileJsonName);
        System.out.println("Creating profile");
        Json jsonObject = Json.read(FileHelper.readString(launcherProfiles));
        Json profiles = jsonObject.at("profiles");
        if (profiles == null) {
            profiles = Json.object();
            jsonObject.set("profiles", profiles);
        }
        String profileName = Launcher.NAME_VERSION;
        Json profile = profiles.at(profileName);
        if (profile == null) {
            profile = createProfile(profileName);
            profiles.set(profileName, profile);
        }
        profile.set("lastVersionId", name);
        FileHelper.writeToFile(launcherProfiles, jsonObject.toString());
        Path modsDir = this.mcDir.resolve("mods");
        if (Files.notExists(modsDir, new java.nio.file.LinkOption[0]))
            Files.createDirectories(modsDir, (FileAttribute<?>[])new FileAttribute[0]);
        System.out.println("Profile generation finished");
    }

    private static Json createProfile(String name) throws IOException {
        if (Files.notExists(Path.of(OSHelper.getOS().getClientDir()))){
            Files.createDirectory(Path.of(OSHelper.getOS().getMc()), new FileAttribute<Object>() {
                @Override
                public String name() {
                    return Launcher.NAME.toLowerCase();
                }

                @Override
                public Object value() {
                    return null;
                }
            });
        }
        Json jsonObject = Json.object();
        jsonObject.set("name", name);
        jsonObject.set("gameDir", Path.of(OSHelper.getOS().getClientDir()));
        jsonObject.set("type", "release");
        jsonObject.set("created", Constants.ISO_8601.format(new Date()));
        jsonObject.set("lastUsed", Constants.ISO_8601.format(new Date()));
        jsonObject.set("icon", Constants.MC_LAUNCHER_ICON);
        return jsonObject;
    }

    public enum LauncherType {
        WIN32("launcher_profiles.json"),
        MICROSOFT_STORE("launcher_profiles_microsoft_store.json");

        public final String profileJsonName;

        LauncherType(String profileJsonName) {
            this.profileJsonName = profileJsonName;
        }
    }
}
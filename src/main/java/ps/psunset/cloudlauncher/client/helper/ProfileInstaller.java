package ps.psunset.cloudlauncher.client.helper;

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
import ps.psunset.cloudlauncher.client.InstallHandler;
import ps.psunset.cloudlauncher.util.*;
import ps.psunset.cloudlauncher.util.OutputHelper;

import javax.swing.*;

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

    /**
     * Generate launcher_profile.json in Minecraft directory
     */
    public void setupProfile(String name, String gameVersion, LauncherType launcherType, Launcher launcher) throws IOException {
        Path launcherProfiles = this.mcDir.resolve(launcherType.profileJsonName);
        if (!Files.exists(launcherProfiles, new java.nio.file.LinkOption[0]))
            throw new FileNotFoundException("Could not find " + launcherType.profileJsonName);
        System.out.println(OutputHelper.getMessage("progress.generating.profile"));
        Json jsonObject = Json.read(FileHelper.readString(launcherProfiles));
        Json profiles = jsonObject.at("profiles");
        if (profiles == null) {
            profiles = Json.object();
            jsonObject.set("profiles", profiles);
        }
        String profileName = Constants.getLauncherNameVersion();
        Json profile = profiles.at(profileName);
        if (profile == null) {
            profile = createProfile(profileName);
            profiles.set(profileName, profile);
        }
        profile.set("lastVersionId", name);
        FileHelper.writeToFile(launcherProfiles, jsonObject.toString());
        Path modsDir = this.mcDir.resolve("mod");
        if (Files.notExists(modsDir, new java.nio.file.LinkOption[0]))
            Files.createDirectories(modsDir, (FileAttribute<?>[])new FileAttribute[0]);

        System.out.println(OutputHelper.getMessage("progress.finished.profile"));
        InstallHandler.progressPlus();
    }

    private static Json createProfile(String name) throws IOException {
        if (Files.notExists(Path.of(OSHelper.getOS().getClientDir()))){
            Files.createDirectory(Path.of(OSHelper.getOS().getMc()), new FileAttribute<Object>() {
                @Override
                public String name() {
                    return Constants.getLauncherName().toLowerCase();
                }

                @Override
                public Object value() {
                    return null;
                }
            });
        }
        Json jsonObject = Json.object();
        jsonObject.set("name", name);
        jsonObject.set("gameDir", OSHelper.getOS().getClientDir());
        jsonObject.set("type", "release");
        jsonObject.set("created", Reference.ISO_8601.format(new Date()));
        jsonObject.set("lastUsed", Reference.ISO_8601.format(new Date()));
        jsonObject.set("icon", Reference.MC_LAUNCHER_ICON);
        return jsonObject;
    }

    public static ProfileInstaller.LauncherType showLauncherTypeSelection() {
        Object[] options = { OutputHelper.getMessage("prompt.launcher.type.xbox"),
                OutputHelper.getMessage("prompt.launcher.type.win32") };
        int result = JOptionPane.showOptionDialog(null, OutputHelper.getMessage("prompt.launcher.type.body"),
                Constants.getLauncherTitle(), 1, 3, null, options, options[0]);
        if (result == -1)
            return null;
        return (result == 0) ? ProfileInstaller.LauncherType.MICROSOFT_STORE : ProfileInstaller.LauncherType.WIN32;
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
package ps.psunset.cloudlauncher;

import ps.psunset.cloudlauncher.client.ClientInstaller;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.client.ProfileInstaller;
import ps.psunset.cloudlauncher.util.OSHelper;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TimeZone;

import static ps.psunset.cloudlauncher.util.Constants.*;

public class LauncherThread extends Thread{

    private final Launcher launcher;

    public LauncherThread(Launcher launcher){
        this.launcher = launcher;
        Constants.ISO_8601.setTimeZone(TimeZone.getTimeZone("UTC"));

        final File mcDir = new File(OSHelper.getOS().getMc());
        if (!mcDir.exists()){
            mcDir.mkdirs();
        }
    }

    @Override
    public void run() {
        final String mc = OSHelper.getOS().getMc();

        /*try{
            File userDir = new File(mc, "cloudclient/");
            FileHelper.deleteDirectory(userDir);

            File verDir = new File(mc, "versions/cloudclient-v" + Constants.getVersionNumber());
            FileHelper.deleteDirectory(verDir);

            verDir.mkdirs();

            final String installDate = sdf.format(new Date());

            final JsonObject newProfile = new JsonObject();
            newProfile.addProperty("name", Launcher.TITLE);
            newProfile.addProperty("type", "custom");
            newProfile.addProperty("created", installDate);
            newProfile.addProperty("lastUsed", installDate);
            newProfile.addProperty("icon", Constants.MC_LAUNCHER_ICON);
            newProfile.addProperty("lastVersionId", Launcher.NAME);

            final File launcherProfileFile = new File(mc, "/launcher_profiles.json");
            JsonObject launcherProfile = new JsonObject();
            if (launcherProfileFile.exists()){
                launcherProfile = new JsonParser().parse(FileHelper.readFile(launcherProfileFile)).getAsJsonObject();
            } else {
                launcherProfile.add("profiles", new JsonObject());
            }
            launcherProfile.get("profiles").getAsJsonObject().add(Launcher.NAME, newProfile);
            launcherProfile.addProperty("selectedProfile", Launcher.NAME);

            String jsonToWrite = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(launcherProfile);

            FileHelper.writeLine(jsonToWrite, launcherProfileFile);
            //FileHelper.writeLine(Constants.getJSON(), new File(verDir + "/" + Constants.getFileName() + ".json"));
            //launcher.moveForward();
            FileHelper.writeLine(Constants.getJar(), new File(verDir + "/" + Constants.getFileName() + ".jar"));
            launcher.moveForward();

        }catch (Exception e){
            System.err.println("Failure to download the client. Shutting down!");
            launcher.die(e);
        }*/

        System.out.println("Installing");
        (new Thread(() -> {
            try {
                Path mcPath = Paths.get(OSHelper.getOS().getMc(), new String[0]);
                if (!Files.exists(mcPath, new java.nio.file.LinkOption[0]))
                    throw new RuntimeException("Can't find the directory.");
                ProfileInstaller profileInstaller = new ProfileInstaller(mcPath);
                ProfileInstaller.LauncherType launcherType = null;
                String profileName = ClientInstaller.install(mcPath, getGameVersion(), getLoadVersion());
                SwingUtilities.invokeLater(() -> {});
            } catch (Exception e) {
                e.printStackTrace();
            }
        })).start();
    }

}

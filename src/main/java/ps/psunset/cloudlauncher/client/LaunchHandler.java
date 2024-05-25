package ps.psunset.cloudlauncher.client;

import ps.psunset.cloudlauncher.Launcher;
import ps.psunset.cloudlauncher.util.LaunchOption;
import ps.psunset.cloudlauncher.util.Constants;
import ps.psunset.cloudlauncher.util.OSHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LaunchHandler {

    public static boolean isRunning = false;

    public static void launch() throws Exception {
        // Avoid relaunching
        if(!canLaunch()){
            return;
        }

        isRunning = true;

        // Launching

        // Java Args
        ArrayList<String> commands = LaunchHandler.getLaunchCommand();

        String cmds = "";
        for (String command : commands) {
            cmds += command;
        }
        String[] ary = cmds.split(" ");

        ProcessBuilder processBuilder = new ProcessBuilder(ary);
        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        processBuilder.directory(new File(System.getProperty("user.dir")));
        processBuilder.redirectErrorStream(true);

        System.out.println("Launching: \n" + getCommand(ary));
        try {
            Process process = processBuilder.start();
            process.waitFor();
            int exitVal = process.exitValue();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            if (exitVal != 0) {
                System.out.println("Minecraft has crashed.");
            }
        } catch (IOException e) {
            throw new Exception("Cannot launch !", e);
        }

    }

    public static ArrayList<String> getLaunchCommand(){
        ArrayList<String> args = new ArrayList<>();

        // start
        if (LaunchOption.customJavaPath != "") {
            args.add(LaunchOption.customJavaPath);
        } else {
            args.add(LaunchOption.defaultJavaPath);
        }
        args.add(File.separator + "bin" + File.separator + "java.exe ");

        if (LaunchOption.customJavaParameter != ""){
            args.add(LaunchOption.customJavaParameter + " ");
        } else {
            args.add(LaunchOption.defaultJavaParameter + " ");
        }

        if (LaunchOption.customMaximumRamMb != 0){
            args.add("-Xmx" + LaunchOption.customMaximumRamMb + "M ");
        } else {
            args.add("-Xmx" + LaunchOption.maximumRamMb + "M ");
        }
        args.add("-Xms" + LaunchOption.minimumRamMb + "M ");


        // native
        args.add("-Djava.library.path=\"" + OSHelper.getOS().getMc() + "bin" + File.separator + "cloudclient\" ");

        // classpath
        args.add("-cp \"");
        for (File libraries : Path.of(OSHelper.getOS().getLibrariesDir()).toFile().listFiles()){
            args.add(libraries.getAbsolutePath() + ";");
        }
        args.add(OSHelper.getOS().getVersionDir() + Constants.getLauncherNameVersion() + ".jar\" ");

        // Main class
        args.add("net.fabricmc.loader.impl.launch.knot.KnotClient ");

        // end
        /*args.add(" --auth_player_name " + "DefaultName");
        args.add(" --auth_uuid " + UUID.randomUUID());
        args.add(" --auth_access_token " + "-");
        args.add(" --launcher_name " + Constants.getLauncherName());
        args.add(" --launcher_version " + Constants.getLauncherVersion());
        args.add(" --user_type " + "legacy");
        args.add(" --version_name " + "1.20.6");
        args.add(" --version_type " + "release");
        args.add(" --game_directory " + OSHelper.getOS().getClientDir());
        args.add(" --assets_root " + OSHelper.getOS().getAssetsDir());
        args.add(" --assets_index_name " + "1.20.6-16");
        args.add(" --user_properties " + "{}");
        args.add(" --clientid " + "-");
        args.add(" --auth_xuid " + "-");*/


        args.add(" --username " + "Steve");
        args.add(" --version " + Constants.getLauncherNameVersion());
        args.add(" --gameDir " + OSHelper.getOS().getMc() + "cloudclient");
        args.add(" --assetsDir " + OSHelper.getOS().getMc() + "assets");
        args.add(" --assetIndex " + "1.20.6-16");
        args.add(" --uuid " + UUID.randomUUID());
        args.add(" --accessToken " + "aeef7bc935f9420eb6314dea7ad7e1e5");
        args.add(" --userType " + "mojang");
        args.add(" --versionType " + "release");


        // options

        if (LaunchOption.serverIp != ""){
            args.add(" --server " + LaunchOption.serverIp);
            if (LaunchOption.port != ""){
                args.add(" --port " + LaunchOption.port);
            }
        }

        if (LaunchOption.screenWidth > 0 && LaunchOption.screenHeight > 0){
            args.add(" --width ");
            args.add(String.valueOf(LaunchOption.screenWidth));
            args.add(" --height ");
            args.add(String.valueOf(LaunchOption.screenHeight));
        }

        return args;
    }

    public static boolean canLaunch(){
        return !isRunning;
    }

    /**
     * Hide the access token inside the console
     * @param arguments The Token
     * @return The arguments List<String> with the hidden token
     */
    public static String getCommand(String[] arguments) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            output.append(" ");
            if (i > 0 && Objects.equals(arguments[i-1], "--accessToken")) {
                output.append("????????");
            } else {
                output.append(arguments[i]);
            }
        }
        return output.toString();
    }
}

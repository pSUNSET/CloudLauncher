package com.psunset.cloudlauncher.client;

import com.psunset.cloudlauncher.ClientOutputUI;
import com.psunset.cloudlauncher.js.FeedforwardHandler;
import com.psunset.cloudlauncher.util.database.ConfigHelper;
import com.psunset.cloudlauncher.util.LaunchOption;
import com.psunset.cloudlauncher.util.Constants;
import com.psunset.cloudlauncher.util.path.MCPathHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class LaunchHandler {

    public static boolean isRunning = false;

    /**
     * Launch game
     */
    public static void launch() {
        // Avoid relaunching
        if(!canLaunch()){
            return;
        }

        isRunning = true;

        // Launching Java Args
        ArrayList<String> commands = getLaunchCommand(ConfigHelper.Type.SELECT_VERSION.getValue());

        String cmds = "";
        for (String command : commands) {
            cmds += command;
        }
        String[] ary = cmds.split(" ");
        String aryWithoutToken = hideToken(ary);

        ProcessBuilder processBuilder = new ProcessBuilder(ary);
        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        processBuilder.directory(new File(MCPathHelper.getOS().getClientDir()));
        processBuilder.redirectErrorStream(true);

        // Client Output Frame start
        ClientOutputUI coUI = ClientOutputUI.getInstance();

        new Thread(() -> {
            // Set last used game version to config file
            ConfigHelper.Type.SELECT_VERSION.setValue(ConfigHelper.Type.SELECT_VERSION.getValue());

            System.out.println("Launching: \n" + aryWithoutToken);


            coUI.addMsg(aryWithoutToken);
            coUI.addMsg("");

            try {

                // Client start
                Process process = Runtime.getRuntime().exec(ary);
//                Process process = processBuilder.start();

                // Print input and error in Client Output Frame
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s = null;
                while ((s = stdInput.readLine()) != null) {
                    coUI.addMsg(s);
                }

                BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String e = null;
                while ((e = stdError.readLine()) != null) {
                    coUI.addMsg(e);
                }


                int exitVal = process.waitFor();

                if (exitVal != 0) {
                    coUI.addMsg("Minecraft has crashed.");
                } else {
                    coUI.addMsg("Minecraft has been closed.");
                }

                // Client closed
                clientClosed();

            } catch (IOException | InterruptedException e) {

                FeedforwardHandler.clientClosed();
                InstallHandler.isRunning = false;
                LaunchHandler.isRunning = false;

                try {
                    throw new Exception("Cannot launch !", e);
                } catch (Exception exc) {
                    throw new RuntimeException(exc);
                }
            }
        }).start();
    }

    public static ArrayList<String> getLaunchCommand(String gameVersion){
        ArrayList<String> args = new ArrayList<>();

        switch (gameVersion) {

            // 1.20
            case "1.20.6":

                // args start

                // java.exe
                args.add(LaunchOption.getJavaPath());
                args.add(File.separator + "bin" + File.separator + "java.exe ");

                // parameter
                args.add(LaunchOption.getJavaParameter() + " ");

                // ram
                args.add(LaunchOption.getMaximumRamMb() + " ");
                args.add(LaunchOption.getMinimumRamMb() + " ");


                // native
                args.add("-Djava.library.path=\"" + MCPathHelper.getOS().getMc() + "bin" + File.separator + "cloudclient\" ");

                // classpath
                args.add("-cp \"");
                args.add(Path.of(MCPathHelper.getOS().getLibrariesDir()).toFile().getAbsolutePath() + File.separator + "*" + ";");
                args.add(MCPathHelper.getOS().getVersionDir() + Constants.getLauncherNameVersion() + ".jar\" ");

                // Main class
                args.add("net.fabricmc.loader.impl.launch.knot.KnotClient");

                // args end
                /*
                args.add(" --auth_player_name " + "DefaultName");
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
                args.add(" --auth_xuid " + "-");
                */


                args.add(" --username " + "Steve");
                args.add(" --version " + Constants.getLauncherNameVersion());
                args.add(" --gameDir " + MCPathHelper.getOS().getClientDir());
                args.add(" --assetsDir " + MCPathHelper.getOS().getMc() + "assets");
                args.add(" --assetIndex " + "1.20.6-16");
                args.add(" --uuid " + UUID.randomUUID());
                args.add(" --accessToken " + "aeef7bc935f9420eb6314dea7ad7e1e5");
                args.add(" --userType " + "mojang");
                args.add(" --versionType " + "release");


                // args options

                if (!LaunchOption.serverIp.isEmpty()){
                    args.add(" --server " + LaunchOption.serverIp);
                    if (!LaunchOption.port.isEmpty()){
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

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }


    }

    public static boolean canLaunch(){
        return !isRunning;
    }

    /**
     * Hide the access token inside the console
     * @param arguments The Token
     * @return The arguments List<String> with the hidden token
     */
    public static String hideToken(String[] arguments) {
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

    public static void clientClosed(){
        // JS client closed animation
        FeedforwardHandler.clientClosed();

        // Close Client Output UI
//        ClientOutputUI.getInstance().exit();

        InstallHandler.isRunning = false;
        LaunchHandler.isRunning = false;
    }
}

package ps.psunset.cloudlauncher.util;

import mjson.Json;
import ps.psunset.cloudlauncher.util.path.CLPathHelper;
import ps.psunset.cloudlauncher.util.path.MCPathHelper;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;

public class ConfigHelper {

    /*private static String getConfigFilePath(String key){
        return MCPathHelper.getOS().getLauncherConfigDir() + key;
    }

    private static String getConfigFile(String key) {
        String config = "";
        try{
            InputStream stream = Files.newInputStream(Path.of(getConfigFilePath(key) + ".txt"), new OpenOption[0]);
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader buffReader = new BufferedReader(reader);
            config = buffReader.readLine();
            buffReader.close();
            reader.close();
            stream.close();
        } catch (IOException e){
            e.printStackTrace();
            Launcher.getInstance().die(e);
        }
        return config;
    }

    private static void setConfigFile(String key, String value) {
        try {
            OutputStream stream = Files.newOutputStream(Path.of(getConfigFilePath(key) + ".txt"), new OpenOption[0]);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(value);
            bufferedWriter.close();
            writer.close();
            stream.close();
        } catch (IOException e){
            e.printStackTrace();
            Launcher.getInstance().die(e);
        }
    }

    public static String getConfig(String key){
        try {
            checkDir();
        } catch (IOException e){
            e.printStackTrace();
        }
        return switch (key) {
            case "javaPath" -> getConfigFile("java-path");
            case "mcPath" -> getConfigFile("mc-path");
            case "maxRam" -> getConfigFile("max-ram");
            case "jvmArgs" -> getConfigFile("jvm-args");
            case "lastGameVersion" -> getConfigFile("latest-played-version");
            default -> throw new IllegalStateException("Unexpected value: " + key);
        };
    }

    public static void setConfig(String key, String value){
        try {
            checkDir();
        } catch (IOException e){
            e.printStackTrace();
        }
        switch (key) {
            case "javaPath" -> setConfigFile("java-path", value);
            case "mcPath" -> setConfigFile("mc-path", value);
            case "maxRam" -> setConfigFile("max-ram", value);
            case "jvmArgs" -> setConfigFile("jvm-args", value);
            case "lastGameVersion" -> setConfigFile("latest-played-version", value);
            default -> throw new IllegalStateException("Unexpected value: " + key);
        }
    }

    private static void checkDir() throws IOException {
        if (!new File(getConfigFilePath("")).exists()){
            new File(getConfigFilePath("")).mkdirs();
        }

        if (!new File(getConfigFilePath("java-path") + ".txt").exists()){
            Files.createFile(Path.of(new File(getConfigFilePath("java-path") + ".txt").getAbsolutePath()), new FileAttribute[0]);
        }
        if (!new File(getConfigFilePath("mc-path") + ".txt").exists()){
            Files.createFile(Path.of(new File(getConfigFilePath("mc-path") + ".txt").getAbsolutePath()), new FileAttribute[0]);
        }
        if (!new File(getConfigFilePath("max-ram") + ".txt").exists()){
            Files.createFile(Path.of(new File(getConfigFilePath("max-ram") + ".txt").getAbsolutePath()), new FileAttribute[0]);
        }
        if (!new File(getConfigFilePath("jvm-args") + ".txt").exists()){
            Files.createFile(Path.of(new File(getConfigFilePath("jvm-args") + ".txt").getAbsolutePath()), new FileAttribute[0]);
        }
        if (!new File(getConfigFilePath("latest-played-version") + ".txt").exists()){
            Files.createFile(Path.of(new File(getConfigFilePath("latest-played-version") + ".txt").getAbsolutePath()), new FileAttribute[0]);
        }
    }*/

    public static Json config = Json.object();
    public static File configFile = new File(MCPathHelper.getOS().getMc() + "cl-config.json");
    //public static File configFile = new File(ClassLoader.getSystemResource("assets/launcher/cl-config.json").getFile());
    public static Path configPath = Path.of(configFile.getPath());

    public static void checkConfig(){
        try {
            configPath.toFile().createNewFile();
            if (!FileHelper.readString(configPath).isEmpty()){
                config = Json.read(FileHelper.readString(configPath));
            } else {
                config.set("javaPath", "");
                config.set("mcPath", "");
                config.set("maxRam", "");
                config.set("jvmArgs", "");
                config.set("lastGameVersion", "");
            }

            FileHelper.writeToFile(configPath, config.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setConfig(String key, String value){
        checkConfig();

        try {
            switch (key) {
                case "javaPath" -> config.set("javaPath", value);
                case "mcPath" -> config.set("mcPath", value);
                case "maxRam" -> config.set("maxRam", value);
                case "jvmArgs" -> config.set("jvmArgs", value);
                case "lastGameVersion" -> config.set("lastGameVersion", value);
                default -> throw new IllegalStateException("Unexpected value: " + key);
            }

            FileHelper.writeToFile(configPath, config.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getConfig(String key) {
        checkConfig();

        return switch (key) {
            case "javaPath" -> config.at("javaPath").asString();
            case "mcPath" -> config.at("mcPath").asString();
            case "maxRam" -> config.at("maxRam").asString();
            case "jvmArgs" -> config.at("jvmArgs").asString();
            case "lastGameVersion" -> config.at("lastGameVersion").asString();
            default -> throw new IllegalStateException("Unexpected value: " + key);
        };
    }
}

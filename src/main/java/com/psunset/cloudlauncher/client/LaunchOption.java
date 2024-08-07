package com.psunset.cloudlauncher.client;

import com.psunset.cloudlauncher.database.ConfigHelper;

public class LaunchOption {
    public final static String defaultJavaParameter =
            "-XX:+UnlockExperimentalVMOptions " +
            "-XX:+UseG1GC " +
            "-XX:G1NewSizePercent=20 " +
            "-XX:G1ReservePercent=20 " +
            "-XX:MaxGCPauseMillis=50 " +
            "-XX:G1HeapRegionSize=32M";

    public final static String defaultJavaPath = System.getProperty("java.home");
    public final static int defaultMaximumRamMb = 4096;
    public static int minimumRamMb = 1024;
    //public static MProfile startProfile = null;
    //public static MSession session = null;
    public static String launcherName = "";
    public static String serverIp = "";
    public static String port = "";

    public static String customJavaPath = ConfigHelper.Type.JAVA_PATH.getValue();
    public static String customJavaParameter = ConfigHelper.Type.JVM_ARGS.getValue();
    public static String customMaximumRamMb = ConfigHelper.Type.MAX_RAM.getValue();

    public static int screenWidth = 854;
    public static int screenHeight = 480;

    public static String getJavaPath(){
        if (!customJavaPath.isEmpty()) {
            System.out.println("java path: " + customJavaPath);
            return customJavaPath;
        } else {
            return defaultJavaPath;
        }
    }

    public static String getJavaParameter() {
        if (!customJavaParameter.isEmpty()){
            return customJavaParameter;
        } else {
            return defaultJavaParameter;
        }
    }

    public static String getMaximumRamMb() {
        if (!customMaximumRamMb.isEmpty()){
            return "-Xmx" + customMaximumRamMb + "M";
        } else {
            return "-Xmx" + defaultMaximumRamMb + "M";
        }
    }

    public static String getMinimumRamMb() {
        return "-Xms" + minimumRamMb + "M";
    }
}

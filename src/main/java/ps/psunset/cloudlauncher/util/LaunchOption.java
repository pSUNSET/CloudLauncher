package ps.psunset.cloudlauncher.util;

public class LaunchOption {
    public static String defaultJavaParameter =
            "-XX:+UnlockExperimentalVMOptions " +
            "-XX:+UseG1GC " +
            "-XX:G1NewSizePercent=20 " +
            "-XX:G1ReservePercent=20 " +
            "-XX:MaxGCPauseMillis=50 " +
            "-XX:G1HeapRegionSize=32M";

    public static String defaultJavaPath = System.getProperty("java.home");
    public static int maximumRamMb = 2048;
    public static int minimumRamMb = 1024;
    //public static MProfile startProfile = null;
    //public static MSession session = null;
    public static String launcherName = "";
    public static String serverIp = "";
    public static String port = "";

    public static String customJavaPath = "";
    public static String customJavaParameter = "";
    public static int customMaximumRamMb = 0;

    public static int screenWidth = 854;
    public static int screenHeight = 480;

    public void checkValid() {
        var exMsg = ""; // error message

        if (maximumRamMb < 1)
            exMsg = "MaximumRamMb is too small.";

        if (minimumRamMb < 1)
            exMsg = "MinimumRamMb is too small.";

        //if (startProfile == null)
        //    exMsg = "StartProfile is null";

        //if (session == null)
        //    exMsg = "Session is null";

        if (launcherName == null)
            launcherName = "";
        else if (launcherName.contains(" "))
            exMsg = "Launcher Name must not contains Space.";

        if (serverIp == null)
            serverIp = "";

        if (customJavaParameter == null)
            customJavaParameter = "";

        if (screenWidth < 0 || screenHeight < 0)
            exMsg = "Screen Size must be greater than or equal to zero.";

        if (exMsg != "") // if launch option is invaild, throw exception
            throw new RuntimeException(exMsg);
    }
}

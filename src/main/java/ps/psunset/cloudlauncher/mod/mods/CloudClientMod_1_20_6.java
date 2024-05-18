package ps.psunset.cloudlauncher.mod.mods;

import ps.psunset.cloudlauncher.mod.ModFile;

import java.io.InputStream;

public class CloudClientMod_1_20_6 extends ModFile {
    public static String modVersion;

    public CloudClientMod_1_20_6() {
        super("1.20.6",
                "liquidbounce(cloudaddon)",
                getModVersion(),
                "https://github.com/pSUNSET/CloudClient/releases/download/CloudClient/liquidbounce.cloudaddon.-" + getModVersion() + ".jar");
    }

    public static String getModVersion(){
        if (modVersion == null){
            InputStream stream;
            /*try{
                stream = FileHelper.getStreamFromURL(LAUNCHER_URL + "mod-version.txt");
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader buffReader = new BufferedReader(reader);
                modVersion = buffReader.readLine();
                buffReader.close();
                reader.close();
                stream.close();
            } catch (IOException e){
                e.printStackTrace();
                Launcher.getInstance().die(e);
            }*/
        }

        modVersion = "0.0.1";

        return modVersion;
    }
}

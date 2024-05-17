package ps.psunset.cloudlauncher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Constants {
    // With https://www.base64-image.de/
    public static final String MC_LAUNCHER_ICON = "data:image/png;base64,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";
    public static final String BASE_URL = "https:///";

    private static String versionNumber = null;
    private static String fileName = null;

    private static String getVersionNumber(){
        if (versionNumber == null){
            try{
                InputStream stream = FileHelper.getStreamFromURL(BASE_URL + "version.txt");
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader buffReader = new BufferedReader(reader);
                versionNumber = buffReader.readLine();
                buffReader.close();
                reader.close();
                stream.close();
            } catch (IOException e){
                e.printStackTrace();;
            }
        }
        return versionNumber;
    }

    private static String getFileName(){
        if (fileName == null){
            fileName = "liquidbounce(cloudaddon)-" + getVersionNumber() + "-dev";
        }
        return fileName;
    }

    public static InputStream getIcon(){
        return ClassLoader.getSystemResourceAsStream("assets/icon_1024x1024.png");
    }

    public static InputStream getJar() throws IOException {
        return FileHelper.getStreamFromURL(BASE_URL + getFileName() + ".jar");
    }
    public static InputStream getJSON() throws IOException {
        return FileHelper.getStreamFromURL(BASE_URL + getFileName() + ".json");
    }
}

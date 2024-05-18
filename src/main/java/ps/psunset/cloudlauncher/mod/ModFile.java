package ps.psunset.cloudlauncher.mod;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ModFile {
    private final String gamaVersion;
    private final String fileName;
    private final String version;
    private final String url;

    public ModFile (String gameVersion, String fileName, String version , String url){
        this.gamaVersion = gameVersion;
        this.fileName = fileName;
        this.version = version;
        this.url = url;
    }

    public String getFileName(){
        return fileName + "-" +  getVersion() + ".jar";
    }

    public String getVersion(){
        return version;
    }

    public URL getURL() throws MalformedURLException {
        return new URL(url);
    }

    public String getGamaVersion() {
        return gamaVersion;
    }
}

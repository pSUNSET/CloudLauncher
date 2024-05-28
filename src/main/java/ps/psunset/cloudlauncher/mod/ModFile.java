package ps.psunset.cloudlauncher.mod;

import ps.psunset.cloudlauncher.util.MCPathHelper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class ModFile extends File {
    private final String gamaVersion;
    private final String fileName;
    private final String version;
    private final String url;

    public ModFile (String gameVersion, String fileName, String version , String url){
        super(MCPathHelper.getOS().getModsDir());

        this.gamaVersion = gameVersion;
        this.fileName = fileName;
        this.version = version;
        this.url = url;
    }

    @Override
    public String getName(){
        return fileName + "-" +  getVersion() + ".jar";
    }

    public String getFileName(){
        return fileName;
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

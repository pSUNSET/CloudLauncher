package ps.psunset.cloudlauncher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

public class FileHelper {
    public static InputStream getStreamFromURL(final String url) throws IOException {
        final URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept-Language", "en-US,en;q-0.5");
        connection.setDoOutput(true);
        return connection.getInputStream();
    }

}

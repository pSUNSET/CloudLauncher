package ps.psunset.cloudlauncher.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;

/**
 * Utils about read, write, download......
 */
public class FileHelper {
    public static InputStream getStreamFromURL(final String url) throws IOException {
        final URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept-Language", "en-US,en;q-0.5");
        connection.setDoOutput(true);
        return connection.getInputStream();
    }

    public static void deleteDirectory(File dir) {
        if (dir.exists()){
            for (final File file :  dir.listFiles()) {
                if (file.isDirectory()){
                    deleteDirectory(file);
                }
                else {
                    file.delete();
                }
            }
        }
    }

    public static String readFile(final File fileIn) throws IOException {
        final FileReader fileReader = new FileReader(fileIn);
        final BufferedReader buffReader = new BufferedReader(fileReader);
        StringBuilder sb = new StringBuilder();

        String currLine;

        while((currLine = buffReader.readLine()) != null && !currLine.startsWith("#")){
            sb.append(currLine);
        }

        buffReader.close();
        fileReader.close();

        return  sb.toString();
    }

    public static void writeLine(String text, File file) throws IOException {
        if(file.exists()){
            file.delete();
        }
        final PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        writer.println(text);
        writer.close();
    }

    public static void writeLine(InputStream input, File file) throws IOException {
        if(file.exists()){
            file.delete();
        }
        final FileOutputStream outputStream = new FileOutputStream(file);
        final byte[] buffer = new byte[8192];
        int bytesRead;
        while((bytesRead = input.read(buffer)) != -1){
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
    }

    public static String readString(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    public static String readString(URL url) throws IOException {
        InputStream is = openUrl(url);
        try {
            String str = readString(is);
            if (is != null)
                is.close();
            return str;
        } catch (Throwable throwable) {
            if (is != null)
                try {
                    is.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
            throw throwable;
        }
    }

    public static String readString(InputStream is) throws IOException {
        byte[] data = new byte[Math.max(1000, is.available())];
        int offset = 0;
        int len;
        while ((len = is.read(data, offset, data.length - offset)) >= 0) {
            offset += len;
            if (offset == data.length) {
                int next = is.read();
                if (next < 0)
                    break;
                data = Arrays.copyOf(data, data.length * 2);
                data[offset++] = (byte)next;
            }
        }
        return new String(data, 0, offset, StandardCharsets.UTF_8);
    }

    public static void writeToFile(Path path, String string) throws IOException {
        Files.write(path, string.getBytes(StandardCharsets.UTF_8), new java.nio.file.OpenOption[0]);
    }

    public static void downloadFile(URL url, Path path) throws IOException {
        try {
            InputStream in = openUrl(url);
            try {
                Files.createDirectories(path.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
                Files.copy(in, path, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
                if (in != null)
                    in.close();
            } catch (Throwable throwable) {
                if (in != null)
                    try {
                        in.close();
                    } catch (Throwable throwable1) {
                        throwable.addSuppressed(throwable1);
                    }
                throw throwable;
            }
        } catch (Throwable t) {
            try {
                Files.deleteIfExists(path);
            } catch (Throwable t2) {
                t.addSuppressed(t2);
            }
            throw t;
        }
    }

    private static InputStream openUrl(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode < 200 || responseCode >= 300)
            throw new IOException("HTTP request to " + url + " failed: " + responseCode);
        return conn.getInputStream();
    }
}

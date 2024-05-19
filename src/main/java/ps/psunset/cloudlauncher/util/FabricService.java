package ps.psunset.cloudlauncher.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import mjson.Json;

/**
 * Code from Fabric Installer by <a href="https://github.com/FabricMC">FabricMC</a>
 */
public final class FabricService {
    private static int activeIndex = 0;

    private static FabricService fixedService;

    private final String meta;

    private final String maven;

    public static Json queryMetaJson(String path) throws IOException {
        return invokeWithFallbacks((service, arg) -> Json.read(FileHelper.readString(new URL(service.meta + arg))), path);
    }

    public static Json queryJsonSubstitutedMaven(String url) throws IOException {
        if (!url.startsWith("https://maven.fabricmc.net/"))
            return Json.read(FileHelper.readString(new URL(url)));
        String path = url.substring("https://maven.fabricmc.net/".length());
        return invokeWithFallbacks((service, arg) -> Json.read(FileHelper.readString(new URL(service.maven + arg))), path);
    }

    public static void downloadSubstitutedMaven(String url, Path out) throws IOException {
        if (!url.startsWith("https://maven.fabricmc.net/")) {
            FileHelper.downloadFile(new URL(url), out);
            return;
        }
        String path = url.substring("https://maven.fabricmc.net/".length());
        invokeWithFallbacks((service, arg) -> {
            FileHelper.downloadFile(new URL(service.maven + arg), out);
            return null;
        }, path);
    }

    private static <A, R> R invokeWithFallbacks(Handler<A, R> handler, A arg) throws IOException {
        if (fixedService != null)
            return handler.apply(fixedService, arg);
        int index = activeIndex;
        IOException exc = null;
        while (true) {
            FabricService service = Reference.FABRIC_SERVICES[index];
            try {
                R ret = handler.apply(service, arg);
                activeIndex = index;
                return ret;
            } catch (IOException e) {
                System.out.println("service " + service + " failed: " + e);
                if (exc == null) {
                    exc = e;
                } else {
                    exc.addSuppressed(e);
                }
                index = (index + 1) % Reference.FABRIC_SERVICES.length;
                if (index == activeIndex)
                    break;
            }
        }
        throw exc;
    }

    public static void setFixed(String metaUrl, String mavenUrl) {
        if (metaUrl == null && mavenUrl == null)
            throw new NullPointerException("both meta and maven are null");
        if (metaUrl == null)
            metaUrl = "https://meta.fabricmc.net/";
        if (mavenUrl == null)
            mavenUrl = "https://maven.fabricmc.net/";
        activeIndex = -1;
        fixedService = new FabricService(metaUrl, mavenUrl);
    }

    FabricService(String meta, String maven) {
        this.meta = meta;
        this.maven = maven;
    }

    public String getMetaUrl() {
        return this.meta;
    }

    public String getMavenUrl() {
        return this.maven;
    }

    public String toString() {
        return "FabricService{meta='" + this.meta + '\'' + ", maven='" + this.maven + "'}";
    }

    private static interface Handler<A, R> {
        R apply(FabricService param1FabricService, A param1A) throws IOException;
    }
}

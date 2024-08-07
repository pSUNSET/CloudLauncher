package com.psunset.cloudlauncher.util;

import java.io.File;
import java.nio.file.Path;
import mjson.Json;

/**
 * Code from Fabric Installer by <a href="https://github.com/FabricMC">FabricMC</a> team
 */
public class FabricLibrary {
    public final String name;

    public final String url;

    public final Path inputPath;

    public FabricLibrary(String name, String url, Path inputPath) {
        this.name = name;
        this.url = url;
        this.inputPath = inputPath;
    }

    public FabricLibrary(Json json) {
        this.name = json.at("name").asString();
        this.url = json.at("url").asString();
        this.inputPath = null;
    }

    public String getURL() {
        String[] parts = this.name.split(":", 3);
        String path = parts[0].replace(".", "/") + "/" + parts[1] + "/" + parts[2] + "/" + parts[1] + "-" + parts[2] + ".jar";
        return this.url + path;
    }

    public String getPath() {
        String[] parts = this.name.split(":", 3);
        String path = parts[0].replace(".", File.separator) + File.separator + parts[1] + File.separator + parts[2] + File.separator + parts[1] + "-" + parts[2] + ".jar";
        return path.replaceAll(" ", "_");
    }
}

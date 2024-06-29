package com.psunset.cloudlauncher.util;

import java.text.SimpleDateFormat;

public class Reference {
    public static final String LOADER_NAME = "fabric-loader";

    public static final String FABRIC_API_URL = "https://www.curseforge.com/minecraft/mc-mods/fabric-api/";

    public static final String SERVER_LAUNCHER_URL = "https://fabricmc.net/use/server/";

    public static final String MINECRAFT_LAUNCHER_MANIFEST = "https://launchermeta.mojang.com/mc/game/version_manifest_v2.json";

    public static final String EXPERIMENTAL_LAUNCHER_MANIFEST = "https://maven.fabricmc.net/net/minecraft/experimental_versions.json";

    static final String FABRIC_META_SERVER = "https://meta.fabricmc.net/";

    static final String FABRIC_MAVEN_SERVER = "https://maven.fabricmc.net/";

    static final FabricService[] FABRIC_SERVICES = new FabricService[] {
            new FabricService("https://meta.fabricmc.net/", "https://maven.fabricmc.net/"), new FabricService("https://meta2.fabricmc.net/", "https://maven2.fabricmc.net/"), new FabricService("https://meta3.fabricmc.net/", "https://maven3.fabricmc.net/")
    };

    public static final String FABRIC_1_20_6_CLIENT_URL = "https://github.com/pSUNSET/CloudLauncher/releases/download/pack/fabric-loader-0.15.11-1.20.6.jar";

    public static final String CLOUD_MAVEN_SERVER = "https://maven.cloudclient.net/";

    public static final String LIBRARIES_1_20_6_URL = "https://github.com/pSUNSET/CloudLauncher/releases/download/pack/libraries.zip";

    public static final String NATIVES_1_20_6_URL = "https://github.com/pSUNSET/CloudLauncher/releases/download/pack/natives.zip";

    public static final String ASSETS_1_20_6_JSON_URL = "https://github.com/pSUNSET/CloudLauncher/releases/download/pack/1.20.6-16.json";

    public static final SimpleDateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Constants.getLocale());
}

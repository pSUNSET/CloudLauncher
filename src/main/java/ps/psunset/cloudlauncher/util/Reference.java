package ps.psunset.cloudlauncher.util;

public class Reference {
    public static final String LOADER_NAME = "fabric-loader";

    public static final String FABRIC_API_URL = "https://www.curseforge.com/minecraft/mc-mods/fabric-api/";

    public static final String SERVER_LAUNCHER_URL = "https://fabricmc.net/use/server/";

    public static final String MINECRAFT_LAUNCHER_MANIFEST = "https://launchermeta.mojang.com/mc/game/version_manifest_v2.json";

    public static final String EXPERIMENTAL_LAUNCHER_MANIFEST = "https://maven.fabricmc.net/net/minecraft/experimental_versions.json";

    static final String DEFAULT_META_SERVER = "https://meta.fabricmc.net/";

    static final String DEFAULT_MAVEN_SERVER = "https://maven.fabricmc.net/";

    static final FabricService[] FABRIC_SERVICES = new FabricService[] { new FabricService("https://meta.fabricmc.net/", "https://maven.fabricmc.net/"), new FabricService("https://meta2.fabricmc.net/", "https://maven2.fabricmc.net/"), new FabricService("https://meta3.fabricmc.net/", "https://maven3.fabricmc.net/") };
}

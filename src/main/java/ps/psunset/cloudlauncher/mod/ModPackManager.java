package ps.psunset.cloudlauncher.mod;

import ps.psunset.cloudlauncher.mod.mods.*;

import java.net.URL;

public class ModPackManager {
    public static ModFile[] getJar(String gameVersion){
        return switch (gameVersion){
            case "1.8.9":
                yield new ModFile[]{
                        new CloudClientMod_1_8_9()
                };

            case "1.20.6":
                yield new ModFile[]{
                        new CloudClientMod_1_20_6(),
                        new FabricLanguageKotlinMod_1_20_6(),
                        new ModMenu_1_20_6(),
                        new Sodium_1_20_6(),
                        new ViaFabricPlus_1_20_6()
                };

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        };
    }
}

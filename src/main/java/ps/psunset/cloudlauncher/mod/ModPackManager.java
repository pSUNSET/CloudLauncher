package ps.psunset.cloudlauncher.mod;

import ps.psunset.cloudlauncher.mod.mods.*;


public class ModPackManager {

    /**
    * Check out need download which mod or mod pack.
    */
    public static ModFile[] getMods(String gameVersion){
        return switch (gameVersion){
            // 1.20.6
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

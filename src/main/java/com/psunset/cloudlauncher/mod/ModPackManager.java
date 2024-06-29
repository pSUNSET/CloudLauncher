package com.psunset.cloudlauncher.mod;

import com.psunset.cloudlauncher.mod.mods.*;

import java.util.ArrayList;


public class ModPackManager {
    public static ModFile[] mods = new ModFile[]{
        new CloudClient_1_20_6(),
        new FabricAPI_1_20_6(),
        new FabricLanguageKotlin_1_20_6(),
        new Indium_1_20_6(),
        new Iris_1_20_6(),
        new Lithium_1_20_6(),
        new ModMenu_1_20_6(),
        new Orbit_1_20_6(),
        new Reflections_1_20_6(),
        new Sodium_1_20_6(),
        new ViaFabricPlus_1_20_6()
    };

    /**
    * Check out need download which mod or mod pack.
    */
    public static ArrayList<ModFile> getMods(String gameVersion){
        ArrayList<ModFile> modPack = new ArrayList<>();

        switch (gameVersion){
            // 1.20.6
            case "1.20.6":
                for (ModFile mod : mods) {
                    if (mod.getGamaVersion().equals("1.20.6")){
                        modPack.add(mod);
                    }
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + gameVersion);
        }

        return modPack;
    }
}

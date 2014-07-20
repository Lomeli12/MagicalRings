package net.lomeli.ring.core.handler;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;

public class ConfigurationHandler {
    private Configuration config;

    public ConfigurationHandler(Configuration config) {
        this.config = config;
    }

    public Configuration getConfig() {
        return this.config;
    }

    public void updateConfig() {
        String modOptions = "options";

        ModLibs.BASE_MP = config.getInt("StartingValue", modOptions, ModLibs.BASE_MP, 100, 500, "The base Max MP players start with");
        ModLibs.RECHARGE_WAIT_TIME = config.getInt("rechargeWaitTime", modOptions, ModLibs.RECHARGE_WAIT_TIME, 20, Integer.MAX_VALUE, "Number of ticks between a player's MP recharging. 20 ticks per second");

        String world = "worldgen";
        config.addCustomCategoryComment(world, "Adjust and enable/disable world gen. Rate = number of times a veins can spawn per chunk");

        ModLibs.tungstenSpawn = config.getBoolean("tungstenSpawn", world, ModLibs.tungstenSpawn, "");
        ModLibs.platinumSpawn = config.getBoolean("platinumSpawn", world, ModLibs.platinumSpawn, "");
        ModLibs.jadeSpawn = config.getBoolean("jadeSpawn", world, ModLibs.jadeSpawn, "");
        ModLibs.amberSpawn = config.getBoolean("amberSpawn", world, ModLibs.amberSpawn, "");
        ModLibs.peridotSpawn = config.getBoolean("peridotSpawn", world, ModLibs.peridotSpawn, "");
        ModLibs.rubySpawn = config.getBoolean("rubySpawn", world, ModLibs.rubySpawn, "");
        ModLibs.sapphireSpawn = config.getBoolean("sapphireSpawn", world, ModLibs.sapphireSpawn, "");
        ModLibs.amethystSpawn = config.getBoolean("amethystSpawn", world, ModLibs.amethystSpawn, "");

        ModLibs.tungstenRate = config.getInt("tungstenRate", world, 1, 0, Integer.MAX_VALUE, "");
        ModLibs.platinumRate = config.getInt("platinumRate", world, 1, 0, Integer.MAX_VALUE, "");
        ModLibs.jadeRate = config.getInt("jadeRate", world, 1, 0, Integer.MAX_VALUE, "");
        ModLibs.amberRate = config.getInt("amberRate", world, 2, 0, Integer.MAX_VALUE, "");
        ModLibs.peridotRate = config.getInt("peridotRate", world, 4, 0, Integer.MAX_VALUE, "");
        ModLibs.rubyRate = config.getInt("rubyRate", world, 4, 0, Integer.MAX_VALUE, "");
        ModLibs.sapphireRate = config.getInt("sapphireRate", world, 4, 0, Integer.MAX_VALUE, "");
        ModLibs.amethystRate = config.getInt("amethystRate", world, 2, 0, Integer.MAX_VALUE, "");

        ModLibs.tungstenSize = config.getInt("tungstenVeinSize", world, 1, 0, Integer.MAX_VALUE, "");
        ModLibs.platinumSize = config.getInt("platinumVeinSize", world, 2, 0, Integer.MAX_VALUE, "");
        ModLibs.jadeSize = config.getInt("jadeVeinSize", world, 3, 0, Integer.MAX_VALUE, "");
        ModLibs.amberSize = config.getInt("amberVeinSize", world, 4, 0, Integer.MAX_VALUE, "");
        ModLibs.peridotSize = config.getInt("peridotVeinSize", world, 5, 0, Integer.MAX_VALUE, "");
        ModLibs.rubySize = config.getInt("rubyVeinSize", world, 5, 0, Integer.MAX_VALUE, "");
        ModLibs.sapphireSize = config.getInt("sapphireVeinSize", world, 5, 0, Integer.MAX_VALUE, "");
        ModLibs.amethystSize = config.getInt("amethystVeinSize", world, 4, 0, Integer.MAX_VALUE, "");

        ModLibs.activateRetroGen = config.getBoolean("activateRetroGen", world, false,
                "Warning! Should only be used if there are chunks where loaded before mod was installed. Running it in a new world could cause bad things to happen! It will also fill your console with tons of info you probably won't care to understand. Make sure to turn off after first use!");

        ModLibs.manaFlowerDensity = config.getInt("manaFlowerDensity", world, ModLibs.manaFlowerDensity, 0, Integer.MAX_VALUE, "");
        ModLibs.manaFlowerQuantity = config.getInt("manaFlowerQuantity", world, ModLibs.manaFlowerQuantity, 0, Integer.MAX_VALUE, "");

        String clientOptions = "clientoptions";
        config.addCustomCategoryComment(clientOptions, "Change the x and y position where your MP is displayed.");

        ModLibs.DISPLAY_X = config.getInt("manaDisplayX", clientOptions, ModLibs.DISPLAY_X, 0, Integer.MAX_VALUE, "");
        ModLibs.DISPLAY_Y = config.getInt("manaDisplayY", clientOptions, ModLibs.DISPLAY_Y, 0, Integer.MAX_VALUE, "");

        if (this.config.hasChanged())
            this.config.save();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equalsIgnoreCase(ModLibs.MOD_ID))
            Rings.configHandler.updateConfig();
    }
}

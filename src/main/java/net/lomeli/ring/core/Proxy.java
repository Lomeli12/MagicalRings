package net.lomeli.ring.core;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import net.lomeli.ring.api.RingAPI;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.block.tile.TileAltar;
import net.lomeli.ring.block.tile.TileItemAltar;
import net.lomeli.ring.block.tile.TileRingForge;
import net.lomeli.ring.core.handler.EntityHandler;
import net.lomeli.ring.core.handler.GameEventHandler;
import net.lomeli.ring.core.handler.TickHandlerCore;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.lomeli.ring.magic.RingMaterialRecipe;
import net.lomeli.ring.worldgen.WorldGenManager;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class Proxy {
    public MagicHandler magicHandler;
    public RingMaterialRecipe ringMaterials;
    public TickHandlerCore tickHandler;
    public WorldGenManager genManager;
    public File modConfig;

    public void preInit() {
        ModItems.loadItems();
        ModBlocks.loadBlocks();
        magicHandler = new MagicHandler();
        ringMaterials = new RingMaterialRecipe();
        genManager = new WorldGenManager();
        tickHandler = new TickHandlerCore();
    }

    public void init() {
        ModRecipe.addChestLoot();
        GameRegistry.registerWorldGenerator(genManager, 100);
        FMLCommonHandler.instance().bus().register(tickHandler);
        MinecraftForge.EVENT_BUS.register(genManager);
        GameRegistry.registerTileEntity(TileAltar.class, TileAltar.class.getName().toLowerCase());
        GameRegistry.registerTileEntity(TileItemAltar.class, TileItemAltar.class.getName().toLowerCase());
        GameRegistry.registerTileEntity(TileRingForge.class, TileRingForge.class.getName().toLowerCase());

        MinecraftForge.EVENT_BUS.register(new EntityHandler());
        FMLCommonHandler.instance().bus().register(new GameEventHandler());
    }

    public void postInit() {
        ModRecipe.load();
        RingAPI.magicHandler = this.magicHandler;
        RingAPI.materialRegistry = this.ringMaterials;
    }

    public void changeClientConfig(int x, int y) {
    }

    public void loadConfig(File file) {
        Configuration config = new Configuration(file);

        config.load();

        baseConfig(config);

        config.save();

        this.modConfig = file;
    }

    public void baseConfig(Configuration config) {
        String modOptions = "Options";

        ModLibs.BASE_MP = config.get(modOptions, "StartingValue", 100, "The base Max MP players start with").getInt(100);
        ModLibs.RECHARGE_WAIT_TIME = config.get(modOptions, "rechargeWaitTime", 70, "Number of ticks between a player's MP recharging. 20 ticks per second").getInt(70);

        String world = "WorldGen";
        config.addCustomCategoryComment(world, "Adjust and enable/disable world gen. Rate = number of times a veins can spawn per chunk");

        ModLibs.tungstenSpawn = config.get(world, "tungstenSpawn", true).getBoolean(true);
        ModLibs.platinumSpawn = config.get(world, "platinumSpawn", true).getBoolean(true);
        ModLibs.jadeSpawn = config.get(world, "jadeSpawn", true).getBoolean(true);
        ModLibs.amberSpawn = config.get(world, "amberSpawn", true).getBoolean(true);
        ModLibs.peridotSpawn = config.get(world, "peridotSpawn", true).getBoolean(true);
        ModLibs.rubySpawn = config.get(world, "rubySpawn", true).getBoolean(true);
        ModLibs.sapphireSpawn = config.get(world, "sapphireSpawn", true).getBoolean(true);
        ModLibs.amethystSpawn = config.get(world, "amethystSpawn", true).getBoolean(true);

        ModLibs.tungstenRate = config.get(world, "tungstenRate", 1).getInt(1);
        ModLibs.platinumRate = config.get(world, "platinumRate", 1).getInt(1);
        ModLibs.jadeRate = config.get(world, "jadeRate", 1).getInt(1);
        ModLibs.amberRate = config.get(world, "amberRate", 2).getInt(2);
        ModLibs.peridotRate = config.get(world, "peridotRate", 4).getInt(4);
        ModLibs.rubyRate = config.get(world, "rubyRate", 4).getInt(4);
        ModLibs.sapphireRate = config.get(world, "sapphireRate", 4).getInt(4);
        ModLibs.amethystRate = config.get(world, "amethystRate", 2).getInt(2);

        ModLibs.tungstenSize = config.get(world, "tungstenVeinSize", 1).getInt(1);
        ModLibs.platinumSize = config.get(world, "platinumVeinSize", 2).getInt(2);
        ModLibs.jadeSize = config.get(world, "jadeVeinSize", 3).getInt(3);
        ModLibs.amberSize = config.get(world, "amberVeinSize", 4).getInt(4);
        ModLibs.peridotSize = config.get(world, "peridotVeinSize", 5).getInt(5);
        ModLibs.rubySize = config.get(world, "rubyVeinSize", 5).getInt(5);
        ModLibs.sapphireSize = config.get(world, "sapphireVeinSize", 5).getInt(5);
        ModLibs.amethystSize = config.get(world, "amethystVeinSize", 4).getInt(4);

        ModLibs.activateRetroGen = config.get(world, "activateRetroGen", false,
                "Warning! Should only be used if there are chunks where loaded before mod was installed. Running it in a new world could cause bad things to happen! It will also fill your console with tons of info you probably won't care to understand. Make sure to turn off after first use!")
                .getBoolean(false);
    }

    public EntityPlayer getPlayerFromNetHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer)
            return ((NetHandlerPlayServer) handler).playerEntity;
        else
            return null;
    }
}

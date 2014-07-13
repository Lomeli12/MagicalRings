package net.lomeli.ring.core;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.block.tile.TileAltar;
import net.lomeli.ring.block.tile.TileItemAltar;
import net.lomeli.ring.block.tile.TileRingForge;
import net.lomeli.ring.client.page.PageUtil;
import net.lomeli.ring.core.handler.*;
import net.lomeli.ring.entity.ModEntities;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.RingMaterialRegistry;
import net.lomeli.ring.magic.SpellRegistry;
import net.lomeli.ring.worldgen.WorldGenManager;

public class Proxy {
    public SpellRegistry spellRegistry;
    public RingMaterialRegistry ringMaterials;
    public TickHandlerCore tickHandler;
    public WorldGenManager genManager;
    public Configuration config;
    public ManaHandler manaHandler;
    public PageUtil pageUtil;

    public void preInit() {
        ModItems.loadItems();
        ModBlocks.loadBlocks();
        spellRegistry = new SpellRegistry();
        ringMaterials = new RingMaterialRegistry();
        genManager = new WorldGenManager();
        tickHandler = new TickHandlerCore();
        manaHandler = new ManaHandler();
        pageUtil = new PageUtil();
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
        MinecraftForge.EVENT_BUS.register(new ItemEventHandler());
        FMLCommonHandler.instance().bus().register(new GameEventHandler());
        MinecraftForge.EVENT_BUS.register(manaHandler);
        ModEntities.registerEntities();
    }

    public void postInit() {
        ModRecipe.load();
        ApiRing.loadInstance();
    }

    public void changeClientConfig(int x, int y) {
    }

    public void loadConfig(File file) {
        config = new Configuration(file);
    }

    public void updateConfig() {
        config.load();

        baseConfig(config);

        config.save();
    }

    public void baseConfig(Configuration config) {


        String modOptions = "options";

        ModLibs.BASE_MP = config.getInt("StartingValue", modOptions, ModLibs.BASE_MP, 1, 500, "The base Max MP players start with");
        ModLibs.RECHARGE_WAIT_TIME = config.get(modOptions, "rechargeWaitTime", ModLibs.RECHARGE_WAIT_TIME, "Number of ticks between a player's MP recharging. 20 ticks per second").getInt(70);

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
}

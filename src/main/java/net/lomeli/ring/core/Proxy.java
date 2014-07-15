package net.lomeli.ring.core;

import net.minecraftforge.common.MinecraftForge;

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
import net.lomeli.ring.magic.RingMaterialRegistry;
import net.lomeli.ring.magic.SpellRegistry;

public class Proxy {
    public SpellRegistry spellRegistry;
    public RingMaterialRegistry ringMaterials;
    public TickHandlerCore tickHandler;
    public WorldGenHandler genManager;
    public ManaHandler manaHandler;
    public PageUtil pageUtil;

    public void preInit() {
        ModItems.loadItems();
        ModBlocks.loadBlocks();
        spellRegistry = new SpellRegistry();
        ringMaterials = new RingMaterialRegistry();
        genManager = new WorldGenHandler();
        tickHandler = new TickHandlerCore();
        manaHandler = new ManaHandler();
        pageUtil = new PageUtil();
    }

    public void init() {
        ModRecipe.addChestLoot();
        GameRegistry.registerWorldGenerator(genManager, 100);


        GameRegistry.registerTileEntity(TileAltar.class, TileAltar.class.getName().toLowerCase());
        GameRegistry.registerTileEntity(TileItemAltar.class, TileItemAltar.class.getName().toLowerCase());
        GameRegistry.registerTileEntity(TileRingForge.class, TileRingForge.class.getName().toLowerCase());

        MinecraftForge.TERRAIN_GEN_BUS.register(new BiomeDecorHandler());

        MinecraftForge.EVENT_BUS.register(genManager);
        MinecraftForge.EVENT_BUS.register(new EntityHandler());
        MinecraftForge.EVENT_BUS.register(new ItemEventHandler());
        MinecraftForge.EVENT_BUS.register(manaHandler);

        FMLCommonHandler.instance().bus().register(new GameEventHandler());
        FMLCommonHandler.instance().bus().register(tickHandler);

        ModEntities.registerEntities();
    }

    public void postInit() {
        ModRecipe.load();
        ApiRing.loadInstance();
    }
}

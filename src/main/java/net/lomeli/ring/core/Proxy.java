package net.lomeli.ring.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import net.minecraftforge.common.MinecraftForge;

import net.lomeli.ring.api.RingAPI;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.block.tile.TileAltar;
import net.lomeli.ring.block.tile.TileItemAltar;
import net.lomeli.ring.block.tile.TileRingForge;
import net.lomeli.ring.core.handler.EntityHandler;
import net.lomeli.ring.core.handler.GameEventHandler;
import net.lomeli.ring.core.handler.TickHandlerCore;
import net.lomeli.ring.item.ModItems;
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

    public void preInit() {
        magicHandler = new MagicHandler();
        ringMaterials = new RingMaterialRecipe();
        tickHandler = new TickHandlerCore();
        genManager = new WorldGenManager();

        ModItems.loadItems();
        ModBlocks.loadBlocks();
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

    public EntityPlayer getPlayerFromNetHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer)
            return ((NetHandlerPlayServer) handler).playerEntity;
        else
            return null;
    }
}

package net.lomeli.modjam4.core;

import net.lomeli.modjam4.block.ModBlocks;
import net.lomeli.modjam4.core.handler.EntityHandler;
import net.lomeli.modjam4.core.handler.GameEventHandler;
import net.lomeli.modjam4.item.ModItems;
import net.lomeli.modjam4.magic.MagicHandler;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class Proxy {
    public MagicHandler magicHandler;
    
    public void preInit() {
        magicHandler = new MagicHandler();
        ModItems.loadItems();
        ModBlocks.loadBlocks();
    }
    
    public void init() {
        MinecraftForge.EVENT_BUS.register(new EntityHandler());
        FMLCommonHandler.instance().bus().register(new GameEventHandler());
    }
    
    public void postInit() {
        
    }
}

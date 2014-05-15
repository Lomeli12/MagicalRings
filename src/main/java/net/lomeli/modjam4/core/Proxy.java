package net.lomeli.modjam4.core;

import net.lomeli.modjam4.block.ModBlocks;
import net.lomeli.modjam4.core.handler.EntityHandler;
import net.lomeli.modjam4.item.ModItems;
import net.lomeli.modjam4.magic.MagicHandler;
import net.minecraftforge.common.MinecraftForge;

public class Proxy {
    public MagicHandler magicHandler;
    
    public void preInit() {
        magicHandler = new MagicHandler();
        ModItems.loadItems();
        ModBlocks.loadBlocks();
    }
    
    public void init() {
        MinecraftForge.EVENT_BUS.register(new EntityHandler());
    }
    
    public void postInit() {
        
    }
}

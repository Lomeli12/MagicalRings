package net.lomeli.ring.block;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static Block altar, ringForge;
    public static void loadBlocks() {
        altar = new BlockAltars("altar").setBlockName("altar");
        GameRegistry.registerBlock(altar, BlockAltars.ItemAltar.class, altar.getUnlocalizedName());
        
        ringForge = new BlockRingForge("ringForge").setBlockName("ringForge");
        GameRegistry.registerBlock(ringForge, ringForge.getUnlocalizedName());
    }
}

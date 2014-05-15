package net.lomeli.modjam4.block;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static Block altar;
    public static void loadBlocks() {
        altar = new BlockAltars("altar").setBlockName("altar");
        GameRegistry.registerBlock(altar, BlockAltars.ItemAltar.class, altar.getUnlocalizedName());
    }
}

package net.lomeli.ring.block;

import net.lomeli.ring.Rings;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPotato;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static Block altar, ringForge, oreBlocks, onionBlock;

    public static void loadBlocks() {
        altar = new BlockAltars("altar").setBlockName("altar");
        GameRegistry.registerBlock(altar, BlockAltars.ItemAltar.class, altar.getUnlocalizedName());

        ringForge = new BlockRingForge("ringForge").setBlockName("ringForge");
        GameRegistry.registerBlock(ringForge, ringForge.getUnlocalizedName());

        oreBlocks = new BlockOre("ore").setBlockName("ore");
        GameRegistry.registerBlock(oreBlocks, BlockOre.ItemBlockOre.class, oreBlocks.getUnlocalizedName());

        onionBlock = new BlockOnion().setBlockName(ModLibs.MOD_ID.toLowerCase() + ".onion");
        GameRegistry.registerBlock(onionBlock, onionBlock.getUnlocalizedName());

        OreDictionary.registerOre("oreTungsten", new ItemStack(oreBlocks, 1, 0));
        OreDictionary.registerOre("orePlatinum", new ItemStack(oreBlocks, 1, 1));
        OreDictionary.registerOre("oreJade", new ItemStack(oreBlocks, 1, 2));
        OreDictionary.registerOre("oreAmber", new ItemStack(oreBlocks, 1, 3));
        OreDictionary.registerOre("orePeridot", new ItemStack(oreBlocks, 1, 4));
        OreDictionary.registerOre("oreRuby", new ItemStack(oreBlocks, 1, 5));
        OreDictionary.registerOre("oreSapphire", new ItemStack(oreBlocks, 1, 6));
        OreDictionary.registerOre("oreAmethyst", new ItemStack(oreBlocks, 1, 7));
    }
}

package net.lomeli.ring.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static Block altar, ringForge, oreBlocks, onionBlock, manaFlower;

    public static void loadBlocks() {
        altar = new BlockAltars("altar").setBlockName("altar");
        registerBlock(altar, BlockAltars.ItemAltar.class, "altar");

        ringForge = new BlockRingForge("ringForge").setBlockName("ringForge");
        registerBlock(ringForge, "ringForge");

        oreBlocks = new BlockOre("ore").setBlockName("ore");
        registerBlock(oreBlocks, BlockOre.ItemBlockOre.class, "ore");

        onionBlock = new BlockOnion().setBlockName("onion");
        registerBlock(onionBlock, "onion");

        manaFlower = new BlockManaFlower("manaFlower_stage_").setBlockName("manaFlower");
        registerBlock(manaFlower, BlockManaFlower.ItemManaBush.class, "manaFlower");

        OreDictionary.registerOre("oreTungsten", new ItemStack(oreBlocks, 1, 0));
        OreDictionary.registerOre("orePlatinum", new ItemStack(oreBlocks, 1, 1));
        OreDictionary.registerOre("oreJade", new ItemStack(oreBlocks, 1, 2));
        OreDictionary.registerOre("oreAmber", new ItemStack(oreBlocks, 1, 3));
        OreDictionary.registerOre("orePeridot", new ItemStack(oreBlocks, 1, 4));
        OreDictionary.registerOre("oreRuby", new ItemStack(oreBlocks, 1, 5));
        OreDictionary.registerOre("oreSapphire", new ItemStack(oreBlocks, 1, 6));
        OreDictionary.registerOre("oreAmethyst", new ItemStack(oreBlocks, 1, 7));
    }

    private static void registerBlock(Block block, String id) {
        GameRegistry.registerBlock(block, id);
    }

    private static void registerBlock(Block block, Class<? extends ItemBlock> clazz, String id) {
        GameRegistry.registerBlock(block, clazz, id);
    }
}

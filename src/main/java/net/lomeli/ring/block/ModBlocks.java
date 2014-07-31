package net.lomeli.ring.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static Block altar, ringForge, oreBlocks, onionBlock, manaFlower, manaGlass, metalPlate, manaBrick;

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

        manaGlass = new BlockCT(Material.glass, "glass", false).setHardness(0.3F).setResistance(20f).setBlockName("manaGlass");
        registerBlock(manaGlass, "manaGlass");

        metalPlate = new BlockCT(Material.iron, "metal", true).setHardness(2f).setResistance(20f).setBlockName("metalPlate");
        registerBlock(metalPlate, "metalPlate");

        manaBrick = new BlockRings(Material.rock, "manabrick").setHardness(2f).setResistance(20f).setBlockName("manaBrick");
        registerBlock(manaBrick, "manaBrick");

        OreDictionary.registerOre("oreTungsten", new ItemStack(oreBlocks, 1, 0));
        OreDictionary.registerOre("orePlatinum", new ItemStack(oreBlocks, 1, 1));
        OreDictionary.registerOre("oreJade", new ItemStack(oreBlocks, 1, 2));
        OreDictionary.registerOre("oreAmber", new ItemStack(oreBlocks, 1, 3));
        OreDictionary.registerOre("orePeridot", new ItemStack(oreBlocks, 1, 4));
        OreDictionary.registerOre("oreRuby", new ItemStack(oreBlocks, 1, 5));
        OreDictionary.registerOre("oreSapphire", new ItemStack(oreBlocks, 1, 6));
        OreDictionary.registerOre("oreAmethyst", new ItemStack(oreBlocks, 1, 7));
        OreDictionary.registerOre("blockGlass", manaGlass);
        OreDictionary.registerOre("plateIron", metalPlate);
        OreDictionary.registerOre("manaBrick", new ItemStack(manaBrick));
    }

    private static void registerBlock(Block block, String id) {
        GameRegistry.registerBlock(block, id);
    }

    private static void registerBlock(Block block, Class<? extends ItemBlock> clazz, String id) {
        GameRegistry.registerBlock(block, clazz, id);
    }
}

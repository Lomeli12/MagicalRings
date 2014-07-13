package net.lomeli.ring.core;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;

import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.magic.MagicHandler;

public class ModRecipe {
    public static void load() {
        shapedRecipes();
        shapelessRecipes();
        furnaceRecipes();
    }

    public static void addChestLoot() {
        for (int i = 0; i < MagicHandler.getAllSpells().size(); i++) {
            if (MagicHandler.getSpellLazy(i) != null) {
                ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.spellParchment, 1, i), 0, 1, 2));
                ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.spellParchment, 1, i), 0, 1, 2));
                ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.spellParchment, 1, i), 0, 1, 2));
                ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.spellParchment, 1, i), 0, 1, 2));
                ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.spellParchment, 1, i), 0, 1, 2));
            }
        }
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.food), 1, 5, 100));
    }

    private static void shapedRecipes() {
        hammerRecipe(ModItems.ironHammer, Blocks.iron_block);
        hammerRecipe(ModItems.diamondHammer, Blocks.diamond_block);
        addShaped(new ItemStack(ModBlocks.altar, 1, 1), "ISI", " I ", "ISI", 'I', "ingotIron", 'S', "stone");
        addShaped(new ItemStack(ModBlocks.altar, 1, 0), "ISI", " I ", "IEI", 'I', "ingotIron", 'S', "stone", 'E', Items.ender_pearl);
        addShaped(ModBlocks.ringForge, "OOO", "ICI", "IAI", 'O', Blocks.obsidian, 'I', "ingotIron", 'C', Blocks.crafting_table, 'A', Blocks.anvil);
        addShaped(new ItemStack(ModItems.materials, 1, 1), "CNC", "NDN", "CNC", 'C', "cobblestone", 'N', Blocks.netherrack, 'D', Blocks.dirt);
        addShaped(new ItemStack(ModItems.materials, 1, 3), "RIR", "IDI", "RIR", 'R', Blocks.redstone_block, 'I', "ingotIron", 'D', "gemDiamond");
    }

    private static void shapelessRecipes() {
        for (int i = 0; i < MagicHandler.getAllSpells().size(); i++) {
            if (MagicHandler.getSpellLazy(i) != null)
                addShapeless(new ItemStack(ModItems.spellParchment, 2, i), new ItemStack(ModItems.spellParchment, 1, i), Items.paper, Items.ender_pearl);
        }
        addShapeless(new ItemStack(ModItems.book, 1, 0), Items.book, "ingotIron");
        addShapeless(new ItemStack(ModItems.book, 1, 1), new ItemStack(ModItems.book, 1, 0), ModItems.ironHammer);
        addShapeless(new ItemStack(ModItems.book, 1, 1), new ItemStack(ModItems.book, 1, 0), ModItems.diamondHammer);
        //addShapeless(ModItems.book, "dyeRed", "dyeBlue", Items.book);
        addShapeless(new ItemStack(ModItems.food, 1, 2), new ItemStack(Items.potionitem, 1, 8261), Items.nether_star, Blocks.gold_block, Blocks.diamond_block);
    }

    private static void furnaceRecipes() {
        if (!Loader.isModLoaded("Railcraft"))
            addSmelt(new ItemStack(ModItems.oreItems, 1, 2), "ingotIron", 2);

        addSmelt(new ItemStack(ModItems.oreItems, 1, 0), new ItemStack(ModBlocks.oreBlocks, 1, 0), 4);
        addSmelt(new ItemStack(ModItems.oreItems, 1, 1), new ItemStack(ModBlocks.oreBlocks, 1, 1), 4);
        addSmelt(new ItemStack(ModItems.oreItems, 1, 3), new ItemStack(ModBlocks.oreBlocks, 1, 2), 4);
        addSmelt(new ItemStack(ModItems.oreItems, 1, 4), new ItemStack(ModBlocks.oreBlocks, 1, 3), 4);
        addSmelt(new ItemStack(ModItems.oreItems, 1, 5), new ItemStack(ModBlocks.oreBlocks, 1, 4), 4);
        addSmelt(new ItemStack(ModItems.oreItems, 1, 6), new ItemStack(ModBlocks.oreBlocks, 1, 5), 4);
        addSmelt(new ItemStack(ModItems.oreItems, 1, 7), new ItemStack(ModBlocks.oreBlocks, 1, 6), 4);
        addSmelt(new ItemStack(ModItems.oreItems, 1, 8), new ItemStack(ModBlocks.oreBlocks, 1, 7), 4);
        addSmelt(new ItemStack(ModItems.food, 1, 1), new ItemStack(ModItems.food, 1, 0), 2);
    }

    private static void hammerRecipe(Item hammer, Object obj) {
        ItemStack stack = null;

        if (obj instanceof ItemStack)
            stack = (ItemStack) obj;
        if (obj instanceof Block)
            stack = new ItemStack((Block) obj);
        if (obj instanceof Item)
            stack = new ItemStack((Item) obj);

        if (stack != null)
            addShaped(hammer, "I", "S", "S", 'S', "stickWood", 'I', stack);
    }

    private static void addShapeless(Object stack, Object... items) {
        if (stack instanceof ItemStack)
            GameRegistry.addRecipe(new ShapelessOreRecipe((ItemStack) stack, items));
        if (stack instanceof Item)
            GameRegistry.addRecipe(new ShapelessOreRecipe((Item) stack, items));
        if (stack instanceof Block)
            GameRegistry.addRecipe(new ShapelessOreRecipe((Block) stack, items));
    }

    private static void addShaped(Object stack, Object... items) {
        if (stack instanceof ItemStack)
            GameRegistry.addRecipe(new ShapedOreRecipe((ItemStack) stack, true, items));
        if (stack instanceof Item)
            GameRegistry.addRecipe(new ShapedOreRecipe((Item) stack, true, items));
        if (stack instanceof Block)
            GameRegistry.addRecipe(new ShapedOreRecipe((Block) stack, true, items));
    }

    private static void addSmelt(Object stack, Object input, float exp) {
        ItemStack result = null, in = null;
        if (stack instanceof ItemStack)
            result = (ItemStack) stack;
        if (stack instanceof Item)
            result = new ItemStack((Item) stack);
        if (stack instanceof Block)
            result = new ItemStack((Block) stack);

        if (input instanceof ItemStack)
            in = (ItemStack) input;
        if (input instanceof Item)
            in = new ItemStack((Item) input);
        if (input instanceof Block)
            in = new ItemStack((Block) input);
        if (input instanceof String) {
            ArrayList<ItemStack> inputs = OreDictionary.getOres((String) input);
            if (!inputs.isEmpty()) {
                for (ItemStack newInput : inputs) {
                    GameRegistry.addSmelting(newInput, result, exp);
                }
                return;
            }
        }

        if (result != null && in != null)
            GameRegistry.addSmelting(in, result, exp);
    }
}

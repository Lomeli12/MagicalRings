package net.lomeli.ring.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {
    public static Item magicRing, onion, ironHammer, diamondHammer, spellParchment, oreItems, book, food, ghostSword, materials;

    public static void loadItems() {
        magicRing = new ItemMagicRing("ring").setUnlocalizedName("magicRing");
        registerItem(magicRing, "magicRing");

        book = new ItemSpellBook("spellbook").setUnlocalizedName("book");
        registerItem(book, "book");

        ironHammer = new ItemHammer("hammerIron", 15).setUnlocalizedName("ironHammer");
        registerItem(ironHammer, "ironHammer");

        diamondHammer = new ItemHammer("hammerDiamond", 40).setUnlocalizedName("diamondHammer");
        registerItem(diamondHammer, "diamondHammer");

        spellParchment = new ItemSpellParchment("parchment").setUnlocalizedName("spellParchment");
        registerItem(spellParchment, "spellParchment");

        oreItems = new ItemOreItems("ingot").setUnlocalizedName("ingot");
        registerItem(oreItems, "ingot");

        food = new ItemMagicFood().setUnlocalizedName("food");
        registerItem(food, "food");

        ghostSword = new ItemGhostSword().setUnlocalizedName("ghostSword");
        registerItem(ghostSword, "ghostSword");

        materials = new ItemMaterial().setUnlocalizedName("material");
        registerItem(materials, "material");

        OreDictionary.registerOre("ingotTungsten", new ItemStack(oreItems, 1, 0));
        OreDictionary.registerOre("ingotPlatinum", new ItemStack(oreItems, 1, 1));
        OreDictionary.registerOre("ingotSteel", new ItemStack(oreItems, 1, 2));
        OreDictionary.registerOre("gemJade", new ItemStack(oreItems, 1, 3));
        OreDictionary.registerOre("gemAmber", new ItemStack(oreItems, 1, 4));
        OreDictionary.registerOre("gemPeridot", new ItemStack(oreItems, 1, 5));
        OreDictionary.registerOre("gemRuby", new ItemStack(oreItems, 1, 6));
        OreDictionary.registerOre("gemSapphire", new ItemStack(oreItems, 1, 7));
        OreDictionary.registerOre("gemAmethyst", new ItemStack(oreItems, 1, 8));
    }

    private static void registerItem(Item item, String id) {
        GameRegistry.registerItem(item, id);
    }
}

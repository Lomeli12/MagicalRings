package net.lomeli.ring.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {
    public static Item magicRing, onion, ironHammer, diamondHammer, spellParchment, oreItems, book, food, ghostSword, materials, sigil;

    public static void loadItems() {
        magicRing = new ItemMagicRing("ring").setUnlocalizedName("magicRing");
        registerItem(magicRing, "magicRing");

        sigil = new ItemSigil("sigil").setUnlocalizedName("sigil");
        registerItem(sigil, "sigil");

        book = new ItemSpellBook("spellbook").setUnlocalizedName("book");
        registerItem(book, "book");

        ironHammer = new ItemHammer("hammerIron", 50).setUnlocalizedName("ironHammer");
        registerItem(ironHammer, "ironHammer");

        diamondHammer = new ItemHammer("hammerDiamond", 110).setUnlocalizedName("diamondHammer");
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
        OreDictionary.registerOre("batWing", new ItemStack(materials, 1, 0));
        OreDictionary.registerOre("fireStone", new ItemStack(materials, 1, 1));
        OreDictionary.registerOre("magmaStone", new ItemStack(materials, 1, 2));
        OreDictionary.registerOre("gemCharged", new ItemStack(materials, 1, 3));
        OreDictionary.registerOre("tentacle", new ItemStack(materials, 1, 4));
        OreDictionary.registerOre("berryMana", new ItemStack(materials, 1, 5));
        OreDictionary.registerOre("gemMana", new ItemStack(materials, 1, 6));
        OreDictionary.registerOre("hammer", new ItemStack(ironHammer, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("hammer", new ItemStack(diamondHammer, 1, OreDictionary.WILDCARD_VALUE));
    }

    private static void registerItem(Item item, String id) {
        GameRegistry.registerItem(item, id);
    }
}

package net.lomeli.ring.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {
    public static Item magicRing, onion, ironHammer, diamondHammer, spellParchment, oreItems, book, food, ghostSword;

    public static void loadItems() {
        magicRing = new ItemMagicRing("ring").setUnlocalizedName("magicRing");
        GameRegistry.registerItem(magicRing, magicRing.getUnlocalizedName());
        
        book = new ItemSpellBook("spellbook").setUnlocalizedName("book");
        GameRegistry.registerItem(book, book.getUnlocalizedName());

        ironHammer = new ItemHammer("hammerIron", 15).setUnlocalizedName("ironHammer");
        GameRegistry.registerItem(ironHammer, ironHammer.getUnlocalizedName());

        diamondHammer = new ItemHammer("hammerDiamond", 40).setUnlocalizedName("diamondHammer");
        GameRegistry.registerItem(diamondHammer, diamondHammer.getUnlocalizedName());

        spellParchment = new ItemSpellParchment("parchment").setUnlocalizedName("spellParchment");
        GameRegistry.registerItem(spellParchment, spellParchment.getUnlocalizedName());
        
        oreItems = new ItemOreItems("ingot").setUnlocalizedName("ingot");
        GameRegistry.registerItem(oreItems, oreItems.getUnlocalizedName());
        
        food = new ItemMagicFood().setUnlocalizedName("food");
        GameRegistry.registerItem(food, food.getUnlocalizedName());
        
        ghostSword = new ItemGhostSword().setUnlocalizedName("ghostSword");
        GameRegistry.registerItem(ghostSword, ghostSword.getUnlocalizedName());
        
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
}

package net.lomeli.ring.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ModItems {
    public static Item magicRing, onion, ironHammer, diamondHammer;
    public static void loadItems() {
        magicRing = new ItemMagicRing("ring").setUnlocalizedName("magicRing");
        GameRegistry.registerItem(magicRing, magicRing.getUnlocalizedName());
        
        ironHammer = new ItemHammer("hammerIron", 15).setUnlocalizedName("ironHammer");
        GameRegistry.registerItem(ironHammer, ironHammer.getUnlocalizedName());
        
        diamondHammer = new ItemHammer("hammerDiamond", 40).setUnlocalizedName("diamondHammer");
        GameRegistry.registerItem(diamondHammer, diamondHammer.getUnlocalizedName());
    }
}

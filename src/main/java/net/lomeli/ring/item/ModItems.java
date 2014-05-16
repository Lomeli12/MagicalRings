package net.lomeli.ring.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ModItems {
    public static Item magicRing;
    public static void loadItems() {
        magicRing = new ItemMagicRing("ring").setUnlocalizedName("magicRing");
        GameRegistry.registerItem(magicRing, magicRing.getUnlocalizedName());
    }
}

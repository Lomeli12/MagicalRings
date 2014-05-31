package net.lomeli.ring.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import net.lomeli.ring.block.ModBlocks;

public class CreativeRing extends CreativeTabs {

    public CreativeRing(String lable) {
        super(lable);
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(ModBlocks.ringForge);
    }

}

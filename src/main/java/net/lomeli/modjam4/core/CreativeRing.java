package net.lomeli.modjam4.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class CreativeRing extends CreativeTabs{

    public CreativeRing(String lable) {
        super(lable);
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(Blocks.enchanting_table);
    }

}

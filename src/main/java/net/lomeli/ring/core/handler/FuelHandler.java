package net.lomeli.ring.core.handler;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.MathHelper;

import cpw.mods.fml.common.IFuelHandler;

import net.lomeli.ring.item.ModItems;

public class FuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel.getItem() == ModItems.materials) {
            switch (fuel.getItemDamage()) {
                case 1:
                    return MathHelper.floor_double(2.5 * TileEntityFurnace.getItemBurnTime(new ItemStack(Items.coal)));
                case 2:
                    return 5 * TileEntityFurnace.getItemBurnTime(new ItemStack(Items.coal));
            }
        }
        return 0;
    }
}

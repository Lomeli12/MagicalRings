package net.lomeli.ring.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.lomeli.ring.item.ItemHammer;

public class SlotHammer extends Slot {

    public SlotHammer(IInventory par1iInventory, int par2, int par3, int par4) {
        super(par1iInventory, par2, par3, par4);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return (stack.getItem() instanceof ItemHammer);
    }
}

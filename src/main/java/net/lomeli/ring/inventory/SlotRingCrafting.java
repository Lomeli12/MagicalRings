package net.lomeli.ring.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRingCrafting extends Slot {

    private IInventory inventory;

    public SlotRingCrafting(IInventory tileInventory, int par2, int par3, int par4) {
        super(tileInventory, par2, par3, par4);
        this.inventory = tileInventory;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return false;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        for (int i = 0; i < inventory.getSizeInventory() - 3; i++) {
            ItemStack slotStack = inventory.getStackInSlot(i);
            if (slotStack != null) {
                slotStack.stackSize -= 10;
                if (slotStack.stackSize <= 0)
                    inventory.setInventorySlotContents(0, null);
            }
        }
        if (inventory.getStackInSlot(2) != null)
            inventory.setInventorySlotContents(2, null);
        
        ItemStack hammer = inventory.getStackInSlot(3);
        if (hammer != null) {
            hammer.setItemDamage(hammer.getItemDamage() + 1);
            if (hammer.getItemDamage() >= hammer.getMaxDamage())
                hammer = null;
            inventory.setInventorySlotContents(3, hammer);
        }
    }
}

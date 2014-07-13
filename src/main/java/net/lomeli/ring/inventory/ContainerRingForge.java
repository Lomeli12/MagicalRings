package net.lomeli.ring.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.block.tile.TileRingForge;

public class ContainerRingForge extends Container {

    private World world;
    private int x, y, z;

    public ContainerRingForge(TileRingForge tile, InventoryPlayer player, World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        this.addSlotToContainer(new SlotRingCrafting(tile, 4, 124, 35));

        this.addSlotToContainer(new Slot(tile, 0, 66, 26));
        this.addSlotToContainer(new Slot(tile, 1, 66, 44));
        this.addSlotToContainer(new Slot(tile, 2, 48, 35));
        this.addSlotToContainer(new SlotHammer(tile, 3, 10, 35));

        for (int l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(player, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(player, l, 8 + l * 18, 142));
        }
        this.onCraftMatrixChanged(tile);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.world.getBlock(this.x, this.y, this.z) != ModBlocks.ringForge ? false : player.getDistanceSq(this.x + 0.5D, this.y + 0.5D, this.z + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack() && slot.slotNumber != 4) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 0) {
                if (!this.mergeItemStack(itemstack1, 5, 41, true))
                    return null;

                slot.onSlotChange(itemstack1, itemstack);
            } else if (par2 >= 5 && par2 < 32) {
                if (!this.mergeItemStack(itemstack1, 32, 41, false))
                    return null;
            } else if (par2 >= 32 && par2 < 41) {
                if (!this.mergeItemStack(itemstack1, 5, 32, false))
                    return null;
            } else if (!this.mergeItemStack(itemstack1, 5, 41, false))
                return null;

            if (itemstack1.stackSize == 0)
                slot.putStack((ItemStack) null);
            else
                slot.onSlotChanged();

            if (itemstack1.stackSize == itemstack.stackSize)
                return null;
        }
        return null;
    }

}

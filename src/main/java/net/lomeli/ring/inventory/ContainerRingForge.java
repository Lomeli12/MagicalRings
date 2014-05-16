package net.lomeli.ring.inventory;

import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.block.tile.TileRingForge;
import net.lomeli.ring.item.ItemHammer;
import net.lomeli.ring.magic.RingMaterialRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerRingForge extends Container {

    private TileRingForge ringForge;
    private InventoryPlayer player;
    private World world;
    private int x, y, z;

    public ContainerRingForge(TileRingForge tile, InventoryPlayer player, World world, int x, int y, int z) {
        this.ringForge = tile;
        this.player = player;
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
        return this.world.getBlock(this.x, this.y, this.z) != ModBlocks.ringForge ? false : player.getDistanceSq((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 0) {
                if (!this.mergeItemStack(itemstack1, 4, 40, true))
                    return null;

                slot.onSlotChange(itemstack1, itemstack);
            }else if (par2 >= 4 && par2 < 31) {
                if (!this.mergeItemStack(itemstack1, 31, 40, false)) 
                    return null;
            }else if (par2 >= 31 && par2 < 40) {
                if (!this.mergeItemStack(itemstack1, 4, 31, false))
                    return null;
            }else if (!this.mergeItemStack(itemstack1, 4, 40, false))
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

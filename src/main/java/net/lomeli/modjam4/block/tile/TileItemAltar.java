package net.lomeli.modjam4.block.tile;

import net.lomeli.modjam4.lib.ModLibs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileItemAltar extends TileEntity implements IInventory{
    private ItemStack[] inventory;
    
    public TileItemAltar() {
        this.inventory = new ItemStack[1];
    }
    
    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return inventory[var1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        if (this.inventory[par1] != null) {
            ItemStack itemstack;

            if (this.inventory[par1].stackSize <= par2) {
                itemstack = this.inventory[par1];
                this.inventory[par1] = null;
                this.markDirty();
                return itemstack;
            }else {
                itemstack = this.inventory[par1].splitStack(par2);

                if (this.inventory[par1].stackSize == 0) {
                    this.inventory[par1] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        ItemStack stack = this.getStackInSlot(var1);
        inventory[var1] = null;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        inventory[var1] = var2;
    }

    @Override
    public String getInventoryName() {
        return ModLibs.ALTAR;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return inventory[var1] == null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound tag = nbt.getCompoundTag("inventory");
        this.inventory[0] = ItemStack.loadItemStackFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        
        NBTTagCompound tag = new NBTTagCompound();
        if (this.inventory[0] != null)
            this.inventory[0].writeToNBT(tag);
        nbt.setTag("inventory", tag);
    }
}

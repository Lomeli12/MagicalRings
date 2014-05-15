package net.lomeli.modjam4.block.tile;

import java.util.ArrayList;
import java.util.List;

import net.lomeli.modjam4.lib.ModLibs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileAltar extends TileEntity implements IInventory {

    private ItemStack[] inventory;

    public TileAltar() {
        this.inventory = new ItemStack[1];
    }

    public void startInfusion() {
        TileEntity[] tiles = new TileEntity[4];
        tiles[0] = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
        tiles[1] = worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
        tiles[2] = worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
        tiles[3] = worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);

        List<ItemStack> items = new ArrayList<ItemStack>();
        for (TileEntity tile : tiles) {
            if (tile instanceof TileItemAltar) {
                TileItemAltar itemAltar = (TileItemAltar) tile;
                if (itemAltar.getStackInSlot(0) != null)
                    items.add(itemAltar.getStackInSlot(0));
            }
        }
        // TODO something something something find spell that matches
        // corresponding items
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
        this.markDirty();
        return stack;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        inventory[var1] = var2;
        this.markDirty();
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

    public void read(NBTTagCompound nbt) {
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.length)
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        read(nbt);
    }

    public void write(NBTTagCompound nbt) {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag("Items", nbttaglist);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        write(nbt);
    }

    @Override
    public Packet getDescriptionPacket() {
        S35PacketUpdateTileEntity packet = (S35PacketUpdateTileEntity) super.getDescriptionPacket();
        NBTTagCompound dataTag = packet != null ? packet.func_148857_g() : new NBTTagCompound();
        write(dataTag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, dataTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt != null ? pkt.func_148857_g() : new NBTTagCompound();
        read(tag);
    }
}

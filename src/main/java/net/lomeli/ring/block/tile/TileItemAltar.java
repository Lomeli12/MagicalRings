package net.lomeli.ring.block.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.network.NetworkRegistry;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketUpdateAltar;

public class TileItemAltar extends TileEntity implements IInventory {
    protected ItemStack[] inventory;
    private int lightValue;

    public TileItemAltar() {
        this.inventory = new ItemStack[1];
    }

    public void spawnEffects() {
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + 0.5, yCoord + 1.1, zCoord + 0.5, 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + 0.5, yCoord + 1.1, zCoord + 0.5, 0, 0, 0);
        worldObj.playSound(xCoord, yCoord, zCoord, "random.pop", 0.5F, 0.4F / ((float) worldObj.rand.nextDouble() * 0.4F + 0.8F), false);
    }

    public int getLightValue() {
        return lightValue;
    }

    public void setLightValue(int i) {
        lightValue = i;
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
                setInventorySlotContents(par1, null);
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.inventory[par1].splitStack(par2);

                if (this.inventory[par1].stackSize == 0)
                    setInventorySlotContents(par1, null);

                this.markDirty();
                return itemstack;
            }
        } else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        ItemStack stack = this.getStackInSlot(var1);
        setInventorySlotContents(var1, null);
        this.markDirty();
        return stack;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        inventory[var1] = var2;
        if (var2 != null && var2.stackSize > getInventoryStackLimit())
            var2.stackSize = getInventoryStackLimit();
        if (this.worldObj != null) {
            if (!this.worldObj.isRemote) {
                ItemStack display = inventory[0];
                if (display != null)
                    lightValue = (byte) Block.getBlockFromItem(display.getItem()).getLightValue();
                else
                    lightValue = 0;

                Rings.pktHandler.sendAllAround(new PacketUpdateAltar(this, display), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 128d));
            }
        }
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
        return 64;
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
                setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
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

    public void markForRenderUpdate() {
        worldObj.func_147479_m(xCoord, yCoord, zCoord);
    }
}

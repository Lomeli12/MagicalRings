package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;

import net.lomeli.ring.block.tile.TileItemAltar;

public class PacketUpdateAltar implements IPacket {
    private int x, y, z, lightValue, itemID, stackSize, itemMeta;
    private boolean doesItemHaveNBT;
    private NBTTagCompound itemData;

    public PacketUpdateAltar() {
    }

    public PacketUpdateAltar(TileItemAltar tile, ItemStack stack) {
        x = tile.xCoord;
        y = tile.yCoord;
        z = tile.zCoord;
        lightValue = tile.getLightValue();
        if (stack != null) {
            itemID = Item.getIdFromItem(stack.getItem());
            stackSize = stack.stackSize;
            itemMeta = stack.getItemDamage();
            doesItemHaveNBT = stack.hasTagCompound();
            if (stack.hasTagCompound())
                itemData = stack.getTagCompound();
        } else {
            itemID = -1;
            stackSize = 0;
            itemMeta = 0;
            doesItemHaveNBT = false;
            itemData = null;
        }
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(lightValue);
        buffer.writeInt(itemID);
        buffer.writeInt(stackSize);
        buffer.writeInt(itemMeta);
        buffer.writeBoolean(doesItemHaveNBT);
        if (doesItemHaveNBT)
            ByteBufUtils.writeTag(buffer, itemData);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        lightValue = buffer.readInt();
        itemID = buffer.readInt();
        stackSize = buffer.readInt();
        itemMeta = buffer.readInt();
        doesItemHaveNBT = buffer.readBoolean();
        if (doesItemHaveNBT)
            itemData = ByteBufUtils.readTag(buffer);
    }

    @Override
    public void readClient(EntityPlayer player) {
        TileEntity tile = player.getEntityWorld().getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileItemAltar) {
            ((TileItemAltar) tile).setLightValue(lightValue);
            ItemStack stack = null;
            if (itemID != -1) {
                stack = new ItemStack(Item.getItemById(itemID), stackSize, itemMeta);
                if (doesItemHaveNBT && itemData != null)
                    stack.stackTagCompound = itemData;
            }

            ((TileItemAltar) tile).setInventorySlotContents(0, stack);

            FMLClientHandler.instance().getClient().theWorld.func_147451_t(x, y, z);
        }
    }

    @Override
    public void readServer() {

    }
}

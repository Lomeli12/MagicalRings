package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class PacketSavePage implements IPacket {
    private int entityID, pageNum;

    public PacketSavePage() {
    }

    public PacketSavePage(EntityPlayer player, int page) {
        this.entityID = player.getEntityId();
        this.pageNum = page;
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeInt(this.pageNum);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.pageNum = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {

    }

    @Override
    public void readServer() {
        MinecraftServer ms = MinecraftServer.getServer();
        EntityPlayer player = (EntityPlayer) ms.getEntityWorld().getEntityByID(this.entityID);
        if (player != null) {
            ItemStack itemStack = player.getCurrentEquippedItem();
            if (!itemStack.hasTagCompound()) itemStack.stackTagCompound = new NBTTagCompound();
            itemStack.getTagCompound().setInteger("LastSavedPage", this.pageNum);
        }
    }
}

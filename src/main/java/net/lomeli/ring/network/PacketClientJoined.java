package net.lomeli.ring.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

import io.netty.buffer.ByteBuf;

public class PacketClientJoined implements IPacket {
    private int entityID;

    public PacketClientJoined() {
    }

    public PacketClientJoined(EntityPlayer player) {
        this.entityID = player.getEntityId();
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(this.entityID);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.entityID = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
    }

    @Override
    public void readServer() {
        MinecraftServer ms = MinecraftServer.getServer();
        EntityPlayer player = (EntityPlayer) ms.getEntityWorld().getEntityByID(this.entityID);
        NBTTagCompound tag = MagicHandler.getMagicHandler().getPlayerTag(player);
        if (tag != null) {
            int mp = tag.getInteger(ModLibs.PLAYER_MP);
            int max = tag.getInteger(ModLibs.PLAYER_MAX);
            PacketHandler.sendTo(new PacketUpdateClient(mp, max), player);
        }
    }

}

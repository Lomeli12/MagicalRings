package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import net.lomeli.ring.core.helper.SimpleUtil;

public class PacketManaHud implements IPacket {
    private int entityID;

    public PacketManaHud() {
    }

    public PacketManaHud(EntityPlayer player) {
        entityID = player.getEntityId();
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(entityID);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        entityID = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
        if (player != null)
            SimpleUtil.changeHudSettings(player);
    }

    @Override
    public void readServer() {
        MinecraftServer ms = MinecraftServer.getServer();
        EntityPlayer player = (EntityPlayer) ms.getEntityWorld().getEntityByID(this.entityID);
        if (player != null)
            SimpleUtil.changeHudSettings(player);
    }
}

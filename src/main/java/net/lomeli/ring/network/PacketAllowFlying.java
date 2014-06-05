package net.lomeli.ring.network;

import net.minecraft.entity.player.EntityPlayer;

import net.lomeli.ring.Rings;

import io.netty.buffer.ByteBuf;

public class PacketAllowFlying implements IPacket {
    private int entityId;

    public PacketAllowFlying() {
    }

    public PacketAllowFlying(EntityPlayer player) {
        this.entityId = player.getEntityId();
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
        if (!Rings.proxy.tickHandler.flyingPlayerList.contains(this.entityId))
            Rings.proxy.tickHandler.flyingPlayerList.add(this.entityId);
    }

    @Override
    public void readServer() {
        if (!Rings.proxy.tickHandler.flyingPlayerList.contains(this.entityId)) {
            Rings.proxy.tickHandler.flyingPlayerList.add(this.entityId);
        }
    }

}

package net.lomeli.ring.network;

import net.minecraft.entity.player.EntityPlayer;

import net.lomeli.ring.Rings;

import io.netty.buffer.ByteBuf;

public class PacketRemovePlayer implements IPacket {

    private int entityId;

    public PacketRemovePlayer() {
    }

    public PacketRemovePlayer(EntityPlayer player) {
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
    }

    @Override
    public void readServer() {
        for (int i = 0; i < Rings.proxy.tickHandler.flyingPlayerList.size(); i++) {
            int j = Rings.proxy.tickHandler.flyingPlayerList.get(i);
            if (j == this.entityId)
                Rings.proxy.tickHandler.flyingPlayerList.remove(i);
        }
    }

}

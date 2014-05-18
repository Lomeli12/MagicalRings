package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.ring.Rings;
import net.minecraft.entity.player.EntityPlayer;

public class PacketRemovePlayer implements IPacket{

    private int entityId;

    public PacketRemovePlayer() {
    }

    public PacketRemovePlayer(EntityPlayer player) {
        this.entityId = player.getEntityId();
    }

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
    }

    @Override
    public void readServer(EntityPlayer player) {
        for (int i = 0; i < Rings.proxy.tickHandler.flyingPlayerList.size(); i++) {
            int j = Rings.proxy.tickHandler.flyingPlayerList.get(i);
            if (j == this.entityId)
                Rings.proxy.tickHandler.flyingPlayerList.remove(i);
        }
    }

}

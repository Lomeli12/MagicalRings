package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.ring.Rings;
import net.minecraft.entity.player.EntityPlayer;

public class PacketAllowFlying implements IPacket {
    private int entityId;

    public PacketAllowFlying() {
    }

    public PacketAllowFlying(EntityPlayer player) {
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
        if (!Rings.proxy.tickHandler.flyingPlayerList.contains(this.entityId))
            Rings.proxy.tickHandler.flyingPlayerList.add(this.entityId);
    }

    @Override
    public void readServer(EntityPlayer player) {
        if (!Rings.proxy.tickHandler.flyingPlayerList.contains(this.entityId))
            Rings.proxy.tickHandler.flyingPlayerList.add(this.entityId);
    }

}

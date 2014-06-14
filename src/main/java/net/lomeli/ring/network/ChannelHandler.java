package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket> {

    public ChannelHandler() {
        this.addDiscriminator(0, PacketAdjustClientPos.class);
        this.addDiscriminator(1, PacketAllowFlying.class);
        this.addDiscriminator(2, PacketCheckPlayerMP.class);
        this.addDiscriminator(3, PacketClientJoined.class);
        this.addDiscriminator(4, PacketStoreEntity.class);
        this.addDiscriminator(5, PacketRingName.class);
        this.addDiscriminator(6, PacketUpdateClient.class);
        this.addDiscriminator(7, PacketModifyMp.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception {
        msg.toByte(target);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket msg) {
        msg.fromByte(source);
    }

}

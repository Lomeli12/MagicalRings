package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

public class IndexCodec extends FMLIndexedMessageToMessageCodec<IPacket> {

    public IndexCodec() {
        this.addDiscriminator(0, PacketAdjustClientPos.class);
        this.addDiscriminator(1, PacketAllowFlying.class);
        this.addDiscriminator(2, PacketStoreEntity.class);
        this.addDiscriminator(3, PacketRingName.class);
        this.addDiscriminator(4, PacketSavePage.class);
        this.addDiscriminator(5, PacketUpdateAltar.class);
        this.addDiscriminator(6, PacketClearWater.class);
        this.addDiscriminator(7, PacketManaHud.class);
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

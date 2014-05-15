package net.lomeli.modjam4.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.modjam4.magic.MagicHandler;
import net.minecraft.entity.player.EntityPlayer;

public class PacketClear implements ISimplePacket {
    
    public PacketClear() {}

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
    }

    @Override
    public void readClient(EntityPlayer player) {
        MagicHandler.getMagicHandler().getList().clear();
    }

    @Override
    public void readServer(EntityPlayer player) {
    }

}

package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public interface ISimplePacket {
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer);
    
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer);
    
    public void readClient(EntityPlayer player);
    
    public void readServer(EntityPlayer player);
}

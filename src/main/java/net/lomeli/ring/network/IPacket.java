package net.lomeli.ring.network;

import net.minecraft.entity.player.EntityPlayer;

import io.netty.buffer.ByteBuf;

public interface IPacket {
    
    public void toByte(ByteBuf buffer);

    public void fromByte(ByteBuf buffer);

    public void readClient(EntityPlayer player);

    public void readServer();
}

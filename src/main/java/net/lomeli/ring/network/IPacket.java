package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;

public interface IPacket {

    public void toByte(ByteBuf buffer);

    public void fromByte(ByteBuf buffer);

    public void readClient(EntityPlayer player);

    public void readServer();
}

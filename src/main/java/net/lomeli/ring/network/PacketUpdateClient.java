package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.ring.lib.ModLibs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketUpdateClient implements IPacket {
    private int mp, max;

    public PacketUpdateClient() {
    }
    
    public PacketUpdateClient(int mp, int max) {
        this.max = max;
        this.mp = mp;
    }

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(this.max);
        buffer.writeInt(this.mp);
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.max = buffer.readInt();
        this.mp = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(ModLibs.PLAYER_MAX, this.max);
        tag.setInteger(ModLibs.PLAYER_MP, this.mp);
        player.getEntityData().setTag(ModLibs.PLAYER_DATA, tag);
    }

    @Override
    public void readServer(EntityPlayer player) {
    }

}

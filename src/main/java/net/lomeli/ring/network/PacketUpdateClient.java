package net.lomeli.ring.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import net.lomeli.ring.core.SimpleUtil;
import net.lomeli.ring.lib.ModLibs;

import io.netty.buffer.ByteBuf;

public class PacketUpdateClient implements IPacket {
    private int mp, max;

    public PacketUpdateClient() {
    }

    public PacketUpdateClient(int mp, int max) {
        this.max = max;
        this.mp = mp;
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(this.max);
        buffer.writeInt(this.mp);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.max = buffer.readInt();
        this.mp = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(ModLibs.PLAYER_MAX, this.max);
        tag.setInteger(ModLibs.PLAYER_MP, this.mp);
        SimpleUtil.addToPersistantData(player, ModLibs.PLAYER_DATA, tag);
    }

    @Override
    public void readServer() {
    }

}

package net.lomeli.ring.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import net.lomeli.ring.lib.ModLibs;

import io.netty.buffer.ByteBuf;

public class PacketModifyMp implements IPacket {

    private int id, mp, max;

    public PacketModifyMp() {
    }

    public PacketModifyMp(EntityPlayer player, int mp, int max) {
        this.id = player.getEntityId();
        this.mp = mp;
        this.max = max;
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(this.id);
        buffer.writeInt(this.mp);
        buffer.writeInt(this.max);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.id = buffer.readInt();
        this.mp = buffer.readInt();
        this.max = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
        if (player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
            NBTTagCompound tag = player.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA);
            tag.setInteger(ModLibs.PLAYER_MP, tag.getInteger(ModLibs.PLAYER_MP) + this.mp);
            tag.setInteger(ModLibs.PLAYER_MAX, tag.getInteger(ModLibs.PLAYER_MAX) + this.max);
            player.getEntityData().setTag(ModLibs.PLAYER_DATA, tag);
        }
    }

    @Override
    public void readServer() {
        update();
    }

    public void update() {
        MinecraftServer ms = MinecraftServer.getServer();
        EntityPlayer player = (EntityPlayer) ms.getEntityWorld().getEntityByID(this.id);
        if (player != null) {
            if (player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
                NBTTagCompound tag = player.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA);
                tag.setInteger(ModLibs.PLAYER_MP, tag.getInteger(ModLibs.PLAYER_MP) + this.mp);
                tag.setInteger(ModLibs.PLAYER_MAX, tag.getInteger(ModLibs.PLAYER_MAX) + this.max);
                player.getEntityData().setTag(ModLibs.PLAYER_DATA, tag);
            }
        }
        PacketHandler.sendTo(new PacketModifyMp(player, this.mp, this.max), player);
    }
}

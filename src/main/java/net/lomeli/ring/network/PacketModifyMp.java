package net.lomeli.ring.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import net.lomeli.ring.core.SimpleUtil;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

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
        NBTTagCompound tag = MagicHandler.getMagicHandler().getPlayerTag(player);

        tag = updateTag(tag);

        SimpleUtil.addToPersistantData(player, ModLibs.PLAYER_DATA, tag);
    }

    @Override
    public void readServer() {
        MinecraftServer ms = MinecraftServer.getServer();
        EntityPlayer player = (EntityPlayer) ms.getEntityWorld().getEntityByID(this.id);
        if (player != null) {
            NBTTagCompound tag = MagicHandler.getMagicHandler().getPlayerTag(player);

            tag = updateTag(tag);

            SimpleUtil.addToPersistantData(player, ModLibs.PLAYER_DATA, tag);
        }
        PacketHandler.sendTo(new PacketModifyMp(player, this.mp, this.max), player);
    }

    public NBTTagCompound updateTag(NBTTagCompound tag) {
        if (tag != null) {
            int newMP = tag.getInteger(ModLibs.PLAYER_MP) + this.mp;
            int newMax = tag.getInteger(ModLibs.PLAYER_MAX) + this.max;
            if (newMP > newMax)
                newMP = newMax;
            tag.setInteger(ModLibs.PLAYER_MP, newMP);
            tag.setInteger(ModLibs.PLAYER_MAX, newMax);
        } else {
            tag = new NBTTagCompound();
            tag.setInteger(ModLibs.PLAYER_MP, this.mp);
            tag.setInteger(ModLibs.PLAYER_MAX, this.max);
        }
        return tag;
    }
}

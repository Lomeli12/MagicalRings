package net.lomeli.ring.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

import io.netty.buffer.ByteBuf;

public class PacketCheckPlayerMP implements IPacket {
    private int entityID;

    public PacketCheckPlayerMP() {
    }

    public PacketCheckPlayerMP(EntityPlayer player) {
        this.entityID = player.getEntityId();
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(this.entityID);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.entityID = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
    }

    @Override
    public void readServer() {
        MinecraftServer ms = MinecraftServer.getServer();
        EntityPlayer pl = (EntityPlayer) ms.getEntityWorld().getEntityByID(this.entityID);
        if (pl != null) {
            if (!MagicHandler.getMagicHandler().playerHasMP(pl) || !pl.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA).hasKey(ModLibs.PLAYER_MAX))
                MagicHandler.modifyPlayerMaxMP(pl, ModLibs.BASE_MP);
            else
                MagicHandler.modifyPlayerMaxMP(pl, MagicHandler.getMagicHandler().getPlayerMaxMP(pl));
        }
    }
}

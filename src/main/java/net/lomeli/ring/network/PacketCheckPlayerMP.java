package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class PacketCheckPlayerMP implements IPacket {
    private int entityID;

    public PacketCheckPlayerMP() {
    }

    public PacketCheckPlayerMP(EntityPlayer player) {
        this.entityID = player.getEntityId();
    }

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(this.entityID);
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.entityID = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
    }

    @Override
    public void readServer(EntityPlayer player) {
        MinecraftServer ms = MinecraftServer.getServer();
        EntityPlayer pl = (EntityPlayer) ms.getEntityWorld().getEntityByID(this.entityID);
        if (pl != null) {
            if (!pl.getEntityData().hasKey(ModLibs.PLAYER_DATA) || !pl.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA).hasKey(ModLibs.PLAYER_MAX))
                MagicHandler.modifyPlayerMaxMP(pl, ModLibs.BASE_MP);
            else {
                NBTTagCompound tag = pl.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA);
                MagicHandler.modifyPlayerMaxMP(pl, tag.getInteger(ModLibs.PLAYER_MAX));
            }
        }
    }
}

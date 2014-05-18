package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class PacketIncreaseMP implements IPacket {

    private int entityID, boost;

    public PacketIncreaseMP() {
    }

    public PacketIncreaseMP(EntityPlayer player, int boost) {
        this.entityID = player.getEntityId();
        this.boost = boost;
    }

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeInt(this.boost);
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.boost = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
    }

    @Override
    public void readServer(EntityPlayer p) {
        EntityPlayer player = (EntityPlayer) MinecraftServer.getServer().getEntityWorld().getEntityByID(this.entityID);
        if (player != null) {
            if (player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
                NBTTagCompound tag = player.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA);
                int max = tag.getInteger(ModLibs.PLAYER_MAX);
                if (max < 1500)
                    MagicHandler.modifyPlayerMaxMP(player, max + this.boost);
            }
        }

    }

}

package net.lomeli.modjam4.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.modjam4.lib.ModLibs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class PacketUpdatePlayerMP implements ISimplePacket {

    private int id, mp, max;

    public PacketUpdatePlayerMP() {
    }

    public PacketUpdatePlayerMP(EntityPlayer player, int mp, int max) {
        this.id = player.getEntityId();
        this.mp = mp;
        this.max = max;
    }

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(this.id);
        buffer.writeInt(this.mp);
        buffer.writeInt(this.max);
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.id = buffer.readInt();
        this.mp = buffer.readInt();
        this.max = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
        System.out.println("running client");
        update();
    }

    @Override
    public void readServer(EntityPlayer player) {
        System.out.println("running server");
        update();
    }

    public void update() {
        MinecraftServer ms = MinecraftServer.getServer();
        EntityPlayer player = (EntityPlayer) ms.getEntityWorld().getEntityByID(this.id);
        if (player != null) {
            NBTTagCompound tag = player.getEntityData().hasKey(ModLibs.PLAYER_DATA) ? player.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA) : new NBTTagCompound();
            tag.setInteger(ModLibs.PLAYER_MP, this.mp);
            tag.setInteger(ModLibs.PLAYER_MAX, this.max);
            player.getEntityData().setTag(ModLibs.PLAYER_DATA, tag);
        }
        /*
         * for (Object obj : ms.getConfigurationManager().playerEntityList) { if
         * (obj instanceof EntityPlayer) { EntityPlayer p = (EntityPlayer) obj;
         * 
         * } }
         */
    }

}

package net.lomeli.modjam4.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.modjam4.Rings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PacketConnect implements ISimplePacket {

    private int entityId;

    public PacketConnect() {
    }

    public PacketConnect(EntityPlayer player) {
        this.entityId = player.getEntityId();
    }

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
    }

    @Override
    public void readServer(EntityPlayer player) {
        PacketUpdateClientList packet = new PacketUpdateClientList(Rings.proxy.magicHandler.getList());
        EntityPlayer pl = null;
        for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer ep0 = (EntityPlayer) obj;
                if (ep0.getEntityId() == this.entityId) {
                    pl = ep0;
                    break;
                }
            }
        }
        if (pl != null)
            PacketHandler.sendTo(packet, pl);
    }

}

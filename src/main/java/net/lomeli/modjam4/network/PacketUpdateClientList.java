package net.lomeli.modjam4.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import net.lomeli.modjam4.Rings;
import net.lomeli.modjam4.magic.PlayerMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PacketUpdateClientList implements ISimplePacket{
    
    private List<PlayerMagic> list;
    
    public PacketUpdateClientList(){}
    
    public PacketUpdateClientList(List<PlayerMagic> list) {
        this.list = list;
    }

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (list != null) {
            buffer.writeInt(list.size());
            for (PlayerMagic player : list) {
                buffer.writeInt(player.player().getEntityId());
                buffer.writeInt(player.getMax());
                buffer.writeInt(player.getMP());
            }
        }
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            int id = buffer.readInt();
            int max = buffer.readInt();
            int mp = buffer.readInt();
            EntityPlayer pl = null;
            for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                if (obj instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) obj;
                    if (player.getEntityId() == id) {
                        pl = player;
                        break;
                    }
                }
            }
            if (pl != null) {
                PlayerMagic session = new PlayerMagic(pl, mp, max);
                list.add(session);
            }
        }
    }

    @Override
    public void readClient(EntityPlayer player) {
        Rings.proxy.magicHandler.getList().clear();
        Rings.proxy.magicHandler.getList().addAll(list);
    }

    @Override
    public void readServer(EntityPlayer player) {
    }

}

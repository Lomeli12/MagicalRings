package net.lomeli.modjam4.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.modjam4.magic.MagicHandler;
import net.lomeli.modjam4.magic.PlayerMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PacketUpdatePlayerMP implements ISimplePacket{
    
    private String playerUUID;
    
    public PacketUpdatePlayerMP() {}
    
    public PacketUpdatePlayerMP(PlayerMagic mg){
        this.playerUUID = mg.player().getGameProfile().getId();
    }

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, playerUUID);
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.playerUUID = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void readClient(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void readServer(EntityPlayer player) {
        PlayerMagic pl = MagicHandler.getPlayerData(playerUUID);
        if (pl != null) {
            MinecraftServer ms = MinecraftServer.getServer();
            for (Object obj : ms.getConfigurationManager().playerEntityList) {
                if (obj instanceof EntityPlayer) {
                    EntityPlayer p = (EntityPlayer) obj;
                    if (p.getGameProfile().getId().equals(playerUUID))
                        pl.writeToNBT(p.getEntityData());
                }
            }
        }
    }

}

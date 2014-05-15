package net.lomeli.modjam4.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.modjam4.magic.PlayerMagic;
import net.minecraft.entity.player.EntityPlayer;

public class PacketUpdatePlayerMP implements ISimplePacket{
    
    private String playerUUID;
    private int mp, maxMP;
    
    public PacketUpdatePlayerMP() {}
    
    public PacketUpdatePlayerMP(PlayerMagic mg){
        this.playerUUID = mg.player().getGameProfile().getId();
        this.mp = mg.getMP();
        this.maxMP = mg.getMax();
    }

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, playerUUID);
        buffer.writeInt(mp);
        buffer.writeInt(maxMP);
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.playerUUID = ByteBufUtils.readUTF8String(buffer);
        this.mp = buffer.readInt();
        this.maxMP = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void readServer(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }

}

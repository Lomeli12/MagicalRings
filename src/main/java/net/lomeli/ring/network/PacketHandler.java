package net.lomeli.ring.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

import net.lomeli.ring.Rings;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<IPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IPacket msg) throws Exception {
        try {
            switch(FMLCommonHandler.instance().getEffectiveSide()) {
            case CLIENT :
                INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                msg.readClient(Rings.proxy.getPlayerFromNetHandler(netHandler));
                break;
            case SERVER :
                msg.readServer();
                break;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendToAll(IPacket packet) {
        Rings.packetChannels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        Rings.packetChannels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendTo(IPacket packet, EntityPlayer player) {
        Rings.packetChannels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        Rings.packetChannels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        Rings.packetChannels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToServer(IPacket packet) {
        Rings.packetChannels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        Rings.packetChannels.get(Side.CLIENT).writeAndFlush(packet);
    }

    public static void sendEverywhere(IPacket packet) {
        sendToAll(packet);
        sendToServer(packet);
    }

    public static void sendToPlayerAndServer(IPacket packet, EntityPlayer player) {
        sendToServer(packet);
        sendTo(packet, player);
    }
}
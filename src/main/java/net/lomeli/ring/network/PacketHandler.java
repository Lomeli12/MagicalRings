package net.lomeli.ring.network;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.EnumMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

import net.lomeli.ring.lib.ModLibs;

@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<IPacket> {
    private EnumMap<Side, FMLEmbeddedChannel> channel;
    private IndexCodec indexCodec;

    public PacketHandler() {
        indexCodec = new IndexCodec();
        channel = NetworkRegistry.INSTANCE.newChannel(ModLibs.MOD_ID, indexCodec, this);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IPacket msg) throws Exception {
        try {
            switch (FMLCommonHandler.instance().getEffectiveSide()) {
                case CLIENT:
                    INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                    msg.readClient(getPlayer(netHandler, FMLCommonHandler.instance().getEffectiveSide()));
                    break;
                case SERVER:
                    msg.readServer();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EntityPlayer getPlayer(INetHandler handler, Side side) {
        if (handler instanceof NetHandlerPlayServer)
            return ((NetHandlerPlayServer) handler).playerEntity;
        else if (side == Side.CLIENT) return Minecraft.getMinecraft().thePlayer;
        else return null;
    }

    public void sendToAll(IPacket packet) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channel.get(Side.SERVER).writeAndFlush(packet);
    }

    public void sendTo(IPacket packet, EntityPlayer player) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channel.get(Side.SERVER).writeAndFlush(packet);
    }

    public void sendToServer(IPacket packet) {
        channel.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channel.get(Side.CLIENT).writeAndFlush(packet);
    }

    public void sendAllAround(IPacket packet, NetworkRegistry.TargetPoint point) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channel.get(Side.SERVER).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    public void sendEverywhere(IPacket packet) {
        sendToAll(packet);
        sendToServer(packet);
    }

    public void sendToPlayerAndServer(IPacket packet, EntityPlayer player) {
        sendToServer(packet);
        sendTo(packet, player);
    }
}
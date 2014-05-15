package net.lomeli.modjam4.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.lomeli.modjam4.Rings;
import net.lomeli.modjam4.lib.ModLibs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;

@ChannelHandler.Sharable
public class PacketHandler extends MessageToMessageCodec<FMLProxyPacket, ISimplePacket> {
    public EnumMap<Side, FMLEmbeddedChannel> channels;
    protected LinkedList<Class<? extends ISimplePacket>> packets = new LinkedList<Class<? extends ISimplePacket>>();
    private boolean isInitialized = false;

    public PacketHandler(Class<? extends ISimplePacket>... msg) {
        for (Class<? extends ISimplePacket> pkt : msg) {
            this.registerPacket(pkt);
        }
        this.channels = NetworkRegistry.INSTANCE.newChannel(ModLibs.MOD_ID, this);
    }

    public void init() {
        if (this.isInitialized)
            return;
        this.isInitialized = true;
        Collections.sort(this.packets, new Comparator<Class<? extends ISimplePacket>>() {
            @Override
            public int compare(Class<? extends ISimplePacket> cl0, Class<? extends ISimplePacket> cl1) {
                int com = String.CASE_INSENSITIVE_ORDER.compare(cl0.getCanonicalName(), cl1.getCanonicalName());
                if (com == 0)
                    com = cl0.getCanonicalName().compareTo(cl1.getCanonicalName());
                return com;
            }
        });
    }

    public void registerPacket(Class<? extends ISimplePacket> clazz) {
        if (this.packets.size() > 256)
            return;
        if (this.packets.contains(clazz))
            return;
        if (this.isInitialized)
            return;
        this.packets.add(clazz);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ISimplePacket msg, List<Object> out) throws Exception {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends ISimplePacket> clazz = msg.getClass();
        if (!this.packets.contains(clazz))
            return;
        byte b0 = (byte) this.packets.indexOf(clazz);
        buffer.writeByte(b0);
        msg.toByte(ctx, buffer);
        FMLProxyPacket proxy = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxy);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.payload();
        byte b0 = payload.readByte();
        Class<? extends ISimplePacket> clazz = this.packets.get(b0);
        if (clazz == null)
            return;
        ISimplePacket packet = clazz.newInstance();
        packet.fromByte(ctx, payload);
        EntityPlayer player;
        switch(FMLCommonHandler.instance().getEffectiveSide()) {
        case CLIENT :
            player = FMLClientHandler.instance().getClientPlayerEntity();
            packet.readClient(player);
            break;
        case SERVER :
            INetHandler net = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
            player = ((NetHandlerPlayServer) net).playerEntity;
            packet.readServer(player);
            break;
        }
        out.add(packet);
    }
    
    public static void sendToAll(ISimplePacket packet) {
        Rings.packetHandler.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        Rings.packetHandler.channels.get(Side.SERVER).writeAndFlush(packet);
    }
    
    public static void sendTo(ISimplePacket packet, EntityPlayer player) {
        Rings.packetHandler.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        Rings.packetHandler.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        Rings.packetHandler.channels.get(Side.SERVER).writeAndFlush(packet);
    }
    
    public static void sendToServer(ISimplePacket packet) {
        Rings.packetHandler.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        Rings.packetHandler.channels.get(Side.SERVER).writeAndFlush(packet);
    }
    
    public static void sendEverywhere(ISimplePacket packet) {
        sendToAll(packet);
        sendToServer(packet);
    }
}

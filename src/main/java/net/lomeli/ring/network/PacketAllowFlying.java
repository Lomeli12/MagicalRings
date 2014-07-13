package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PacketAllowFlying implements IPacket {
    private int entityId;
    private boolean allowFlying;

    public PacketAllowFlying() {
    }

    public PacketAllowFlying(EntityPlayer player, boolean allow) {
        this.entityId = player.getEntityId();
        this.allowFlying = allow;
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
        updateTag(player, this.allowFlying);
    }

    @Override
    public void readServer() {
        MinecraftServer ms = MinecraftServer.getServer();
        EntityPlayer player = (EntityPlayer) ms.getEntityWorld().getEntityByID(this.entityId);
        if (player != null)
            updateTag(player, this.allowFlying);
    }

    public void updateTag(EntityPlayer player, boolean val) {
        if (player != null)
            player.capabilities.allowFlying = val;
    }
}

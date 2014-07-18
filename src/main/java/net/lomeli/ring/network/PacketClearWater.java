package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;

import net.lomeli.ring.Rings;
import net.lomeli.ring.client.handler.RenderHandler;

public class PacketClearWater implements IPacket {
    private boolean clear;

    public PacketClearWater() {
    }

    public PacketClearWater(boolean clear) {
        this.clear = clear;
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeBoolean(this.clear);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.clear = buffer.readBoolean();
    }

    @Override
    public void readClient(EntityPlayer player) {
        Rings.proxy.renderHandler.clearFog = this.clear;
    }

    @Override
    public void readServer() {

    }
}

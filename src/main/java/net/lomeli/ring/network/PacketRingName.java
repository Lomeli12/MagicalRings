package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.network.ByteBufUtils;

import net.lomeli.ring.block.tile.TileRingForge;

public class PacketRingName implements IPacket {
    private String name;
    private int x, y, z;

    public PacketRingName() {
    }

    public PacketRingName(String name, int x, int y, int z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void toByte(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, this.name);
        buffer.writeInt(this.x);
        buffer.writeInt(this.y);
        buffer.writeInt(this.z);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.name = ByteBufUtils.readUTF8String(buffer);
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
    }

    @Override
    public void readServer() {
        setName();
    }

    public void setName() {
        MinecraftServer mc = MinecraftServer.getServer();
        TileEntity tile = mc.getEntityWorld().getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRingForge) {
            TileRingForge forge = (TileRingForge) tile;
            forge.setName(name);
        }
    }

}

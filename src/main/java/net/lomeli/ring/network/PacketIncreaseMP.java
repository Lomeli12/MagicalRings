package net.lomeli.ring.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

import io.netty.buffer.ByteBuf;

public class PacketIncreaseMP implements IPacket {

    private int entityID, boost;

    public PacketIncreaseMP() {
    }

    public PacketIncreaseMP(EntityPlayer player, int boost) {
        this.entityID = player.getEntityId();
        this.boost = boost;
    }

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeInt(this.boost);
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.boost = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
    }

    @Override
    public void readServer() {
        EntityPlayer player = (EntityPlayer) MinecraftServer.getServer().getEntityWorld().getEntityByID(this.entityID);
        if (player != null) {
            if (player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
                int max = MagicHandler.getMagicHandler().getPlayerMaxMP(player);
                if (max < 1500)
                    MagicHandler.modifyPlayerMaxMP(player, max + this.boost);
            }
        }

    }

}

package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketStoreEntity implements IPacket {
    private int playerID;
    private boolean addEntity;
    private String entityClass;
    private NBTTagCompound entityTag;

    public PacketStoreEntity(EntityPlayer player, boolean addEntity) {
        this(player, addEntity, null, null);
    }

    public PacketStoreEntity(EntityPlayer player, boolean addEntity, String clazz, NBTTagCompound tag) {
        this.playerID = player.getEntityId();
        this.addEntity = addEntity;
        if (addEntity) {
            this.entityClass = clazz;
            this.entityTag = tag != null ? tag : new NBTTagCompound();
        }
    }

    public PacketStoreEntity(){}

    @Override
    public void toByte(ByteBuf buffer) {
        buffer.writeInt(this.playerID);
        buffer.writeBoolean(this.addEntity);
        if (this.addEntity) {
            ByteBufUtils.writeUTF8String(buffer, this.entityClass);
            ByteBufUtils.writeTag(buffer, this.entityTag);
        }
    }

    @Override
    public void fromByte(ByteBuf buffer) {
        this.playerID = buffer.readInt();
        this.addEntity = buffer.readBoolean();
        if (this.addEntity) {
            this.entityClass = ByteBufUtils.readUTF8String(buffer);
            this.entityTag = ByteBufUtils.readTag(buffer);
        }
    }

    @Override
    public void readClient(EntityPlayer player) {
    }

    @Override
    public void readServer() {
        MinecraftServer ms = MinecraftServer.getServer();
        EntityPlayer player = (EntityPlayer) ms.getEntityWorld().getEntityByID(this.playerID);
        if (player != null) {
            if (this.addEntity) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("EntityClass", this.entityClass);
                tag.setTag("EntityData", this.entityTag);
                player.getCurrentEquippedItem().getTagCompound().setTag("EntityData", tag);
            } else {
                if (player.getCurrentEquippedItem().getTagCompound().hasKey("EntityData"))
                    player.getCurrentEquippedItem().getTagCompound().removeTag("EntityData");
            }
        }
    }
}

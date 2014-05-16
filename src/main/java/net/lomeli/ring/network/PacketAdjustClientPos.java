package net.lomeli.ring.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

public class PacketAdjustClientPos implements ISimplePacket {

    private int x, y;

    public PacketAdjustClientPos() {
    }

    public PacketAdjustClientPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void toByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(this.x);
        buffer.writeInt(this.y);
    }

    @Override
    public void fromByte(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
    }

    @Override
    public void readClient(EntityPlayer player) {
        Configuration config = new Configuration(Rings.modConfig);

        config.load();

        String clientOptions = "clientOptions";
        ConfigCategory cat = config.getCategory(clientOptions) != null ? config.getCategory(clientOptions) : new ConfigCategory(clientOptions);
        cat.setComment("Change the x and y position where your MP is displayed.");

        Property propX = new Property("displayX", this.x + "", Type.INTEGER);
        propX.set(this.x);
        Property propY = new Property("displayY", this.y + "", Type.INTEGER);
        propY.set(this.y);
        cat.put("displayX", propX);
        cat.put("displayY", propY);
        
        ModLibs.DISPLAY_X = this.x;
        ModLibs.DISPLAY_Y = this.y;

        config.save();
    }

    @Override
    public void readServer(EntityPlayer player) {
    }

}

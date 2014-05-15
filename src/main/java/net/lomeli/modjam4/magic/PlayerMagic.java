package net.lomeli.modjam4.magic;

import net.lomeli.modjam4.lib.ModLibs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerMagic {
    private int mp, max;
    private EntityPlayer player;

    public PlayerMagic() {
        this(null, 0, 0);
    }

    public PlayerMagic(EntityPlayer player, int mp, int max) {
        this.player = player;
        this.mp = mp;
        this.max = max;
    }

    public void writeToNBT(NBTTagCompound tag) {
        NBTTagCompound modTag = new NBTTagCompound();
        modTag.setInteger("MP", this.mp);
        modTag.setInteger("MaxMP", this.max);
        tag.setTag(ModLibs.MOD_ID + "_Data", modTag);
    }

    public void readFromNBT(NBTTagCompound tag) {
        NBTTagCompound modTag = tag.getCompoundTag(ModLibs.MOD_ID + "_Data");
        if (modTag == null) {
            this.mp = 0;
            this.max = ModLibs.BASE_MP;
        }else {
            this.mp = modTag.getInteger("MP");
            this.max = modTag.getInteger("MaxMP");
        }
    }

    public void sendPacket() {

    }

    public int getMP() {
        return this.mp;
    }

    public int getMax() {
        return this.max;
    }

    public EntityPlayer player() {
        return this.player;
    }
}

package net.lomeli.ring.magic.spells;

import java.lang.reflect.Constructor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketStoreEntity;

public class BeingDisplacement implements ISpell {
    @Override
    public boolean useOnBlock(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        if (!world.isRemote) {
            if (player.getCurrentEquippedItem().getTagCompound() != null && MagicHandler.canUse(player, cost() / 2) && !world.isAirBlock(x, y, z)) {
                if (player.getCurrentEquippedItem().getTagCompound().hasKey("EntityData")) {
                    NBTTagCompound tag = player.getCurrentEquippedItem().getTagCompound().getCompoundTag("EntityData");
                    try {
                        Class<?> clazz = Class.forName(tag.getString("EntityClass"));
                        Constructor<?> cont = clazz.getConstructor(World.class);
                        Object obj = cont.newInstance(world);
                        if (obj instanceof EntityLivingBase) {
                            EntityLivingBase entity = (EntityLivingBase) obj;
                            if (tag.getCompoundTag("EntityData") != null)
                                entity.readEntityFromNBT(tag.getCompoundTag("EntityData"));
                            if (entity != null) {
                                entity.setPosition(x + 0.5, y + 1, z + 0.5);
                                world.spawnEntityInWorld(entity);
                                MagicHandler.modifyPlayerMP(player, -(cost() / 2));
                                PacketHandler.sendToServer(new PacketStoreEntity(player, false));
                                return true;
                            }
                        }
                    } catch (Exception e) {
                    }
                    PacketHandler.sendToServer(new PacketStoreEntity(player, false));
                }
            }
        }
        return false;
    }

    @Override
    public void onUse(World world, EntityPlayer player, ItemStack stack, int boost, int cost) {

    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {

    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity) {

    }

    @Override
    public void applyToMob(EntityPlayer player, Entity target, int boost, int cost) {
        if (!player.getEntityWorld().isRemote) {
            if (player.getCurrentEquippedItem().getTagCompound() != null && (target instanceof EntityLivingBase && !(target instanceof EntityPlayer)) && MagicHandler.canUse(player, cost())) {
                if (!player.getCurrentEquippedItem().getTagCompound().hasKey("EntityData")) {
                    NBTTagCompound tag = new NBTTagCompound();
                    ((EntityLivingBase) target).writeEntityToNBT(tag);
                    PacketHandler.sendToServer(new PacketStoreEntity(player, true, target.getClass().getName(), tag));
                    target.setDead();
                    MagicHandler.modifyPlayerMP(player, -cost());
                }
            }
        }
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost, boolean enabled) {
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.DISPLACE;
    }

    @Override
    public int cost() {
        return 100;
    }
}

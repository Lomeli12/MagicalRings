package net.lomeli.ring.magic.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.lomeli.ring.network.PacketAllowFlying;
import net.lomeli.ring.network.PacketHandler;

public class SwiftWind implements ISpell {

    @Override
    public boolean useOnBlock(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
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
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!player.capabilities.isCreativeMode) {
                boolean canFlyNow = player.capabilities.allowFlying;
                if (player.capabilities.isFlying)
                    player.capabilities.isFlying = false;
                if (player.capabilities.allowFlying)
                    player.capabilities.allowFlying = false;
                if (canFlyNow != player.capabilities.allowFlying)
                    PacketHandler.sendToServer(new PacketAllowFlying(player, player.capabilities.allowFlying));
            }
        }
    }

    @Override
    public void applyToMob(EntityPlayer player, Entity target, int boost, int cost) {
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost, boolean enabled) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (MagicHandler.getMagicHandler().canPlayerUseMagic(player)) {
                if (!player.capabilities.isCreativeMode) {
                    boolean canFlyNow = player.capabilities.allowFlying;
                    if (MagicHandler.canUse(player, cost())) {
                        if (player.capabilities.allowFlying == false)
                            player.capabilities.allowFlying = true;

                        if (player.capabilities.isFlying) {
                            MagicHandler.modifyPlayerMP(player, -cost());
                        } else if (player.fallDistance >= 4F) {
                            MagicHandler.modifyPlayerMP(player, -((int) (player.fallDistance / 4)));
                        }
                    } else {
                        if (player.capabilities.allowFlying == true)
                            player.capabilities.allowFlying = false;
                        if (player.capabilities.isFlying == true)
                            player.capabilities.isFlying = false;
                    }
                    if (canFlyNow != player.capabilities.allowFlying)
                        PacketHandler.sendToServer(new PacketAllowFlying(player, player.capabilities.allowFlying));
                }
            }
        }
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.WING;
    }

    @Override
    public int cost() {
        return 1;
    }

}

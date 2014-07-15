package net.lomeli.ring.magic.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketAllowFlying;

public class SwiftWind implements ISpell {
    private int tick;

    @Override
    public boolean useOnBlock(World world, EntityPlayer player, IPlayerSession session, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        return false;
    }

    @Override
    public void onUse(World world, EntityPlayer player, IPlayerSession session, ItemStack stack, int boost, int cost) {

    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {

    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!player.capabilities.isCreativeMode) {
                player.capabilities.isFlying = false;
                player.capabilities.allowFlying = false;
                Rings.pktHandler.sendToPlayerAndServer(new PacketAllowFlying(player, false), player);
            }
        }
    }

    @Override
    public void applyToMob(EntityPlayer player, IPlayerSession session, Entity target, int boost, int cost) {
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, IPlayerSession session, int par4, boolean par5, int boost, int cost, boolean enabled) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (session != null) {
                if (!player.capabilities.isCreativeMode) {
                    boolean canFlyNow = player.capabilities.allowFlying;
                    if (session.hasEnoughMana(cost())) {
                        if (player.capabilities.allowFlying == false)
                            player.capabilities.allowFlying = true;

                        if (++tick >= 10) {
                            if (player.capabilities.isFlying)
                                session.adjustMana(-cost(), false);
                            else if (player.fallDistance >= 4F)
                                session.adjustMana(-MathHelper.floor_float(player.fallDistance / 4), false);
                            tick = 0;
                        }
                    } else {
                        if (player.capabilities.allowFlying == true)
                            player.capabilities.allowFlying = false;
                        if (player.capabilities.isFlying == true)
                            player.capabilities.isFlying = false;
                    }
                    if (canFlyNow != player.capabilities.allowFlying)
                        Rings.pktHandler.sendToServer(new PacketAllowFlying(player, player.capabilities.allowFlying));
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
        return 2;
    }

    @Override
    public String getSpellDescription() {
        return ModLibs.WING + "Desc";
    }
}

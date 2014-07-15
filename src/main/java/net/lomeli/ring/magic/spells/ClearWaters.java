package net.lomeli.ring.magic.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.client.handler.RenderHandler;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketClearWater;

public class ClearWaters implements ISpell {
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
        if (entity instanceof EntityPlayer)
            Rings.pktHandler.sendTo(new PacketClearWater(true), (EntityPlayer) entity);
    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {
        if (entity instanceof EntityPlayer)
            Rings.pktHandler.sendTo(new PacketClearWater(false), (EntityPlayer) entity);
    }

    @Override
    public void applyToMob(EntityPlayer player, IPlayerSession session, Entity target, int boost, int cost) {

    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, IPlayerSession session, int par4, boolean par5, int boost, int cost, boolean enabled) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (session != null && player.isInWater()) {
                if (session.hasEnoughMana(cost())) {
                    if (world.isRemote) {
                        if (!RenderHandler.clearFog)
                            Rings.pktHandler.sendTo(new PacketClearWater(true), player);
                    }
                    if (++tick >= 40) {
                        session.adjustMana(-cost(), false);
                        tick = 0;

                    }
                } else
                    Rings.pktHandler.sendTo(new PacketClearWater(false), player);
            }
        }
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.CLEAR_WATER;
    }

    @Override
    public String getSpellDescription() {
        return ModLibs.CLEAR_WATER + "Desc";
    }

    @Override
    public int cost() {
        return 5;
    }
}

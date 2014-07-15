package net.lomeli.ring.magic.spells;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.lib.ModLibs;

public class FriendlyFire implements ISpell {

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

    }

    @Override
    public void applyToMob(EntityPlayer player, IPlayerSession session, Entity target, int boost, int cost) {
        if (!player.worldObj.isRemote) {
            if (target instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) target;
                if (target.boundingBox != null) {
                    List<?> entityList = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, target.boundingBox.expand(16, 16, 15));
                    if (entityList != null) {
                        Object obj = entityList.get(player.worldObj.rand.nextInt(entityList.size()));
                        if (obj instanceof EntityLivingBase) {
                            EntityLivingBase newTarget = (EntityLivingBase) obj;
                            if (session.hasEnoughMana(cost)) {
                                session.adjustMana(-cost, false);
                                if (living instanceof EntityCreature)
                                    ((EntityCreature) living).setAttackTarget(newTarget.equals(living) ? null : newTarget);
                                else
                                    living.setRevengeTarget((EntityLivingBase) obj);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, IPlayerSession session, int par4, boolean par5, int boost, int cost, boolean bool) {
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.FRIENDLY_FIRE;
    }

    @Override
    public int cost() {
        return 100;
    }

    @Override
    public String getSpellDescription() {
        return ModLibs.FRIENDLY_FIRE + "Desc";
    }
}

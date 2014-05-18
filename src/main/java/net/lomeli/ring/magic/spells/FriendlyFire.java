package net.lomeli.ring.magic.spells;

import java.util.List;

import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.ISpell;
import net.lomeli.ring.magic.MagicHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FriendlyFire implements ISpell {

    @Override
    public boolean activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        return false;
    }

    @Override
    public void applyToMob(EntityPlayer player, Entity target, int boost, int cost) {
        if (!player.worldObj.isRemote) {
            if (target instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) target;
                if (target.boundingBox != null) {
                    List<?> entityList = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, target.boundingBox.expand(16, 16, 15));
                    if (entityList != null) {
                        Object obj = entityList.get(player.worldObj.rand.nextInt(entityList.size()));
                        if (obj instanceof EntityLivingBase) {
                            if (MagicHandler.canUse(player, cost)) {
                                MagicHandler.modifyPlayerMP(player, -cost);
                                if (living instanceof EntityCreature)
                                    ((EntityCreature) living).setAttackTarget((EntityLivingBase) obj);
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
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost) {
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.FRIENDLY_FIRE;
    }

    @Override
    public int cost() {
        return 150;
    }

}

package net.lomeli.ring.magic.spells;

import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.ISpell;
import net.lomeli.ring.magic.MagicHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AngelKiss implements ISpell {

    @Override
    public boolean activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        if (MagicHandler.canUse(player, cost)) {
            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(5 + boost);
                MagicHandler.modifyPlayerMP(player, -cost);
            }
        }
        return false;
    }

    @Override
    public void applyToMob(EntityPlayer player, Entity target, int boost, int cost) {
        if (MagicHandler.canUse(player, cost)) {
            if (target instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) target;
                if (living.getHealth() < living.getMaxHealth()) {
                    MagicHandler.modifyPlayerMP(player, -cost);
                    living.heal(5 + boost);
                }
            }
        }
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost) {
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.ANGEL_KISS;
    }

    @Override
    public int cost() {
        return 50;
    }

}

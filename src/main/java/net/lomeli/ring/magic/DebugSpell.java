package net.lomeli.ring.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import net.lomeli.ring.api.ISpell;

public class DebugSpell implements ISpell {

    @Override
    public boolean activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1000, 1000));
        return false;
    }

    @Override
    public String getUnlocalizedName() {
        return "debug";
    }

    @Override
    public int cost() {
        return 80;
    }

    @Override
    public void applyToMob(EntityPlayer player, Entity target, int boost, int cost) {
        target.motionY += 0.5f;
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost, boolean bool) {
        if (bool) {
            // NBTTagCompound tag =
            // stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) entity;
                if (!living.isPotionActive(Potion.nightVision)) {
                    if (living instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) living;
                        if (MagicHandler.canUse(player, cost)) {
                            MagicHandler.modifyPlayerMP(player, -cost);
                            player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1000, 1000));
                        }
                    }else
                        living.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1000, 1000));
                }
            }
        }
    }

}

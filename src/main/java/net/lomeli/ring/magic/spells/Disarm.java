package net.lomeli.ring.magic.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.lib.ModLibs;

public class Disarm implements ISpell {

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
        if (session.hasEnoughMana(cost())) {
            if (target instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) target;
                if (living instanceof EntityPlayer) {
                    EntityPlayer pl = (EntityPlayer) living;
                    pl.dropOneItem(true);
                    session.adjustMana(-cost(), false);
                }
                if (living instanceof EntityCreature) {
                    EntityCreature creature = (EntityCreature) living;
                    if (creature.getHeldItem() != null) {
                        EntityItem item = new EntityItem(player.worldObj, creature.posX, creature.posY, creature.posZ, creature.getHeldItem().copy());
                        if (!player.worldObj.isRemote)
                            player.worldObj.spawnEntityInWorld(item);
                        creature.setCurrentItemOrArmor(0, null);
                        session.adjustMana(-cost(), false);
                    }
                }
            }
        }
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, IPlayerSession session, int par4, boolean par5, int boost, int cost, boolean enabled) {
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.DISARM;
    }

    @Override
    public int cost() {
        return 50;
    }

    @Override
    public String getSpellDescription() {
        return ModLibs.DISARM + "Desc";
    }

}

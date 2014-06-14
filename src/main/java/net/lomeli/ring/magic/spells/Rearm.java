package net.lomeli.ring.magic.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

public class Rearm implements ISpell {

    @Override
    public boolean useOnBlock(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {

        return false;
    }

    @Override
    public void onUse(World world, EntityPlayer player, ItemStack stack, int boost, int cost) {
        if (MagicHandler.canUse(player, cost())) {
            ItemStack item = new ItemStack(ModItems.ghostSword);
            player.inventory.addItemStackToInventory(item);
            MagicHandler.modifyPlayerMP(player, -cost());
        }
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {

    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity) {

    }

    @Override
    public void applyToMob(EntityPlayer player, Entity target, int boost, int cost) {
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost, boolean enabled) {
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.REARM;
    }

    @Override
    public int cost() {
        return 50;
    }

}

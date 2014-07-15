package net.lomeli.ring.magic.spells;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.lib.ModLibs;

public class HeavenStrike implements ISpell {

    @Override
    public boolean useOnBlock(World world, EntityPlayer player, IPlayerSession session, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        return false;
    }

    @Override
    public void onUse(World world, EntityPlayer player, IPlayerSession session, ItemStack stack, int boost, int cost) {
        if (session.hasEnoughMana(cost())) {
            MovingObjectPosition mop = SimpleUtil.rayTrace(player, world, 43);
            if (mop != null) {
                if (mop.typeOfHit == MovingObjectType.BLOCK) {
                    int blockX = mop.blockX;
                    int blockY = mop.blockY;
                    int blockZ = mop.blockZ;
                    Block block = world.getBlock(blockX, blockY, blockZ);
                    if (block != null && !world.isAirBlock(blockX, blockY, blockZ)) {
                        EntityLightningBolt light = new EntityLightningBolt(world, blockX, blockY, blockZ);
                        world.spawnEntityInWorld(light);
                        session.adjustMana(-cost(), false);
                    }
                }
            }
        }
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {

    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {

    }

    @Override
    public void applyToMob(EntityPlayer player, IPlayerSession session, Entity target, int boost, int cost) {
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, IPlayerSession session, int par4, boolean par5, int boost, int cost, boolean bool) {
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.THUNDER_SKY;
    }

    @Override
    public int cost() {
        return 45;
    }

    @Override
    public String getSpellDescription() {
        return ModLibs.THUNDER_SKY + "Desc";
    }
}

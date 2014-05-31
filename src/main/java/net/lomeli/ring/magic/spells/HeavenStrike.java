package net.lomeli.ring.magic.spells;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.core.RayTraceHelper;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

public class HeavenStrike implements ISpell {

    @Override
    public boolean activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        if (MagicHandler.canUse(player, cost())) {
            MovingObjectPosition mop = RayTraceHelper.getRayTrace(player, world);
            if (mop != null) {
                if (mop.typeOfHit == MovingObjectType.BLOCK) {
                    int blockX = mop.blockX;
                    int blockY = mop.blockY;
                    int blockZ = mop.blockZ;
                    Block block = world.getBlock(blockX, blockY, blockZ);
                    if (block != null && !world.isAirBlock(blockX, blockY, blockZ)) {
                        EntityLightningBolt light = new EntityLightningBolt(world, blockX, blockY, blockZ);
                        world.spawnEntityInWorld(light);
                        MagicHandler.modifyPlayerMP(player, -cost());
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void applyToMob(EntityPlayer player, Entity target, int boost, int cost) {
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost, boolean bool) {
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.THUNDER_SKY;
    }

    @Override
    public int cost() {
        return 90;
    }

}

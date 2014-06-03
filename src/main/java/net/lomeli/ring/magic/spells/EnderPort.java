package net.lomeli.ring.magic.spells;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.core.RayTraceHelper;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

public class EnderPort implements ISpell {
    private Random rand = new Random();

    @Override
    public boolean activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        if (MagicHandler.canUse(player, cost())) {
            MovingObjectPosition mop = RayTraceHelper.rayTrace(player, world);
            if (mop != null) {
                if (mop.typeOfHit == MovingObjectType.BLOCK) {
                    int newX = mop.blockX;
                    int newY = mop.blockY;
                    int newZ = mop.blockZ;
                    double distance = player.getDistance(newX, newY, newZ);
                    if (distance <= 42) {
                        Block blk = world.getBlock(newX, newY, newZ);
                        if (blk != null && !world.isAirBlock(newX, newY, newZ)) {
                            MagicHandler.modifyPlayerMP(player, -cost());

                            teleportTo(player, newX, newY + 2, newZ);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void applyToMob(EntityPlayer player, Entity target, int boost, int cost) {
        if (MagicHandler.canUse(player, cost)) {
            if (target instanceof EntityLivingBase) {
                teleportRandomly((EntityLivingBase) target);
                MagicHandler.modifyPlayerMP(player, -cost);
            }
        }
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost, boolean bool) {
        if (entity instanceof EntityLivingBase) {
            if (!(entity instanceof EntityPlayer))
                this.teleportRandomly((EntityLivingBase) entity);
        }
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.ENDERPORT;
    }

    @Override
    public int cost() {
        return 60;
    }

    protected boolean teleportRandomly(EntityLivingBase entity) {
        double d0 = entity.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = entity.posY + (this.rand.nextInt(64) - 32);
        double d2 = entity.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(entity, d0, d1, d2);
    }

    protected boolean teleportTo(EntityLivingBase entity, double d0, double d1, double d2) {
        double var7 = entity.posX;
        double var9 = entity.posY;
        double var11 = entity.posZ;
        entity.posX = d0;
        entity.posY = d1;
        entity.posZ = d2;
        boolean var13 = false;
        int var14 = MathHelper.floor_double(entity.posX);
        int var15 = MathHelper.floor_double(entity.posY);
        int var16 = MathHelper.floor_double(entity.posZ);
        Block var18;

        if (entity.worldObj.blockExists(var14, var15, var16)) {
            boolean var17 = false;

            while (!var17 && var15 > 0) {
                var18 = entity.worldObj.getBlock(var14, var15 - 1, var16);

                if (var18 != null && var18.getMaterial().blocksMovement())
                    var17 = true;
                else {
                    --entity.posY;
                    --var15;
                }
            }

            if (var17) {
                if (!entity.worldObj.isRemote)
                    entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

                if (entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty() && !entity.worldObj.isAnyLiquid(entity.boundingBox))
                    var13 = true;
            }
        }

        if (!var13) {
            if (!entity.worldObj.isRemote)
                entity.setPositionAndUpdate(var7, var9, var11);
            return false;
        }else {
            short var30 = 128;

            for (int j = 0; j < var30; ++j) {
                double var19 = j / (var30 - 1.0D);
                float var21 = (entity.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float var22 = (entity.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float var23 = (entity.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                double var24 = var7 + (entity.posX - var7) * var19 + (entity.worldObj.rand.nextDouble() - 0.5D) * entity.width * 2.0D;
                double var26 = var9 + (entity.posY - var9) * var19 + entity.worldObj.rand.nextDouble() * entity.height;
                double var28 = var11 + (entity.posZ - var11) * var19 + (entity.worldObj.rand.nextDouble() - 0.5D) * entity.width * 2.0D;
                entity.worldObj.spawnParticle("portal", var24, var26, var28, var21, var22, var23);
            }

            entity.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
            entity.playSound("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
        /*
         * EnderTeleportEvent event = new EnderTeleportEvent(entity, d0, d1, d2,
         * 0); if (MinecraftForge.EVENT_BUS.post(event)) return false; double
         * oldX = entity.posX; double oldY = entity.posY; double oldZ =
         * entity.posZ; double newX = d0; double newY = d1; double newZ = d2; if
         * (!entity.worldObj.isAirBlock((int) d0, (int) (d1 + (entity.height /
         * 2)), (int) d2)) { newX = d0 + (this.rand.nextDouble() + 0.5D); newZ =
         * d2 + (this.rand.nextDouble() + 0.5D); } entity.setPosition(newX,
         * newY, newZ); float f = (rand.nextFloat() - 0.5F) * 0.2F; float f1 =
         * (rand.nextFloat() - 0.5F) * 0.2F; float f2 = (rand.nextFloat() -
         * 0.5F) * 0.2F; entity.worldObj.spawnParticle("portal", newX, newY,
         * newZ, f, f1, f2); entity.worldObj.spawnParticle("portal", oldX, oldY,
         * oldZ, f, f1, f2); entity.worldObj.playSoundEffect(newX, newY, newZ,
         * "mob.endermen.portal", 1.0F, 1.0F);
         * entity.playSound("mob.endermen.portal", 1.0F, 1.0F); return true;
         */
    }
}

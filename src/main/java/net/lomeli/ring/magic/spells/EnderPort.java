package net.lomeli.ring.magic.spells;

import java.util.Random;

import net.lomeli.ring.core.RayTraceHelper;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.ISpell;
import net.lomeli.ring.magic.MagicHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EnderPort implements ISpell {
    private Random rand = new Random();

    @Override
    public boolean activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        if (MagicHandler.canUse(player, cost())) {
            MovingObjectPosition mop = RayTraceHelper.getRayTrace(player, world);
            if (mop != null) {
                if (mop.typeOfHit == MovingObjectType.BLOCK) {
                    int newX = mop.blockX;
                    int newY = mop.blockY;
                    int newZ = mop.blockZ;
                    MagicHandler.modifyPlayerMP(player, -cost());

                    teleportTo(player, newX, newY + 3, newZ);
                    return true;
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
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost) {
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
        double d1 = entity.posY + (double) (this.rand.nextInt(64) - 32);
        double d2 = entity.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(entity, d0, d1, d2);
    }

    protected boolean teleportTo(EntityLivingBase entity, double d0, double d1, double d2) {
        EnderTeleportEvent event = new EnderTeleportEvent(entity, d0, d1, d2, 0);
        if (MinecraftForge.EVENT_BUS.post(event))
            return false;
        double oldX = entity.posX;
        double oldY = entity.posY;
        double oldZ = entity.posZ;
        double newX = d0;
        double newY = d1;
        double newZ = d2;
        if (!entity.worldObj.isAirBlock((int)d0, (int)(d1 + (entity.height/2)), (int)d2)) {
            newX = d0 + (this.rand.nextDouble() + 0.5D);
            newZ = d2 + (this.rand.nextDouble() + 0.5D);
        }
        entity.setPosition(newX, newY, newZ);
        float f = (rand.nextFloat() - 0.5F) * 0.2F;
        float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
        float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
        entity.worldObj.spawnParticle("portal", newX, newY, newZ, f, f1, f2);
        entity.worldObj.spawnParticle("portal", oldX, oldY, oldZ, f, f1, f2);
        entity.worldObj.playSoundEffect(newX, newY, newZ, "mob.endermen.portal", 1.0F, 1.0F);
        entity.playSound("mob.endermen.portal", 1.0F, 1.0F);
        return true;
    }
}

package net.lomeli.ring.magic.spells;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.lib.ModLibs;

public class EnderPort implements ISpell {
    private Random rand = new Random();

    @Override
    public boolean useOnBlock(World world, EntityPlayer player, IPlayerSession session, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        onUse(world, player, session, player.getCurrentEquippedItem(), boost, cost);
        return false;
    }

    @Override
    public void onUse(World world, EntityPlayer player, IPlayerSession session, ItemStack stack, int boost, int cost) {
        if (session.hasEnoughMana(cost())) {
            MovingObjectPosition mop = SimpleUtil.rayTrace(player, world, 70 + (boost * 2));
            if (mop != null) {
                int oldX = MathHelper.floor_double(player.posX), oldY = MathHelper.floor_double(player.posY), oldZ = MathHelper.floor_double(player.posZ);
                int newX = MathHelper.floor_double(player.posX), newY = MathHelper.floor_double(player.posY), newZ = MathHelper.floor_double(player.posZ);
                boolean teleport = false;
                if (mop.typeOfHit == MovingObjectType.BLOCK) {
                    newX = mop.blockX;
                    newY = mop.blockY;
                    newZ = mop.blockZ;
                    Block blk = world.getBlock(newX, newY, newZ);
                    if (blk != null && !world.isAirBlock(newX, newY, newZ))
                        teleport = true;
                } else if (mop.typeOfHit == MovingObjectType.ENTITY) {
                    newX = MathHelper.floor_double(mop.entityHit.posX);
                    newY = MathHelper.floor_double(mop.entityHit.posY);
                    newZ = MathHelper.floor_double(mop.entityHit.posZ);
                    teleport = true;
                }
                if (teleport) {
                    teleportTo(player, newX, newY, newZ);
                    if (MathHelper.floor_double(player.posX) != oldX || MathHelper.floor_double(player.posY) != oldY || MathHelper.floor_double(player.posZ) != oldZ)
                        session.adjustMana(-cost(), false);
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
        if (session.hasEnoughMana(cost)) {
            if (target instanceof EntityLivingBase) {
                teleportRandomly((EntityLivingBase) target);
                session.adjustMana(-cost(), false);
            }
        }
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, IPlayerSession session, int par4, boolean par5, int boost, int cost, boolean bool) {
        if (entity instanceof EntityLivingBase) {
            if (!(entity instanceof EntityPlayer))
                teleportRandomly((EntityLivingBase) entity);
        }
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.ENDERPORT;
    }

    @Override
    public int cost() {
        return 30;
    }

    protected boolean teleportRandomly(EntityLivingBase entity) {
        double d0 = entity.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = entity.posY + (this.rand.nextInt(64) - 32);
        double d2 = entity.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(entity, d0, d1, d2);
    }

    protected boolean teleportTo(EntityLivingBase entity, double d0, double d1, double d2) {
        EnderTeleportEvent event = new EnderTeleportEvent(entity, d0, d1, d2, 0);
        if (MinecraftForge.EVENT_BUS.post(event))
            return false;
        for (int i = 0; i < 32; ++i) {
            entity.worldObj.spawnParticle("portal", d0, d1 + this.rand.nextDouble() * 2.0D, d2, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }

        boolean successful = false;
        if (!entity.worldObj.isRemote) {
            if (entity instanceof EntityPlayerMP) {
                EntityPlayerMP entityplayermp = (EntityPlayerMP) entity;
                if (entityplayermp.playerNetServerHandler.func_147362_b().isChannelOpen()) {
                    if (entityplayermp.isRiding())
                        entityplayermp.mountEntity(null);
                    entityplayermp.setPositionAndUpdate(d0, d1 + 1, d2);
                    entityplayermp.fallDistance = 0f;
                }
            } else {
                if (entity.isRiding())
                    entity.mountEntity(null);
                entity.setPositionAndUpdate(d0, d1 + 1, d2);
                entity.fallDistance = 0f;
            }
            if (entity.posX == d0 && entity.posY == d1 && entity.posZ == d2)
                successful = true;
        }
        entity.worldObj.playSoundAtEntity(entity, "mob.endermen.portal", 1.0F, 1.0F);
        return successful;
    }

    @Override
    public String getSpellDescription() {
        return ModLibs.ENDERPORT + "Desc";
    }
}

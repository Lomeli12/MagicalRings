package net.lomeli.ring.core;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.ReflectionHelper;

import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.SpellRegistry;

public class SimpleUtil {
    private static Random rand = new Random();

    public static MovingObjectPosition rayTrace(EntityLivingBase entity, World world) {
        return rayTrace(entity, world, true);
    }

    public static MovingObjectPosition rayTrace(EntityLivingBase entity, World world, boolean hitLiquids) {
        float f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * 1f;
        float f2 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * 1f;
        double d = entity.prevPosX + (entity.posX - entity.prevPosX) * 1f;
        double d1 = entity.prevPosY + entity.getEyeHeight() + (entity.posY - entity.prevPosY) * (1f + 1.6200000000000001D);
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * 1f;
        Vec3 vec3d = Vec3.createVectorHelper(d, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329f - 3.141593f);
        float f4 = MathHelper.sin(-f2 * 0.01745329f - 3.141593f);
        float f5 = -MathHelper.cos(-f1 * 0.01745329f);
        float f6 = MathHelper.sin(-f1 * 0.01745329f);
        float f7 = f4 * f5;
        float f8 = f6;
        float f9 = f3 * f5;
        double d3 = 5000D;
        Vec3 vec3d2 = vec3d.addVector(f7 * d3, f8 * d3, f9 * d3);
        MovingObjectPosition mop = world.rayTraceBlocks(vec3d, vec3d2, hitLiquids);
        return mop;
    }

    public static Entity getEntityPointedAt(World world, EntityPlayer player, double midrange, double range, boolean noCollide) {
        Entity pEntity = null;
        Vec3 v = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 v1 = player.getLook(1f);
        Vec3 v2 = v.addVector(v1.xCoord * range, v1.yCoord * range, v1.zCoord * range);
        List list = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(v1.xCoord * range, v1.yCoord * range, v1.zCoord * range));
        if (!list.isEmpty()) {
            double d = 0;
            for (int i = 0; i < list.size(); i++) {
                Entity entity = (Entity) list.get(i);
                if (entity.getDistanceToEntity(player) >= midrange && (entity.canBeCollidedWith() || noCollide) &&
                        world.func_147447_a(Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ), Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ), false, true, false) == null) {
                    float f = Math.max(0.8f, entity.getCollisionBorderSize());
                    AxisAlignedBB aabb = entity.boundingBox.expand(f, f, f);
                    MovingObjectPosition mov = aabb.calculateIntercept(v, v2);
                    if (aabb.isVecInside(v)) {
                        if (0d < d || d == 0d) {
                            d = 0d;
                            pEntity = entity;
                        }
                    } else if (mov != null) {
                        double d1 = v.distanceTo(mov.hitVec);
                        if (d1 < d || d == 0d) {
                            d = d1;
                            pEntity = entity;
                        }
                    }
                }
            }
        }
        return pEntity;
    }

    public static NBTTagCompound getRingTag(ItemStack stack) {
        if (stack.getTagCompound() != null) {
            if (stack.getTagCompound().hasKey(ModLibs.RING_TAG)) {
                NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
                return tag;
            }
        }
        return null;
    }

    public static ISpell getSpell(NBTTagCompound tag) {
        if (tag != null) {
            if (tag.hasKey(ModLibs.SPELL_ID)) {
                int spellID = tag.getInteger(ModLibs.SPELL_ID);
                ISpell spell = SpellRegistry.getSpellLazy(spellID);
                return spell;
            }
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String... names) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                for (String fieldName : names) {
                    if (field.getName().equalsIgnoreCase(fieldName)) {
                        field.setAccessible(true);
                        return field;
                    }
                }
            }
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToFindFieldException(names, e);
        }
        return null;
    }

    public static Object getObject(Class<?> clazz, Object obj, String... names) {
        try {
            Field field = getField(clazz, names);
            if (field != null)
                return field.get(obj);
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToAccessFieldException(names, e);
        }
        return null;
    }

    public static int getInt(Class<?> clazz, Object obj, String... names) {
        try {
            Field field = getField(clazz, names);
            if (field != null)
                return field.getInt(obj);
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToAccessFieldException(names, e);
        }
        return 0;
    }

    public static int randDist(int random) {
        return rand.nextInt(random) * (rand.nextBoolean() ? -1 : 1);
    }
}

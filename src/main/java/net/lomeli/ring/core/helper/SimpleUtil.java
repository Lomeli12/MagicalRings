package net.lomeli.ring.core.helper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.relauncher.ReflectionHelper;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.lib.ModLibs;

public class SimpleUtil {
    private static Random rand = new Random();

    public static MovingObjectPosition rayTrace(EntityLivingBase entity, World world, double range) {
        return rayTrace(entity, world, range, true);
    }

    public static MovingObjectPosition rayTrace(EntityLivingBase entity, World world, double range, boolean hitLiquids) {
        Vec3 v = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        Vec3 v1 = entity.getLook(1f);
        Vec3 v2 = v.addVector(v1.xCoord * range, v1.yCoord * range, v1.zCoord * range);
        MovingObjectPosition mop = world.rayTraceBlocks(v, v2, hitLiquids);
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
        if (stack != null && stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey(ModLibs.RING_TAG)) {
                NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
                return tag;
            }
        }
        return null;
    }

    public static String getSpellIdFromTag(NBTTagCompound tag) {
        if (tag != null) {
            if (tag.hasKey(ModLibs.SPELL_ID))
                return tag.getString(ModLibs.SPELL_ID);
        }
        return null;
    }

    public static ISpell getSpell(NBTTagCompound tag) {
        if (tag != null)
            return Rings.proxy.spellRegistry.getSpell(getSpellIdFromTag(tag));
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

    public static boolean isStackRegisteredAsOreDic(ItemStack stack, String oreDicName) {
        if (stack != null && stack.getItem() != null) {
            List<ItemStack> stackList = OreDictionary.getOres(oreDicName);
            if (stackList != null && !stackList.isEmpty()) {
                for (ItemStack item : stackList) {
                    if (areStacksSame(item, stack))
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean areStacksSame(ItemStack item1, ItemStack item2) {
        if (item1 != null && item1.getItem() != null && item2 != null && item2.getItem() != null)
            return item1.getItem() == item2.getItem() && item1.getItemDamage() == item2.getItemDamage();
        return false;
    }
}

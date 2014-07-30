package net.lomeli.ring.core.helper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
        return rayTrace(entity, world, range, true, false);
    }

    public static MovingObjectPosition rayTrace(EntityLivingBase entity, World world, double range, boolean hitLiquids, boolean noClipBlocks) {
        Vec3 v = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        Vec3 v1 = entity.getLook(1f);
        Vec3 v2 = v.addVector(v1.xCoord * range, v1.yCoord * range, v1.zCoord * range);
        MovingObjectPosition mop = world.func_147447_a(v, v2, hitLiquids, noClipBlocks, false);
        return mop;
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

    public static boolean areItemObjectsSame(Object obj1, Object obj2) {
        if (obj1 instanceof String) {
            if (obj2 instanceof String)
                return obj1.equals(obj2);
            else {
                ItemStack stack = null;
                if (obj2 instanceof Item)
                    stack = new ItemStack((Item) obj2);
                else if (obj2 instanceof Block)
                    stack = new ItemStack((Block) obj2);
                else if (obj2 instanceof ItemStack)
                    stack = (ItemStack) obj2;
                if (stack != null)
                    return isStackRegisteredAsOreDic(stack, (String) obj1);
            }
        } else {
            ItemStack item = null;
            if (obj1 instanceof Item)
                item = new ItemStack((Item) obj1);
            else if (obj1 instanceof Block)
                item = new ItemStack((Block) obj1);
            else if (obj1 instanceof ItemStack)
                item = (ItemStack) obj1;
            if (item != null) {
                if (obj2 instanceof String)
                    return isStackRegisteredAsOreDic(item, (String) obj2);
                else {
                    ItemStack stack = null;
                    if (obj2 instanceof Item)
                        stack = new ItemStack((Item) obj2);
                    else if (obj2 instanceof Block)
                        stack = new ItemStack((Block) obj2);
                    else if (obj2 instanceof ItemStack)
                        stack = (ItemStack) obj2;
                    if (stack != null)
                        return areStacksSame(item, stack);
                }
            }
        }
        return false;
    }
}

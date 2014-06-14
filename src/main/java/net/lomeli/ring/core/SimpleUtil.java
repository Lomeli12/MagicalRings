package net.lomeli.ring.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

public class SimpleUtil {
    
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

    public static void addToPersistantData(EntityPlayer player, String tagName, NBTTagCompound tag) {
        if(tag != null) {
            NBTTagCompound pData = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            if (pData == null)
                pData = new NBTTagCompound();

            pData.setTag(tagName, tag);

            player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, pData);
        }
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
                ISpell spell = MagicHandler.getSpellLazy(spellID);
                return spell;
            }
        }
        return null;
    }
}

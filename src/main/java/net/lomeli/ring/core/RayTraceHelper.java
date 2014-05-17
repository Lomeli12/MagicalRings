package net.lomeli.ring.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RayTraceHelper {

    public static MovingObjectPosition getRayTrace(EntityPlayer player, World world) {
        float f = 1f;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d = player.prevPosX + (player.posX - player.prevPosX) * f;
        double d1 = player.prevPosY + (world.isRemote ? 0D : (player.getEyeHeight() - 0.09D)) +(player.posY - player.prevPosY) * (f + 1.62);
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
        Vec3 vec3d = Vec3.createVectorHelper(d, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329f - 3.141593f);
        float f4 = MathHelper.sin(-f2 * 0.01745329f - 3.141593f);
        float f5 = -MathHelper.cos(-f1 * 0.01745329f);
        float f6 = MathHelper.sin(-f1 * 0.01745329f);
        float f7 = f4 * f5;
        float f8 = f6;
        float f9 = f3 * f5;
        double d3 = 5000D;
        Vec3 vec3d2 = vec3d.addVector((double) f7 * d3, (double) f8 * d3, (double) f9 * d3);
        MovingObjectPosition mop = world.rayTraceBlocks(vec3d, vec3d2, true);
        return mop;
    }

}

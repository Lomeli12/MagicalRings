package net.lomeli.ring.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireProofItem extends EntityItem {
    public EntityFireProofItem(World world) {
        super(world);
        init();
    }

    public EntityFireProofItem(World world, double x, double y, double z) {
        super(world, x, y, z);
        init();
    }

    public EntityFireProofItem(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
        init();
    }

    private void init() {
        this.isImmuneToFire = true;
    }

    @Override
    public void onUpdate() {
        ItemStack stack = this.getEntityItem();
        if (stack != null && stack.getItem() != null && stack.getItem().onEntityItemUpdate(this))
            return;
        this.onEntityUpdate();
        if (delayBeforeCanPickup > 0)
            delayBeforeCanPickup--;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.03999999910593033D;

        noClip = func_145771_j(posX, (boundingBox.minY + boundingBox.maxY) / 2D, posZ);
        moveEntity(motionX, motionY, motionZ);

        float f = 0.98F;

        if (onGround)
            f = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ)).slipperiness * 0.98F;

        motionX *= f;
        motionY *= 0.9800000190734863D;
        motionZ *= f;

        if (onGround)
            motionY *= -0.5D;
    }

    @Override
    public boolean handleLavaMovement() {
        return worldObj.isMaterialInBB(boundingBox, Material.lava);
    }

    @Override
    public void setFire(int p_70015_1_) {
    }

    @Override
    protected void dealFireDamage(int p_70081_1_) {
    }

    @Override
    protected void setOnFireFromLava() {
    }
}

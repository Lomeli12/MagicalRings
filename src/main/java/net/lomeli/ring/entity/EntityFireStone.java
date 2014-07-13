package net.lomeli.ring.entity;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import net.lomeli.ring.item.ModItems;

public class EntityFireStone extends EntityFireProofItem {
    private int timer;

    public EntityFireStone(World world) {
        super(world);
    }

    public EntityFireStone(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityFireStone(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
    }

    @Override
    protected void setOnFireFromLava() {
        if (isDead)
            return;
        if (worldObj.isRemote)
            return;
        int x = MathHelper.floor_double(posX), y = MathHelper.floor_double(posY), z = MathHelper.floor_double(posZ);
        if (worldObj.getBlock(x, y, z).getMaterial() == Material.lava || worldObj.getBlock(x, y + 1, z).getMaterial() == Material.lava) {
            for (int i = y + 1; i <= y + 10; i++) {
                if (worldObj.isAirBlock(x, i, z) && worldObj.getBlock(x, i - 1, z).getMaterial() == Material.lava) {
                    if (++timer >= 120) {
                        double newY = y;
                        for (int j = 0; j < worldObj.getActualHeight(); j++) {
                            if (worldObj.isAirBlock(x, y + j, z)) {
                                newY = y + j + 0.5;
                                break;
                            }
                        }
                        EntityFireProofItem upgraded = new EntityFireProofItem(worldObj, posX, newY, posZ, new ItemStack(ModItems.materials, getEntityItem().stackSize, 2));
                        worldObj.spawnEntityInWorld(upgraded);
                        setDead();
                        return;
                    }
                }
            }
        }
    }
}

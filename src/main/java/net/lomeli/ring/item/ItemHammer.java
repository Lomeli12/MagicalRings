package net.lomeli.ring.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import net.lomeli.ring.lib.ModLibs;

public class ItemHammer extends ItemRings {

    public ItemHammer(String texture, int damage) {
        super(texture);
        this.setMaxDamage(damage);
        this.setMaxStackSize(1);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean par) {
        if (GuiScreen.isShiftKeyDown()) {
            info.add(StatCollector.translateToLocal(ModLibs.USES_LEFT) + " " + (itemStack.getMaxDamage() - itemStack.getItemDamage()));
            info.add(StatCollector.translateToLocal(ModLibs.HAMMER_INFO));
        }else
            info.add(StatCollector.translateToLocal(ModLibs.MORE_INFO));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float f, float f1, float f2) {
        Block blk = world.getBlock(x, y, z);
        if (blk != null) {
            AxisAlignedBB box = blk.getCollisionBoundingBoxFromPool(world, x, y + 1, z);
            if (box != null) {
                List<?> entityList = world.getEntitiesWithinAABB(EntityItem.class, box);
                if (entityList != null && !entityList.isEmpty()) {
                    for (Object obj : entityList) {
                        if (obj != null && obj instanceof EntityItem) {
                            EntityItem entityItem = (EntityItem) obj;
                            if (entityItem.getEntityItem().getItem() == Items.book) {
                                if (entityItem.getEntityItem().stackSize > 1) {
                                    ItemStack oldStack = entityItem.getEntityItem().copy();
                                    oldStack.stackSize--;
                                    EntityItem leftOvers = new EntityItem(world, x, y, z, oldStack);
                                    if (!world.isRemote)
                                        world.spawnEntityInWorld(leftOvers);
                                }
                                entityItem.setEntityItemStack(new ItemStack(ModItems.book));
                                spawnEffects(world, entityItem.posX, entityItem.posY - 1, entityItem.posZ);
                                if (!player.capabilities.isCreativeMode)
                                    stack.damageItem(1, player);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return super.onItemUse(stack, player, world, x, y, z, side, f, f1, f2);
    }

    public void spawnEffects(World worldObj, double xCoord, double yCoord, double zCoord) {
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + 0.5, yCoord + 1.1, zCoord + 0.5, 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + 0.5, yCoord + 1.1, zCoord + 0.5, 0, 0, 0);
    }
}

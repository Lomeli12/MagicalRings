package net.lomeli.modjam4.block;

import java.util.List;

import net.lomeli.modjam4.Rings;
import net.lomeli.modjam4.block.tile.TileAltar;
import net.lomeli.modjam4.block.tile.TileItemAltar;
import net.lomeli.modjam4.lib.ModLibs;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAltars extends BlockRings implements ITileEntityProvider {

    public BlockAltars(String texture) {
        super(Material.rock, texture);
        this.setHardness(4f);
        this.setResistance(20);
        this.setTickRandomly(true);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return ModLibs.altarRenderID;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz) {

        TileEntity tile = world.getTileEntity(x, y, z);
        ItemStack stack = player.getCurrentEquippedItem();
        if (tile != null) {
            world.func_147479_m(x, y, z);
            if (tile instanceof TileAltar) {
                // TODO activate infusion ritual
            }

            if (stack != null) {
                if (((IInventory) tile).getStackInSlot(0) == null) {
                    ((IInventory) tile).setInventorySlotContents(0, new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
                    player.getCurrentEquippedItem().stackSize--;
                    world.func_147479_m(x, y, z);
                    return true;
                }
            }else {
                if (((IInventory) tile).getStackInSlot(0) != null) {
                    ItemStack item = ((IInventory) tile).getStackInSlot(0).copy();
                    System.out.println(item.getDisplayName());
                    EntityItem entity = new EntityItem(world, x, y + 2, z, item);
                    if (!world.isRemote)
                        world.spawnEntityInWorld(entity);
                    ((IInventory) tile).setInventorySlotContents(0, null);
                    world.func_147479_m(x, y, z);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getItemIconName() {
        return ModLibs.MOD_ID.toLowerCase() + ":altar";
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (meta == 0)
            return new TileAltar();
        else
            return new TileItemAltar();
    }

    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
        TileEntity tile = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

        if (tile != null && tile instanceof IInventory) {
            for (int i1 = 0; i1 < ((IInventory) tile).getSizeInventory(); ++i1) {
                ItemStack itemstack = ((IInventory) tile).getStackInSlot(i1);

                if (itemstack != null) {
                    float f = p_149749_1_.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = p_149749_1_.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = p_149749_1_.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; p_149749_1_.spawnEntityInWorld(entityitem)) {
                        int j1 = p_149749_1_.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                            j1 = itemstack.stackSize;

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(p_149749_1_, (double) ((float) p_149749_2_ + f), (double) ((float) p_149749_3_ + f1), (double) ((float) p_149749_4_ + f2), new ItemStack(itemstack.getItem(),
                                j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) p_149749_1_.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) p_149749_1_.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) p_149749_1_.rand.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                    }
                }
            }

            p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
        }

        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

    public static class ItemAltar extends ItemBlock {

        public ItemAltar(Block block) {
            super(block);
            this.setHasSubtypes(true);
            this.setCreativeTab(Rings.modTab);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void getSubItems(Item item, CreativeTabs tab, List list) {
            list.add(new ItemStack(item, 1, 0));
            list.add(new ItemStack(item, 1, 1));
        }

    }
}

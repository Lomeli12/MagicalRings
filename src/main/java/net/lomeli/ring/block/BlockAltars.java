package net.lomeli.ring.block;

import java.awt.Color;
import java.util.List;

import net.lomeli.ring.Rings;
import net.lomeli.ring.block.tile.TileAltar;
import net.lomeli.ring.block.tile.TileItemAltar;
import net.lomeli.ring.item.ItemMagicRing;
import net.lomeli.ring.item.ItemSpellParchment;
import net.lomeli.ring.lib.ModLibs;
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
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAltars extends BlockRings implements ITileEntityProvider {

    public BlockAltars(String texture) {
        super(Material.rock, texture);
        this.setHardness(4f);
        this.setResistance(20);
        this.setBlockBounds(0.145F, 0.0F, 0.145F, 0.855F, 0.76F, 0.855F);
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

    public boolean isRing(ItemStack stack) {
        if (stack != null)
            return (stack.getItem() instanceof ItemMagicRing);
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz) {
        TileEntity tile = world.getTileEntity(x, y, z);
        ItemStack stack = player.getCurrentEquippedItem();
        if (tile != null && tile instanceof IInventory) {
            ItemStack tileStack = ((IInventory) tile).getStackInSlot(0);
            world.func_147479_m(x, y, z);

            if (stack != null) {
                if (!player.isSneaking()) {
                    if (stack.getItem() instanceof ItemSpellParchment) {
                        if (tile instanceof TileAltar && tileStack != null) {
                            if (isRing(tileStack)) {
                                ((TileAltar) tile).startInfusion(player, stack.getItemDamage());
                                return true;
                            }
                        }
                    }
                    if (tileStack == null) {
                        tileStack = new ItemStack(stack.getItem(), 1, stack.getItemDamage());
                        tileStack.stackTagCompound = stack.getTagCompound();
                        ((IInventory) tile).setInventorySlotContents(0, tileStack);
                        if (!player.capabilities.isCreativeMode)
                            player.getCurrentEquippedItem().stackSize--;
                        world.func_147479_m(x, y, z);
                        return true;
                    }
                }
            }else {
                if (tileStack != null) {
                    ItemStack item = tileStack;
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
        return metadata < 2 ? true : false;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        switch(meta) {
        case 0 :
            return new TileAltar();
        case 1 :
            return new TileItemAltar();
        default:
            return null;
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        return this.damageDropped(world.getBlockMetadata(x, y, z));
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof IInventory) {
            for (int i1 = 0; i1 < ((IInventory) tile).getSizeInventory(); ++i1) {
                ItemStack itemstack = ((IInventory) tile).getStackInSlot(i1);

                if (itemstack != null) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = world.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem)) {
                        int j1 = world.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                            j1 = itemstack.stackSize;

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) world.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) world.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) world.rand.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                    }
                }
            }

            world.func_147453_f(x, y, z, block);
        }

        super.breakBlock(world, x, y, z, block, meta);
    }

    public static class ItemAltar extends ItemBlock {

        public ItemAltar(Block block) {
            super(block);
            this.setHasSubtypes(true);
            this.setCreativeTab(Rings.modTab);
        }

        @Override
        public int getMetadata(int par1) {
            return par1;
        }

        @Override
        public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
            return par1ItemStack.getItemDamage() == 1 ? Color.WHITE.getRGB() : Color.BLUE.getRGB();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void getSubItems(Item item, CreativeTabs tab, List list) {
            list.add(new ItemStack(item, 1, 0));
            list.add(new ItemStack(item, 1, 1));
        }

        @Override
        public String getUnlocalizedName(ItemStack stack) {
            return this.getUnlocalizedName() + "." + stack.getItemDamage();
        }
    }
}

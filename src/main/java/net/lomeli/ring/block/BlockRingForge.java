package net.lomeli.ring.block;

import net.lomeli.ring.Rings;
import net.lomeli.ring.block.tile.TileRingForge;
import net.lomeli.ring.lib.ModLibs;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRingForge extends BlockRings implements ITileEntityProvider, IBookEntry {

    @SideOnly(Side.CLIENT)
    private IIcon top, sides, bottom;

    public BlockRingForge(String texture) {
        super(Material.iron, texture);
    }
    
    @Override
    public int getPage(int metadata) {
        return 3;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz) {
        TileRingForge tile = (TileRingForge) world.getTileEntity(x, y, z);
        if (tile != null && !player.isSneaking()) {
            player.openGui(Rings.instance, ModLibs.RING_FORGE_GUI, world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        top = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":" + this.blockTexture + "_top");
        sides = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":" + this.blockTexture + "_sides");
        bottom = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":" + this.blockTexture + "_bottom");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return side == 0 ? bottom : side == 1 ? top : sides;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileRingForge();
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof IInventory) {
            for (int i1 = 0; i1 < ((IInventory) tile).getSizeInventory() - 1; ++i1) {
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

}

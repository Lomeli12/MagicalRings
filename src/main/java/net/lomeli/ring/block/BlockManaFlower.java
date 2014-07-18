package net.lomeli.ring.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.api.interfaces.IBookEntry;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;

public class BlockManaFlower extends BlockRings implements IGrowable, IPlantable, IBookEntry {
    private String texture;
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public BlockManaFlower(String texture) {
        super(Material.leaves, texture);
        this.setHardness(0.1F);
        this.setStepSound(soundTypeGrass);
        this.setTickRandomly(true);
        this.texture = texture;
        this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.8F, 0.7f, 0.8F);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        iconArray = new IIcon[4];
        for (int i = 0; i < iconArray.length; i++) {
            iconArray[i] = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":flower/" + this.texture + i);
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return this.iconArray[meta % this.iconArray.length];
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
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
        return 1;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!this.canBlockStay(world, x, y, z))
            world.func_147480_a(x, y, z, true);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta >= 3) {
            ItemStack stack = new ItemStack(ModItems.materials, 1 + world.rand.nextInt(2), 5);
            if (!world.isRemote)
                world.spawnEntityInWorld(new EntityItem(world, x, y, z, stack));
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
            onNeighborBlockChange(world, x, y, z, this);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        int meta = world.getBlockMetadata(x, y, z);
        System.out.println(meta);
        if (meta >= 3) {
            ItemStack stack = new ItemStack(ModItems.materials, 1 + world.rand.nextInt(2), 5);
            if (!world.isRemote)
                world.spawnEntityInWorld(new EntityItem(world, x, y, z, stack));
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
            onNeighborBlockChange(world, x, y, z, this);
        } else
            super.onBlockClicked(world, x, y, z, player);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);
        if (canBlockStay(world, x, y, z)) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta < 3 && world.canBlockSeeTheSky(x, y + 1, z)) {
                if (rand.nextInt(10) == 0) {
                    world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
                    onNeighborBlockChange(world, x, y, z, this);
                }
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
    }

    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_) {
        return world.getBlockMetadata(x, y, z) != 3;
    }

    @Override
    public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
        return true;
    }

    @Override
    public void func_149853_b(World world, Random rand, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z) + MathHelper.getRandomIntegerInRange(rand, 2, 5);
        if (l > 3)
            l = 3;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return ModBlocks.manaFlower;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }

    @Override
    public String getBookPage(int metadata) {
        return ModLibs.MOD_ID.toLowerCase() + ".manaFlower";
    }

    @Override
    public int getData() {
        return 0;
    }

    public static class ItemManaBush extends ItemBlock {

        public ItemManaBush(Block p_i45328_1_) {
            super(p_i45328_1_);
            this.setMaxDamage(0);
            this.setHasSubtypes(true);
        }

        @Override
        public int getMetadata(int par1) {
            return par1;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IIcon getIconFromDamage(int par1) {
            return this.field_150939_a.getIcon(0, par1);
        }
    }
}

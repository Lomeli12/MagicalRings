package net.lomeli.ring.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.client.render.IconCT;

public class BlockCT extends BlockRings {
    @SideOnly(Side.CLIENT)
    private IconCT connectedIcon;

    public BlockCT(Material mat, String texture) {
        super(mat, texture);
        this.setHardness(4f);
        this.setResistance(20);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        connectedIcon = new IconCT(register, this.texture, "ct/" + this.texture + "/", ModLibs.MOD_ID);
        this.blockIcon = connectedIcon.getFullIcon();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        Block block = world.getBlock(x, y, z);

        if (block == this) {
            Block sideBlock = world.getBlock(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side]);
            if (sideBlock != null) {
                if (sideBlock.isOpaqueCube()) {
                    if (world.getBlockMetadata(x, y, z) != world.getBlockMetadata(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side]))
                        return true;
                }
            }

            if (block == this)
                return false;
        }

        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return side == 7 ? this.connectedIcon : this.blockIcon;
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
        return ModLibs.ctRenderID;
    }
}

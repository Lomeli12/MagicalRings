package net.lomeli.ring.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.api.interfaces.IBookEntry;
import net.lomeli.ring.client.render.IconCT;
import net.lomeli.ring.lib.ModLibs;

public class BlockCT extends BlockRings implements IBookEntry {
    @SideOnly(Side.CLIENT)
    private IconCT connectedIcon;

    private boolean opaque;

    public BlockCT(Material mat, String texture, boolean opaque) {
        super(mat, texture);
        this.opaque = opaque;
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        connectedIcon = new IconCT(register, this.texture, "ct/" + this.texture + "/", ModLibs.MOD_ID);
        this.blockIcon = connectedIcon.getFullIcon();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        Block block = world.getBlock(x, y, z);

        if (block != null) {
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
        return opaque;
    }

    @Override
    public int getRenderType() {
        return ModLibs.ctRenderID;
    }

    @Override
    public String getBookPage(int metadata) {
        return ModLibs.MOD_ID.toLowerCase() + "." + this.texture;
    }

    @Override
    public int getData() {
        return 0;
    }
}

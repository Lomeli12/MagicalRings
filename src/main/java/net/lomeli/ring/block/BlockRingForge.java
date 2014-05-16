package net.lomeli.ring.block;

import net.lomeli.ring.Rings;
import net.lomeli.ring.block.tile.TileRingForge;
import net.lomeli.ring.lib.ModLibs;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRingForge extends BlockRings implements ITileEntityProvider {

    @SideOnly(Side.CLIENT)
    private IIcon top, sides, bottom;

    public BlockRingForge(String texture) {
        super(Material.iron, texture);
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

}

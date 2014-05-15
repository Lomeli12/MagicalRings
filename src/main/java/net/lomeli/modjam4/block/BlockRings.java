package net.lomeli.modjam4.block;

import net.lomeli.modjam4.Rings;
import net.lomeli.modjam4.lib.ModLibs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockRings extends Block{
    protected String blockTexture;
    protected IIcon blankIcon;

    protected BlockRings(Material material, String texture) {
        super(material);
        this.setCreativeTab(Rings.modTab);
        this.blockTexture = texture;
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":" + this.blockTexture);
        this.blankIcon = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":blank");
    }

    @Override
    public Block setBlockName(String p_149663_1_) {
        return super.setBlockName(ModLibs.MOD_ID.toLowerCase() + "." + p_149663_1_);
    }
   
    

}

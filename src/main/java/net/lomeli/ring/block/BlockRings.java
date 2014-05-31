package net.lomeli.ring.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;

public class BlockRings extends Block {
    protected String blockTexture;
    protected IIcon blankIcon;

    public BlockRings(Material material, String texture) {
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

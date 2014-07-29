package net.lomeli.ring.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;

public class BlockRings extends Block {
    protected IIcon blankIcon;
    protected String texture;

    public BlockRings(Material material, String texture) {
        super(material);
        this.setCreativeTab(Rings.modTab);
        this.texture = texture;
        this.setBlockTextureName(ModLibs.MOD_ID.toLowerCase() + ":" + texture);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        this.blankIcon = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":blank");
    }

    @Override
    public Block setBlockName(String p_149663_1_) {
        return super.setBlockName(ModLibs.MOD_ID.toLowerCase() + "." + p_149663_1_);
    }

}

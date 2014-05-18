package net.lomeli.ring.block;

import net.lomeli.ring.Rings;
import net.lomeli.ring.item.ModItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOnion extends BlockCrops {
    @SideOnly(Side.CLIENT)
    private IIcon[] field_149869_a;
    private static final String __OBFID = "CL_00000286";
    
    public BlockOnion() {
        super();
        this.setBlockTextureName("potatoes");
        this.setCreativeTab(null);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        if (p_149691_2_ < 7) {
            if (p_149691_2_ == 6)
                p_149691_2_ = 5;

            return this.field_149869_a[p_149691_2_ >> 1];
        }else
            return this.field_149869_a[3];
    }

    protected Item func_149866_i() {
        return ModItems.food;
    }

    protected Item func_149865_P() {
        return ModItems.food;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.field_149869_a = new IIcon[4];

        for (int i = 0; i < this.field_149869_a.length; ++i) {
            this.field_149869_a[i] = p_149651_1_.registerIcon(this.getTextureName() + "_stage_" + i);
        }
    }
}

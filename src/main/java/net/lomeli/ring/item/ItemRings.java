package net.lomeli.ring.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;

public class ItemRings extends Item {
    protected String itemTexture;
    protected IIcon blankIcon;

    public ItemRings(String texture) {
        super();
        this.setCreativeTab(Rings.modTab);
        this.setTextureName(ModLibs.MOD_ID.toLowerCase() + ":" + texture);
        this.itemTexture = texture;
    }

    @Override
    public Item setUnlocalizedName(String name) {
        return super.setUnlocalizedName(ModLibs.MOD_ID.toLowerCase() + "." + name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        this.blankIcon = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":blank");
    }
}

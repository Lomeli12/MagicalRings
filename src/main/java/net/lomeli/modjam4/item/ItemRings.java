package net.lomeli.modjam4.item;

import net.lomeli.modjam4.lib.ModLibs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRings extends Item{
    protected String itemTexture;
    protected IIcon blankIcon;
    
    public ItemRings(String texture) {
        super();
        this.itemTexture = texture;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":" + this.itemTexture);
        this.blankIcon = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":blank");
    }
}

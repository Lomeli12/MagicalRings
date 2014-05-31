package net.lomeli.ring.api;

import net.minecraft.item.ItemStack;

public interface IPageRegistry {
    public void registerTitlePage(String title, String id);
    
    public void registerTextPage(String title, String text, String id);
    
    public void registerImagePage(String title, String alttext, String resource, int u, int v, int width, int height, String id);
    
    public void registerItemPage(ItemStack item, String alttext, String id);
    
    public void registerItemRecipePage(ItemStack item, String alttext, String id);
}

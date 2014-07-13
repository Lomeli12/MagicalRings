package net.lomeli.ring.api.interfaces;

import net.minecraft.item.ItemStack;

import net.lomeli.ring.api.Page;

/**
 * Do NOT implement! Use instance in RingAPI to use these methods. Try to keep page IDs unique.
 */
public interface IPageRegistry {

    /**
     * Add new title page
     */
    public void addTitlePage(String title, String id);

    /**
     * Add page with text
     */
    public void addTextPage(String title, String text, String id);

    /**
     * Add page with Image and caption
     */
    public void addImagePage(String title, String alttext, String resource, int u, int v, int width, int height, String id);

    /**
     * Add page with item info
     */
    public void addItemPage(ItemStack item, String alttext, String id);

    /**
     * Add page with item recipe
     */
    public void addItemRecipePage(ItemStack item, String alttext, String id);

    /**
     * Add custom page
     */
    public void addNewPage(Page page, String id);

    /**
     * Add custom page
     */
    public void addNewPage(Page page);
}

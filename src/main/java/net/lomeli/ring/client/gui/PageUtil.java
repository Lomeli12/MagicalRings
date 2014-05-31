package net.lomeli.ring.client.gui;

import net.minecraft.item.ItemStack;

import net.lomeli.ring.api.IPageRegistry;

public class PageUtil implements IPageRegistry{

    @Override
    public void registerTitlePage(String title, String id) {
        Page.addPage(new PageTitle(null, title).setID(id));
    }

    @Override
    public void registerTextPage(String title, String text, String id) {
        Page.addPage(new PageText(null, title, text).setID(id));
    }

    @Override
    public void registerImagePage(String title, String alttext, String resource, int u, int v, int width, int height, String id) {
        Page.addPage(new PageImage(null, title, alttext, resource, u, v, width, height).setID(id));
    }

    @Override
    public void registerItemPage(ItemStack item, String alttext, String id) {
        Page.addPage(new PageItem(null, item, alttext).setID(id));
    }

    @Override
    public void registerItemRecipePage(ItemStack item, String alttext, String id) {
        Page.addPage(new PageRecipe(null, item, alttext).setID(id));
    }

}

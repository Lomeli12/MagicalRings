package net.lomeli.ring.client.page;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import net.lomeli.ring.api.Page;
import net.lomeli.ring.api.interfaces.IBookGui;

public class PageItem extends Page {

    private ItemStack stack;
    private String text;
    private int color;

    public PageItem(IBookGui screen, ItemStack item, String text) {
        this(screen, item, text, Color.CYAN.getRGB());
    }

    public PageItem(IBookGui screen, ItemStack item, String text, int color) {
        super(screen);
        this.stack = item;
        this.text = text;
        this.color = color;
    }

    @Override
    public PageItem setID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public void draw() {
        super.draw();
        if (this.stack != null && this.text != null) {
            this.renderItem(stack, drawX, drawY - 5);
            mc.fontRenderer.drawStringWithShadow(stack.getDisplayName(), drawX + 20, drawY, this.color);
            drawString(StatCollector.translateToLocal(text), drawX, drawY + 5, 0);
        }
    }

}

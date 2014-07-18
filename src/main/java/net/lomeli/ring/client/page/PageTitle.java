package net.lomeli.ring.client.page;

import java.awt.Color;

import net.minecraft.util.StatCollector;

import net.lomeli.ring.api.Page;
import net.lomeli.ring.api.interfaces.IBookGui;

public class PageTitle extends Page {

    private String title;
    private int color;

    public PageTitle(IBookGui screen, String title) {
        super(screen);
        this.title = title;
        this.color = Color.CYAN.getRGB();
    }

    public PageTitle(IBookGui screen, String title, int color) {
        this(screen, title);
        this.color = color;
    }

    @Override
    public PageTitle setID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public void draw() {
        super.draw();
        if (this.title != null) {
            mc.fontRenderer.drawSplitString(StatCollector.translateToLocal(this.title), drawX + 1, drawY + 26, this.wordWrap, 0);
            mc.fontRenderer.drawSplitString(StatCollector.translateToLocal(this.title), drawX, drawY + 25, this.wordWrap, this.color);
        }
    }

}

package net.lomeli.ring.client.page;

import java.awt.Color;

import net.minecraft.util.StatCollector;

import net.lomeli.ring.api.Page;
import net.lomeli.ring.api.interfaces.IBookGui;

public class PageText extends Page {

    private String text, title;
    private int color;

    public PageText(IBookGui screen, String text) {
        this(screen, null, text);
    }

    public PageText(IBookGui screen, String title, String text) {
        this(screen, title, text, Color.CYAN.getRGB());
    }

    public PageText(IBookGui screen, String title, String text, int color) {
        super(screen);
        this.text = text;
        this.title = title;
        this.color = color;
    }

    @Override
    public PageText setID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public void draw() {
        super.draw();
        if (title != null) {
            mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(this.title), drawX, drawY, this.color);
            this.drawString(this.text, drawX, drawY + 5, 0);
        } else
            this.drawString(this.text, drawX, drawY - 10, 0);
    }
}

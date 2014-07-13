package net.lomeli.ring.client.page;

import java.awt.Color;

import net.minecraft.util.StatCollector;

import net.lomeli.ring.api.Page;
import net.lomeli.ring.client.gui.GuiSpellBook;

public class PageText extends Page {

    private String text, title;

    public PageText(GuiSpellBook screen, String text) {
        this(screen, null, text);
    }

    public PageText(GuiSpellBook screen, String title, String text) {
        super(screen);
        this.text = text;
        this.title = title;
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
            mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(this.title), drawX, drawY, Color.CYAN.getRGB());
            this.drawString(this.text, drawX, drawY + 5, 0);
        } else
            this.drawString(this.text, drawX, drawY - 10, 0);
    }
}

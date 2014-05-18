package net.lomeli.ring.client.gui;

import java.awt.Color;

import net.minecraft.util.StatCollector;

public class PageTitle extends Page {

    private String title;

    public PageTitle(GuiSpellBook screen, String title) {
        super(screen);
        this.title = title;
    }

    @Override
    public void draw() {
        super.draw();
        if (this.title != null)
            drawSplitStringWithShadow(StatCollector.translateToLocal(this.title), drawX, drawY + 25, this.wordWrap, Color.YELLOW.getRGB());
    }

}

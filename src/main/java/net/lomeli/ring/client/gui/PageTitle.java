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
    public PageTitle setID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public void draw() {
        super.draw();
        if (this.title != null){
            mc.fontRenderer.drawSplitString(StatCollector.translateToLocal(this.title), drawX + 1, drawY + 26, this.wordWrap, 0);
            mc.fontRenderer.drawSplitString(StatCollector.translateToLocal(this.title), drawX, drawY + 25, this.wordWrap, Color.CYAN.getRGB());
        }
    }

}

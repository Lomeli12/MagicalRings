package net.lomeli.ring.client.gui;

import java.awt.Color;

import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class PageText extends Page {

    private String text;
    
    public PageText(GuiSpellBook screen, String text) {
        super(screen);
        this.text = text;
    }

    @Override
    public void draw() {
        super.draw();
        mc.fontRenderer.drawSplitString(StatCollector.translateToLocal(text), this.drawX, this.drawY, this.wordWrap, 0);
    }
}

package net.lomeli.ring.client.gui;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class PageItem extends Page {

    private ItemStack stack;
    private String text;

    public PageItem(GuiSpellBook screen, ItemStack item, String text) {
        super(screen);
        this.stack = item;
        this.text = text;
    }

    @Override
    public void draw() {
        super.draw();
        if (this.stack != null && this.text != null) {
            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, drawX, drawY - 5);
            drawSplitStringWithShadow(stack.getDisplayName(), drawX + 20, drawY, wordWrap, Color.YELLOW.getRGB());
            mc.fontRenderer.drawSplitString(StatCollector.translateToLocal(text), drawX, drawY + 15, wordWrap, 0);
        }
    }

}

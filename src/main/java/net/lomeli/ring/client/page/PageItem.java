package net.lomeli.ring.client.page;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import net.lomeli.ring.api.Page;
import net.lomeli.ring.client.gui.GuiSpellBook;

public class PageItem extends Page {

    private ItemStack stack;
    private String text;

    public PageItem(GuiSpellBook screen, ItemStack item, String text) {
        super(screen);
        this.stack = item;
        this.text = text;
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
            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, drawX, drawY - 5);
            mc.fontRenderer.drawStringWithShadow(stack.getDisplayName(), drawX + 20, drawY, Color.CYAN.getRGB());
            drawString(StatCollector.translateToLocal(text), drawX, drawY + 5, 0);
        }
    }

}

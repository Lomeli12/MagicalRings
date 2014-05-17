package net.lomeli.ring.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.IIcon;

public abstract class Page {
    public GuiScreen gui;
    protected static RenderItem itemRenderer = new RenderItem();
    protected int x, y;
    
    public Page(GuiScreen screen, int x, int y) {
        this.gui = screen;
        this.x = x;
        this.y = y;
    }
    
    public abstract void draw();
    
    public abstract IIcon pageIcon();
}

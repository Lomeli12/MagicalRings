package net.lomeli.ring.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class PageImage extends Page {
    private ResourceLocation image;
    private String unlocalizedName, text;
    private int u, v, w, h;

    public PageImage(GuiSpellBook screen, String title, String text, String resource, int u, int v, int width, int height) {
        super(screen);
        this.unlocalizedName = title;
        this.text = text;
        this.image = new ResourceLocation(resource);
        this.u = u;
        this.v = v;
        this.w = width;
        this.h = height;
    }

    @Override
    public PageImage setID(String id) {
        this.id = id;
        return this;
    }
    
    @Override
    public void draw() {
        super.draw();
        mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(text), drawX, drawY, Color.CYAN.getRGB());
        
        mc.renderEngine.bindTexture(image);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1F, 1F, 1F, 1F);

        gui.drawTexturedModalRect(drawX, drawY + 15, v, u, w, h);
        
        GL11.glDisable(GL11.GL_BLEND);

        this.drawString(this.unlocalizedName, drawX, drawY + h + 10, 0);
    }

}

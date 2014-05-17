package net.lomeli.ring.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.lomeli.ring.lib.ModLibs;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiSpellBook extends GuiScreen {
    public static List<Page> avaliablePages = new ArrayList<Page>();
    private static final ResourceLocation guiTexture = new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":gui/book.png");
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    private Page currentPage;

    public GuiSpellBook() {
        this.currentPage = avaliablePages.size() > 0 ? avaliablePages.get(0) : null;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTexture);
        int k = (this.width - this.bookImageWidth) / 2;
        byte b0 = 2;
        this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
        if (this.currentPage != null)
            this.currentPage.draw();
        super.drawScreen(par1, par2, par3);
    }

}

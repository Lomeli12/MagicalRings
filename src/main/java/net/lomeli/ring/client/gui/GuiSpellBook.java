package net.lomeli.ring.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.lomeli.ring.lib.ModLibs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiSpellBook extends GuiScreen {
    public static List<Page> avaliablePages = new ArrayList<Page>();
    public static final ResourceLocation guiTexture = new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":gui/book.png");
    public int bookImageWidth = 192, bookImageHeight = 192, pageNumber;
    private GuiSpellBook.NextPageButton nextPage, previousPage;
    private Page currentPage;

    public GuiSpellBook() {
        Page.loadPages(this);
        this.pageNumber = 0;
    }

    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        int k = (this.width - this.bookImageWidth) / 2;
        int b0 = (this.height - this.bookImageHeight) / 2;
        this.currentPage = avaliablePages.size() > 0 ? (this.pageNumber < avaliablePages.size() ? avaliablePages.get(this.pageNumber) : null) : null;
        this.buttonList.add(this.nextPage = new GuiSpellBook.NextPageButton(1, k + 120, b0 + 157, true));
        this.buttonList.add(this.previousPage = new GuiSpellBook.NextPageButton(2, k + 38, b0 + 157, false));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        if (button != null) {
            if (button.id == this.nextPage.id) {
                if (this.pageNumber < avaliablePages.size() -1)
                    this.pageNumber++;
            }else if (button.id == this.previousPage.id) {
                if (this.pageNumber > 0)
                    this.pageNumber--;
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(guiTexture);
        int k = (this.width - this.bookImageWidth) / 2;
        int b0 = (this.height - this.bookImageHeight) / 2;
        this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();

        this.currentPage = avaliablePages.size() > 0 ? (this.pageNumber < avaliablePages.size() ? avaliablePages.get(this.pageNumber) : null) : null;
        if (this.currentPage != null)
            this.currentPage.draw();

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        super.drawScreen(par1, par2, par3);
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton {
        private final boolean field_146151_o;
        private static final String __OBFID = "CL_00000745";

        public NextPageButton(int par1, int par2, int par3, boolean par4) {
            super(par1, par2, par3, 23, 13, "");
            this.field_146151_o = par4;
        }

        public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
            if (this.visible) {
                boolean flag = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                p_146112_1_.getTextureManager().bindTexture(GuiSpellBook.guiTexture);
                int k = 0;
                int l = 192;

                if (flag) {
                    k += 23;
                }

                if (!this.field_146151_o) {
                    l += 13;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
            }
        }
    }

}

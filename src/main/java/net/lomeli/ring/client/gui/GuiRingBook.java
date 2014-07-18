package net.lomeli.ring.client.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.Page;
import net.lomeli.ring.api.interfaces.IBookGui;
import net.lomeli.ring.network.PacketSavePage;

public class GuiRingBook extends GuiScreen implements IBookGui {
    public ResourceLocation guiTexture;
    public List<Page> avaliablePages = new ArrayList<Page>();
    public int bookImageWidth = 192, bookImageHeight = 192, pageNumber, left, top;
    private NextPageButton nextPage, previousPage;
    private Page currentPage;

    private int mouseX, mouseY;

    public GuiRingBook(int i) {
        this.pageNumber = i;
    }

    public GuiRingBook() {
        this(0);
    }

    public GuiRingBook setGuiTexture(ResourceLocation rs) {
        this.guiTexture = rs;
        return this;
    }

    public GuiRingBook setPageNumber(int i) {
        this.pageNumber = i;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        int k = (this.width - this.bookImageWidth) / 2;
        int b0 = (this.height - this.bookImageHeight) / 2;
        left = k;
        top = b0;
        this.currentPage = avaliablePages.size() > 0 ? (this.pageNumber < avaliablePages.size() ? avaliablePages.get(this.pageNumber) : null) : null;
        this.buttonList.add(this.nextPage = new NextPageButton(1, k + 144, b0 + 168, true, guiTexture));
        this.buttonList.add(this.previousPage = new NextPageButton(2, k + 17, b0 + 168, false, guiTexture));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        if (button != null) {
            if (button.id == this.nextPage.id) {
                if (this.pageNumber < avaliablePages.size() - 1)
                    this.pageNumber++;
            } else if (button.id == this.previousPage.id) {
                if (this.pageNumber > 0)
                    this.pageNumber--;
            }
            Rings.pktHandler.sendToServer(new PacketSavePage(Minecraft.getMinecraft().thePlayer, this.pageNumber));
        }
    }

    private void bindTexture(ResourceLocation loc) {
        mc.getTextureManager().bindTexture(loc);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        if (this.pageNumber <= 0)
            this.previousPage.visible = false;
        else
            this.previousPage.visible = true;

        if (this.pageNumber >= avaliablePages.size() - 1)
            this.nextPage.visible = false;
        else
            this.nextPage.visible = true;


        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(guiTexture);

        int k = (this.width - this.bookImageWidth) / 2;
        int b0 = (this.height - this.bookImageHeight) / 2;
        this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();

        this.currentPage = avaliablePages.size() > 0 ? (this.pageNumber < avaliablePages.size() ? avaliablePages.get(this.pageNumber) : null) : null;
        if (this.currentPage != null)
            this.currentPage.draw();

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void handleMouseInput() {
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        mouseX = x;
        mouseY = y;

        super.handleMouseInput();
    }

    public void drawToolTipOverArea(int minX, int minY, int maxX, int maxY, String msg) {
        if ((mouseX >= minX && mouseX <= maxX) && (mouseY >= minY && mouseY <= maxY)) {
            List<String> list = new ArrayList<String>();
            list.add(msg);
            this.drawHoveringText(list, mouseX, mouseY, mc.fontRenderer);
        }
    }

    @Override
    public void drawHoveringText(List p_146283_1_, int p_146283_2_, int p_146283_3_, FontRenderer font) {
        super.drawHoveringText(p_146283_1_, p_146283_2_, p_146283_3_, font);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getLeft() {
        return left;
    }

    @Override
    public int getTop() {
        return 0;
    }
}

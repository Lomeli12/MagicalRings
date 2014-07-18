package net.lomeli.ring.client.page;

import org.lwjgl.opengl.GL11;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import net.lomeli.ring.api.Page;
import net.lomeli.ring.api.interfaces.IBookGui;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.client.gui.GuiRingBook;
import net.lomeli.ring.lib.BookText;

public class PageInfusionSetup extends Page {

    public PageInfusionSetup(IBookGui screen) {
        super(screen);
    }

    @Override
    public PageInfusionSetup setID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public void draw() {
        super.draw();
        mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(BookText.INFUSE), this.drawX, this.drawY, Color.CYAN.getRGB());
        this.drawString("1x ", this.drawX, this.drawY + 6, 0);
        this.renderBlock(ModBlocks.altar, 0, this.drawX + mc.fontRenderer.getStringWidth("1x"), this.drawY + 12);
        String translatedText = StatCollector.translateToLocal(BookText.INFUSE2);
        this.drawString(translatedText, this.drawX, this.drawY + 22, 0);
        this.renderBlock(ModBlocks.altar, 1, this.drawX + mc.fontRenderer.getStringWidth(translatedText) - 5, this.drawY + 28);
        renderSlots();

        this.renderBlock(ModBlocks.altar, 1, this.drawX + 11, this.drawY + 46);
        this.renderBlock(ModBlocks.altar, 1, this.drawX + 47, this.drawY + 46);
        this.renderBlock(ModBlocks.altar, 1, this.drawX + 83, this.drawY + 46);

        this.renderBlock(ModBlocks.altar, 1, this.drawX + 11, this.drawY + 82);
        this.renderBlock(ModBlocks.altar, 1, this.drawX + 83, this.drawY + 82);

        this.renderBlock(ModBlocks.altar, 1, this.drawX + 11, this.drawY + 118);
        this.renderBlock(ModBlocks.altar, 1, this.drawX + 47, this.drawY + 118);
        this.renderBlock(ModBlocks.altar, 1, this.drawX + 83, this.drawY + 118);

        this.renderBlock(ModBlocks.altar, 0, this.drawX + 47, this.drawY + 82);
    }

    public void renderSlots() {
        mc.renderEngine.bindTexture(((GuiRingBook) gui).guiTexture);
        GL11.glColor3f(1f, 1f, 1f);
        this.drawSlot(this.drawX + 10, this.drawY + 45);
        this.drawSlot(this.drawX + 28, this.drawY + 45);
        this.drawSlot(this.drawX + 46, this.drawY + 45);
        this.drawSlot(this.drawX + 64, this.drawY + 45);
        this.drawSlot(this.drawX + 82, this.drawY + 45);

        this.drawSlot(this.drawX + 10, this.drawY + 63);
        this.drawSlot(this.drawX + 28, this.drawY + 63);
        this.drawSlot(this.drawX + 46, this.drawY + 63);
        this.drawSlot(this.drawX + 64, this.drawY + 63);
        this.drawSlot(this.drawX + 82, this.drawY + 63);

        this.drawSlot(this.drawX + 10, this.drawY + 81);
        this.drawSlot(this.drawX + 28, this.drawY + 81);
        this.drawSlot(this.drawX + 46, this.drawY + 81);
        this.drawSlot(this.drawX + 64, this.drawY + 81);
        this.drawSlot(this.drawX + 82, this.drawY + 81);

        this.drawSlot(this.drawX + 10, this.drawY + 99);
        this.drawSlot(this.drawX + 28, this.drawY + 99);
        this.drawSlot(this.drawX + 46, this.drawY + 99);
        this.drawSlot(this.drawX + 64, this.drawY + 99);
        this.drawSlot(this.drawX + 82, this.drawY + 99);

        this.drawSlot(this.drawX + 10, this.drawY + 117);
        this.drawSlot(this.drawX + 28, this.drawY + 117);
        this.drawSlot(this.drawX + 46, this.drawY + 117);
        this.drawSlot(this.drawX + 64, this.drawY + 117);
        this.drawSlot(this.drawX + 82, this.drawY + 117);
    }

    public void renderBlock(Block block, int meta, int x, int y) {
        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(block, 1, meta), x, y);
    }

    public void drawSlot(int x, int y) {
        ((GuiScreen) gui).drawTexturedModalRect(x, y, 62, 196, 18, 18);
    }

}
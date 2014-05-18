package net.lomeli.ring.client.gui;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.BookText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public abstract class Page {
    public GuiSpellBook gui;
    protected Minecraft mc = Minecraft.getMinecraft();
    protected static RenderItem itemRenderer = new RenderItem();
    protected int drawX, drawY, wordWrap;
    protected static Random rand = new Random();

    public Page(GuiSpellBook screen) {
        this.gui = screen;
        this.drawX = ((screen.width - screen.bookImageWidth) / 2) + 250;
        this.drawY = ((screen.height - screen.bookImageHeight) / 2) + 135;
        this.wordWrap = this.gui.bookImageWidth - 75;
    }

    public void draw() {
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    public void drawSplitStringWithShadow(String par1Str, int par2, int par3, int par4, int par5) {
        mc.fontRenderer.drawSplitString(par1Str, par2 + 1, par3 + 1, par4, 0);
        mc.fontRenderer.drawSplitString(par1Str, par2, par3, par4, par5);
    }

    public static void loadPages(GuiSpellBook screen) {
        GuiSpellBook.avaliablePages.clear();
        GuiSpellBook.avaliablePages.add(new PageTitle(screen, new ItemStack(ModItems.book).getDisplayName()));
        GuiSpellBook.avaliablePages.add(new PageText(screen, BookText.INTRO));
        GuiSpellBook.avaliablePages.add(new PageItem(screen, new ItemStack(ModItems.magicRing), BookText.RING));
        GuiSpellBook.avaliablePages.add(new PageItem(screen, new ItemStack(ModBlocks.ringForge), BookText.FORGE));
        GuiSpellBook.avaliablePages.add(new PageText(screen, BookText.FORGE2));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModBlocks.ringForge), BookText.FORGE3));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.ironHammer)));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.diamondHammer)));
        GuiSpellBook.avaliablePages.add(new PageText(screen, BookText.ALTAR));
        GuiSpellBook.avaliablePages.add(new PageItem(screen, new ItemStack(ModBlocks.altar), BookText.ALTAR2));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModBlocks.altar), BookText.ALTAR3));
        GuiSpellBook.avaliablePages.add(new PageItem(screen, new ItemStack(ModBlocks.altar, 1, 1), BookText.ITEM_ALTAR));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModBlocks.altar, 1, 1)));
        GuiSpellBook.avaliablePages.add(new PageItem(screen, new ItemStack(ModItems.spellParchment), BookText.SPELL));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.spellParchment), BookText.SPELL2));
    }
}

package net.lomeli.ring.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.BookText;
import net.lomeli.ring.lib.ModLibs;

public abstract class Page {
    public GuiSpellBook gui;
    protected Minecraft mc = Minecraft.getMinecraft();
    protected static RenderItem itemRenderer = new RenderItem();
    protected int drawX, drawY, wordWrap;
    protected static Random rand = new Random();
    protected String id;

    public Page(GuiSpellBook screen) {
        this.gui = screen;
    }

    public Page setID(String id) {
        this.id = id;
        return this;
    }

    public void setGui(GuiSpellBook screen) {
        this.gui = screen;
    }

    public void draw() {
        this.drawX = (this.gui.width - this.gui.bookImageWidth) / 2 + 35;
        this.drawY = (this.gui.height - this.gui.bookImageHeight) / 2 + 15;
        this.wordWrap = this.gui.bookImageWidth - 75;
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    public void drawSplitStringWithShadow(String par1Str, int par2, int par3, int par4, int par5) {
        drawString(par1Str, par2 + 1, par3 + 1, 0);
        drawString(par1Str, par2, par3, par5);
    }

    public void drawString(String text, int xOffset, int yOffset, int color) {
        boolean unicode = mc.fontRenderer.getUnicodeFlag();
        mc.fontRenderer.setUnicodeFlag(true);
        String translated = StatCollector.translateToLocal(text).replaceAll("&", "\u00a7");
        String[] textEntries = translated.split("<br>");

        String lastFormat = "";
        String pendingFormat = "";
        for (String s : textEntries) {
            List<String> wrappedLines = new ArrayList<String>();
            String workingOn = "";

            int i = 0;
            String[] tokens = s.split(" ");
            for (String s1 : tokens) {
                boolean skipPending = false;
                String format = getFormatFromString(s1);

                if (!format.isEmpty() && s1.length() > 0 && s1.charAt(0) != '\u00a7') {
                    skipPending = true;
                    pendingFormat = format;
                    format = "";
                }

                if (!pendingFormat.isEmpty() && !skipPending) {
                    format = pendingFormat;
                    pendingFormat = "";
                }

                if (MathHelper.stringNullOrLengthZero(format))
                    format = lastFormat;

                if (mc.fontRenderer.getStringWidth(workingOn + " " + s1) >= this.wordWrap) {
                    wrappedLines.add(workingOn);
                    workingOn = "";
                }
                workingOn = workingOn + format + " " + s1;

                if (i == tokens.length - 1)
                    wrappedLines.add(workingOn);

                ++i;
                lastFormat = format;
            }

            for (String s1 : wrappedLines) {
                yOffset += 9;
                mc.fontRenderer.drawString(s1, xOffset, yOffset, color);
            }

            yOffset += 4;
        }

        mc.fontRenderer.setUnicodeFlag(unicode);
    }

    public static boolean isFormatColor(char par0) {
        return par0 >= 48 && par0 <= 57 || par0 >= 97 && par0 <= 102 || par0 >= 65 && par0 <= 70;
    }

    public static boolean isFormatSpecial(char par0) {
        return par0 >= 107 && par0 <= 111 || par0 >= 75 && par0 <= 79 || par0 == 114 || par0 == 82;
    }

    public static String getFormatFromString(String par0Str) {
        String s1 = "";
        int i = -1;
        int j = par0Str.length();

        while ((i = par0Str.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                char c0 = par0Str.charAt(i + 1);

                if (isFormatColor(c0))
                    s1 = "\u00a7" + c0;
                else if (isFormatSpecial(c0))
                    s1 = s1 + "\u00a7" + c0;
            }
        }

        return s1;
    }

    public String pageID() {
        return this.id;
    }

    private static List<Page> addonPages = new ArrayList<Page>();

    public static void addPage(Page pg) {
        if (addonPages.contains(pg))
            return;
        addonPages.add(pg);
    }

    public static void loadPages(GuiSpellBook screen) {
        GuiSpellBook.avaliablePages.clear();
        GuiSpellBook.avaliablePages.add(new PageTitle(screen, new ItemStack(ModItems.book).getDisplayName()));
        GuiSpellBook.avaliablePages.add(new PageText(screen, BookText.INTRO));
        GuiSpellBook.avaliablePages.add(new PageItem(screen, new ItemStack(ModItems.magicRing), BookText.RING));
        GuiSpellBook.avaliablePages.add(new PageItem(screen, new ItemStack(ModBlocks.ringForge), BookText.FORGE).setID(ModLibs.MOD_ID.toLowerCase() + ".ringForge"));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModBlocks.ringForge), BookText.FORGE2));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.ironHammer)));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.diamondHammer)));
        GuiSpellBook.avaliablePages.add(new PageText(screen, BookText.ALTAR_INTRO, BookText.ALTAR));
        GuiSpellBook.avaliablePages.add(new PageImage(screen, "", BookText.ALTAR_INTRO, BookText.IMAGES, 0, 107, 115, 86));
        GuiSpellBook.avaliablePages.add(new PageItem(screen, new ItemStack(ModBlocks.altar), BookText.ALTAR2).setID(ModLibs.MOD_ID.toLowerCase() + ".infusionAltar"));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModBlocks.altar)));
        GuiSpellBook.avaliablePages.add(new PageItem(screen, new ItemStack(ModBlocks.altar, 1, 1), BookText.ITEM_ALTAR).setID(ModLibs.MOD_ID.toLowerCase() + ".altar"));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModBlocks.altar, 1, 1)));
        GuiSpellBook.avaliablePages.add(new PageInfusionSetup(screen));
        GuiSpellBook.avaliablePages.add(new PageText(screen, BookText.ALTAR_INTRO, BookText.INFUSE_INFO));
        GuiSpellBook.avaliablePages.add(new PageItem(screen, new ItemStack(ModItems.spellParchment), BookText.SPELL));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.spellParchment), BookText.SPELL2));
        GuiSpellBook.avaliablePages.add(new PageText(screen, BookText.RING_USE));
        GuiSpellBook.avaliablePages.add(new PageText(screen, BookText.BAUBLE_INTRO, BookText.BAUBLE));
        GuiSpellBook.avaliablePages.add(new PageImage(screen, BookText.BAUBLE1, BookText.BAUBLE_INTRO, BookText.IMAGES, 0, 0, 107, 100));
        GuiSpellBook.avaliablePages.add(new PageText(screen, BookText.BAUBLE_INTRO, BookText.BAUBLE2));
        GuiSpellBook.avaliablePages.add(new PageText(screen, ModLibs.MANA, BookText.MANA));
        GuiSpellBook.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.food, 1, 2), BookText.MANA2));
        if (!addonPages.isEmpty()) {
            for (Page pg : addonPages) {
                if (pg.gui == null)
                    pg.setGui(screen);
                GuiSpellBook.avaliablePages.add(pg);
            }
        }
    }
}

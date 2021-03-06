package net.lomeli.ring.api;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import net.lomeli.ring.api.interfaces.IBookGui;

public abstract class Page {
    protected static RenderItem itemRenderer = new RenderItem();
    protected static Random rand = new Random();
    public IBookGui gui;
    protected Minecraft mc = Minecraft.getMinecraft();
    protected int drawX, drawY, wordWrap;
    protected String id;

    public Page() {
    }

    public Page(IBookGui screen) {
        this.gui = screen;
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

    public void renderItem(ItemStack stack, int x, int y) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x, y);
        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x, y);
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }

    public Page setID(String id) {
        this.id = id;
        return this;
    }

    public void setGui(IBookGui screen) {
        this.gui = screen;
    }

    public void draw() {
        this.drawX = (this.gui.getWidth() - 192) / 2 + 35;
        this.drawY = (this.gui.getHeight() - 192) / 2 + 15;
        this.wordWrap = 192 - 75;
        GL11.glColor4f(1f, 1f, 1f, 1f);
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

    public String pageID() {
        return this.id;
    }
}

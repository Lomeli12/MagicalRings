package net.lomeli.ring.client.page;

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.Page;
import net.lomeli.ring.api.interfaces.IBookGui;
import net.lomeli.ring.client.gui.GuiRingBook;
import net.lomeli.ring.lib.BookText;
import net.lomeli.ring.lib.ModLibs;

public class PageInfusionRecipe extends Page {
    private ResourceLocation circle = new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":gui/circle.png");
    private ItemStack output;
    private Object[] recipe;
    private Object mainItem;
    private int cost, color;
    private double currentDegree;
    private List<PageUtil.ToolTipInfo> toolTips = new ArrayList<PageUtil.ToolTipInfo>();

    public PageInfusionRecipe(IBookGui gui, ItemStack itemStack, int color) {
        super(gui);
        this.output = itemStack;
        this.recipe = Rings.proxy.infusionRegistry.getRecipeFromOut(itemStack);
        this.cost = Rings.proxy.infusionRegistry.getCostFromOutput(itemStack);
        this.mainItem = Rings.proxy.infusionRegistry.getOutputFromBase(itemStack);
        this.color = color;
        this.currentDegree = Minecraft.getMinecraft().theWorld.getTotalWorldTime();
    }

    public PageInfusionRecipe(IBookGui gui, ItemStack itemStack) {
        this(gui, itemStack, Color.CYAN.getRGB());
    }

    @Override
    public void draw() {
        super.draw();
        toolTips.clear();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(circle);
        int k = (gui.getWidth() - 192) / 2;
        int b0 = (gui.getHeight() - 192) / 2;
        ((GuiScreen) gui).drawTexturedModalRect(k, b0, 0, 0, 192, 192);
        GL11.glPushMatrix();
        if (this.output != null) {
            this.renderItem(output, drawX, drawY - 5, false);
            mc.fontRenderer.drawStringWithShadow(output.getDisplayName(), drawX + 20, drawY, this.color);

            if (mainItem != null)
                this.renderItem(this.mainItem, drawX + 48, drawY + 68);

            currentDegree += 0.5d;
            if (this.recipe != null && this.recipe.length > 0) {
                int degreePerInput = 360 / this.recipe.length;
                for (Object obj : recipe) {
                    renderItemAtAngle(obj, (int) currentDegree);
                    currentDegree += degreePerInput;
                }
            }

            if (cost > 0)
                this.drawString(StatCollector.translateToLocal(BookText.MANA_COST) + " " + cost, drawX + 20, drawY + 130, 0);

            if (!toolTips.isEmpty())
                this.drawToolTips();
        }
    }

    private void renderItemAtAngle(Object obj, int angle) {
        if (obj != null) {
            angle -= 90;
            int radius = 32;
            double xPos = drawX + Math.cos(angle * Math.PI / 180D) * radius + 115 / 2 - 8;
            double yPos = drawY + Math.sin(angle * Math.PI / 180D) * radius + 70;
            renderItem(obj, (int) xPos, (int) yPos);
        }
    }

    private void renderItem(Object obj, int x, int y) {
        renderItem(obj, x, y, true);
    }

    private void renderItem(Object obj, int x, int y, boolean drawToolTip) {
        if (obj != null) {
            String toolTip = null;
            if (obj instanceof Item) {
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack((Item) obj), x, y);
                toolTip = new ItemStack((Item) obj).getDisplayName();
            } else if (obj instanceof Block) {
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack((Block) obj), x, y);
                toolTip = new ItemStack((Block) obj).getDisplayName();
            } else if (obj instanceof ItemStack) {
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, ((ItemStack) obj), x, y);
                toolTip = ((ItemStack) obj).getDisplayName();
            } else if (obj instanceof String) {
                String oreName = (String) obj;
                if (oreName != null) {
                    List<ItemStack> oreList = OreDictionary.getOres(oreName);
                    if (oreList != null && !oreList.isEmpty()) {
                        ItemStack stack = oreList.get(0);
                        if (stack != null) {
                            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, x, y);
                            toolTip = stack.getDisplayName();
                        }
                    }
                }
            }
            if (toolTip != null && drawToolTip)
                toolTips.add(new PageUtil.ToolTipInfo(x, y, toolTip));
        }
    }

    public void drawToolTips() {

        for (PageUtil.ToolTipInfo toolTip : toolTips) {
            int x = toolTip.getX(), y = toolTip.getY();
            ((GuiRingBook) gui).drawToolTipOverArea(x, y, x + 16, y + 16, toolTip.getToolTipText());
        }
    }
}

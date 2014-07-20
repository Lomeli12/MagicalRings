package net.lomeli.ring.client.page;

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IBookGui;
import net.lomeli.ring.lib.BookText;
import net.lomeli.ring.lib.ModLibs;

public class PageInfusionRecipe extends PageRecipe {
    private ResourceLocation circle = new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":gui/circle.png");
    private Object[] recipe;
    private Object mainItem;
    private int cost;
    private double currentDegree;

    public PageInfusionRecipe(IBookGui gui, ItemStack itemStack, int color) {
        super(gui, itemStack, null, color);
        this.output = itemStack;
        this.recipe = Rings.proxy.infusionRegistry.getRecipeFromOut(itemStack);
        this.cost = Rings.proxy.infusionRegistry.getCostFromOutput(itemStack);
        this.mainItem = Rings.proxy.infusionRegistry.getBaseFromOutput(itemStack);
    }

    public PageInfusionRecipe(IBookGui gui, ItemStack itemStack) {
        this(gui, itemStack, Color.CYAN.getRGB());
    }

    @Override
    public void renderRecipe() {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(circle);
        int k = (gui.getWidth() - 192) / 2;
        int b0 = (gui.getHeight() - 192) / 2;
        ((GuiScreen) gui).drawTexturedModalRect(k, b0, 0, 0, 192, 192);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glPopMatrix();

        if (mainItem != null)
            this.renderItemObject(this.mainItem, drawX + 48, drawY + 68);

        currentDegree = mc.theWorld.getWorldTime();
        if (this.recipe != null && this.recipe.length > 0) {
            int degreePerInput = 360 / this.recipe.length;
            for (Object obj : recipe) {
                renderItemAtAngle(obj, (int) currentDegree);
                currentDegree += degreePerInput;
            }
        }

        if (cost > 0)
            this.drawString(StatCollector.translateToLocal(BookText.MANA_COST) + " " + cost, drawX + 20, drawY + 130, 0);
    }

    private void renderItemAtAngle(Object obj, int angle) {
        if (obj != null) {
            angle -= 90;
            int radius = 32;
            double xPos = drawX + Math.cos(angle * Math.PI / 180D) * radius + 115 / 2 - 8;
            double yPos = drawY + Math.sin(angle * Math.PI / 180D) * radius + 70;
            renderItemObject(obj, (int) xPos, (int) yPos);
        }
    }

    @Override
    protected void renderItemObject(Object obj, int x, int y) {
        if (obj != null) {
            if (obj instanceof Item)
                this.renderItem(new ItemStack((Item) obj), x, y);
            else if (obj instanceof Block)
                this.renderItem(new ItemStack((Block) obj), x, y);
            else if (obj instanceof ItemStack)
                this.renderItem(((ItemStack) obj), x, y);
            else if (obj instanceof String) {
                String oreName = (String) obj;
                if (oreName != null) {
                    List<ItemStack> oreList = OreDictionary.getOres(oreName);
                    if (oreList != null && !oreList.isEmpty()) {
                        ItemStack stack = oreList.get(0);
                        if (stack != null)
                            this.renderItem(stack, x, y);
                    }
                }
            }
        }
    }
}

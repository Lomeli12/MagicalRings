package net.lomeli.ring.client.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class PageRecipe extends Page {
    private Object[] recipe;
    private ItemStack output;
    private String itemDescription;
    private int itemX = drawX + 25;

    public PageRecipe(GuiSpellBook screen, ItemStack item, String itemDescription) {
        super(screen);
        if (item != null) {
            this.output = item;
            this.recipe = getItemRecipe(item);
        }
        this.itemDescription = itemDescription;
    }

    public PageRecipe(GuiSpellBook screen, ItemStack item) {
        this(screen, item, null);
    }

    @Override
    public void draw() {
        super.draw();
        if (this.output != null && (this.recipe != null && this.recipe.length > 0)) {

            this.renderItem(output, drawX, drawY - 5);
            mc.fontRenderer.drawStringWithShadow(output.getDisplayName(), drawX + 20, drawY, Color.YELLOW.getRGB());

            renderSlots();
            
            if (recipe[0] != null)
                this.renderItem(recipe[0], itemX, drawY + 15);
            if (recipe[1] != null)
                this.renderItem(recipe[1], itemX + 20, drawY + 15);
            if (recipe[2] != null)
                this.renderItem(recipe[2], itemX + 40, drawY + 15);

            if (recipe[3] != null)
                this.renderItem(recipe[3], itemX, drawY + 35);
            if (recipe[4] != null)
                this.renderItem(recipe[4], itemX + 20, drawY + 35);
            if (recipe[5] != null)
                this.renderItem(recipe[5], itemX + 40, drawY + 35);

            if (recipe[6] != null)
                this.renderItem(recipe[6], itemX, drawY + 55);
            if (recipe[7] != null)
                this.renderItem(recipe[7], itemX + 20, drawY + 55);
            if (recipe[8] != null)
                this.renderItem(recipe[8], itemX + 40, drawY + 55);
            
            if (this.itemDescription != null) {
                mc.fontRenderer.drawSplitString(StatCollector.translateToLocal(this.itemDescription), this.drawX, this.drawY + 80, this.wordWrap, 0);
            }
        }
    }
    
    public void renderSlots(){
        mc.renderEngine.bindTexture(gui.guiTexture);
        GL11.glColor3f(1f, 1f, 1f);
        this.gui.drawTexturedModalRect(itemX - 1, drawY + 14, 62, 196, 18, 18);
        this.gui.drawTexturedModalRect(itemX + 19, drawY + 14, 62, 196, 18, 18);
        this.gui.drawTexturedModalRect(itemX + 39, drawY + 14, 62, 196, 18, 18);
        
        this.gui.drawTexturedModalRect(itemX - 1, drawY + 34, 62, 196, 18, 18);
        this.gui.drawTexturedModalRect(itemX + 19, drawY + 34, 62, 196, 18, 18);
        this.gui.drawTexturedModalRect(itemX + 39, drawY + 34, 62, 196, 18, 18);
        
        this.gui.drawTexturedModalRect(itemX - 1, drawY + 54, 62, 196, 18, 18);
        this.gui.drawTexturedModalRect(itemX + 19, drawY + 54, 62, 196, 18, 18);
        this.gui.drawTexturedModalRect(itemX + 39, drawY + 54, 62, 196, 18, 18);
    }

    private void renderItem(Object obj, int x, int y) {
        if (obj != null) {
            if (obj instanceof Item)
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack((Item) obj), x, y);
            else if (obj instanceof Block)
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack((Block) obj), x, y);
            else if (obj instanceof ItemStack)
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, ((ItemStack) obj), x, y);
        }
    }

    public static Object[] getItemRecipe(ItemStack stack) {
        Object[] recipe = new Object[9];
        if (stack != null) {
            List<IRecipe> possibleRecipe = new ArrayList<IRecipe>();
            for (Object rp : CraftingManager.getInstance().getRecipeList()) {
                if (rp instanceof IRecipe) {
                    ItemStack output = ((IRecipe) rp).getRecipeOutput();
                    if (output != null && (output.getItem() == stack.getItem() && output.getItemDamage() == stack.getItemDamage()))
                        possibleRecipe.add((IRecipe) rp);
                }
            }
            if (!possibleRecipe.isEmpty()) {
                IRecipe main = possibleRecipe.get(0);
                try {
                    Object[] inputs = null;
                    if (main instanceof ShapedOreRecipe)
                        inputs = ((ShapedOreRecipe) main).getInput();
                    else if (main instanceof ShapelessOreRecipe)
                        inputs = ((ShapelessOreRecipe) main).getInput().toArray();

                    if (inputs != null) {
                        for (int i = 0; i < inputs.length; i++) {
                            Object obj = inputs[i];
                            if (obj instanceof ArrayList<?>)
                                recipe[i] = ((ArrayList<?>) obj).get(rand.nextInt(((ArrayList<?>) obj).size()));
                            else
                                recipe[i] = obj;
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return recipe;
    }

}

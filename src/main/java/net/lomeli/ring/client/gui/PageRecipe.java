package net.lomeli.ring.client.gui;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class PageRecipe extends Page {
    private Object[] recipe, cache;
    private ItemStack output;
    private String itemDescription;
    private int tick;
    private int[] arrayIndex;

    public PageRecipe(GuiSpellBook screen, ItemStack item, String itemDescription) {
        super(screen);
        this.cache = new Object[9];
        this.arrayIndex = new int[9];
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
    public PageRecipe setID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public void draw() {
        super.draw();
        if (this.output != null && (this.recipe != null && this.recipe.length > 0)) {
            this.renderItem(output, drawX, drawY - 5, 0);
            mc.fontRenderer.drawStringWithShadow(output.getDisplayName(), drawX + 20, drawY, Color.CYAN.getRGB());

            renderSlots();

            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    int yOffset = drawY + 15 + (20 * y);
                    int xOffset = (drawX + 25) + (20 * x);
                    int index = x + (y * 3);
                    if (recipe[index] != null)
                        this.renderItem(recipe[index], xOffset, yOffset, index);
                }
            }

            if (this.itemDescription != null) {
                this.drawString(StatCollector.translateToLocal(this.itemDescription), this.drawX, this.drawY + 65, 0);
            }
        }
    }

    public void renderSlots() {
        mc.renderEngine.bindTexture(GuiSpellBook.guiTexture);
        GL11.glColor3f(1f, 1f, 1f);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int yOffset = drawY + 15 + (20 * y) - 1;
                int xOffset = (drawX + 25) + (20 * x) - 1;
                this.gui.drawTexturedModalRect(xOffset, yOffset, 62, 196, 18, 18);
            }
        }
    }

    private void renderItem(Object obj, int x, int y, int index) {
        if (obj != null) {
            if (obj instanceof Item)
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack((Item) obj), x, y);
            else if (obj instanceof Block)
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack((Block) obj), x, y);
            else if (obj instanceof ItemStack)
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, ((ItemStack) obj), x, y);
            else if (obj instanceof ArrayList<?>) {
                if (this.cache[index] == null)
                    this.cache[index] = ((ArrayList<?>) obj).get(rand.nextInt(((ArrayList<?>) obj).size()));
                
                if (++this.tick >= 25) {
                    arrayIndex[index]++;
                    if (arrayIndex[index] >= ((ArrayList<?>) obj).size())
                        arrayIndex[index] = 0;
                    this.cache[index] = ((ArrayList<?>) obj).get(arrayIndex[index]);
                }

                if (this.cache[index] instanceof Item)
                    itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack((Item) this.cache[index]), x, y);
                else if (this.cache[index] instanceof Block)
                    itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack((Block) this.cache[index]), x, y);
                else if (this.cache[index] instanceof ItemStack)
                    itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, ((ItemStack) this.cache[index]), x, y);
            }
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
                int width = 0, height = 0;
                try {
                    Object[] inputs = null;
                    if (main instanceof ShapedOreRecipe) {
                        inputs = ((ShapedOreRecipe) main).getInput();

                        width = getInteger(ShapedOreRecipe.class, main, 4);
                        height = getInteger(ShapedOreRecipe.class, main, 5);
                        if (inputs != null) {
                            for (int x = 0; x < width; x++) {
                                for (int y = 0; y < height; y++) {
                                    int index = y * width + x;
                                    Object obj = inputs[index];
                                    if (obj == null)
                                        continue;
                                    recipe[index] = obj;
                                }
                            }
                        }
                    }else if (main instanceof ShapelessOreRecipe) {
                        inputs = ((ShapelessOreRecipe) main).getInput().toArray();
                        if (inputs != null) {
                            for (int i = 0; i < inputs.length; i++) {
                                Object obj = inputs[i];
                                recipe[i] = obj;
                            }
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return recipe;
    }

    private static int getInteger(Class<?> clazz, Object obj, int index) throws IllegalArgumentException, IllegalAccessException {
        if (index < clazz.getDeclaredFields().length) {
            Field field = clazz.getDeclaredFields()[index];
            if (field != null) {
                field.setAccessible(true);
                return field.getInt(obj);
            }
        }
        return 0;
    }

}

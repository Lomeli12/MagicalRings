package net.lomeli.ring.client.page;

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import net.lomeli.ring.api.Page;
import net.lomeli.ring.api.interfaces.IBookGui;
import net.lomeli.ring.client.gui.GuiRingBook;
import net.lomeli.ring.client.handler.RenderHandler;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.lib.BookText;

/**
 * Some of this based off of NEI. Thanks ChickenBones
 */
public class PageRecipe extends Page {
    private PageUtil.StackPosition[] recipe;
    protected ItemStack output, toolTipStack;
    private String itemDescription;
    private int color;
    private boolean shapeless;

    public PageRecipe(IBookGui screen, ItemStack item, String itemDescription) {
        this(screen, item, itemDescription, Color.CYAN.getRGB());
    }

    public PageRecipe(IBookGui screen, ItemStack item, String itemDescription, int color) {
        super(screen);
        if (item != null) {
            this.output = item;
            this.recipe = getItemRecipe(item);
        }
        this.itemDescription = itemDescription;
        this.color = color;
    }

    public PageRecipe(IBookGui screen, ItemStack item) {
        this(screen, item, null);
    }

    public PageUtil.StackPosition[] getItemRecipe(ItemStack stack) {
        PageUtil.StackPosition[] recipe = new PageUtil.StackPosition[9];
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
                int width, height;
                try {
                    Object[] inputs = null;
                    if (main instanceof ShapedOreRecipe) {
                        inputs = ((ShapedOreRecipe) main).getInput();

                        width = SimpleUtil.getInt(ShapedOreRecipe.class, main, "width");
                        height = SimpleUtil.getInt(ShapedOreRecipe.class, main, "height");
                        if (inputs != null) {
                            for (int x = 0; x < width; x++)
                                for (int y = 0; y < height; y++) {
                                    int index = y * width + x;
                                    if (inputs[index] == null)
                                        continue;
                                    recipe[index] = new PageUtil.StackPosition(inputs[index], x, y);
                                }
                        }
                    } else if (main instanceof ShapelessOreRecipe) {
                        inputs = ((ShapelessOreRecipe) main).getInput().toArray();
                        if (inputs != null) {
                            for (int i = 0; i < inputs.length; i++) {
                                Object obj = inputs[i];
                                recipe[i] = new PageUtil.StackPosition(obj, PageUtil.stackorder[i][0], PageUtil.stackorder[i][1]);
                            }
                        }
                        shapeless = true;
                    } else if (main instanceof ShapedRecipes) {
                        inputs = (Object[]) SimpleUtil.getObject(ShapedRecipes.class, main, "recipeItems", "field_77574_d", "c");

                        width = SimpleUtil.getInt(ShapedRecipes.class, main, "recipeWidth", "field_77576_b", "a");
                        height = SimpleUtil.getInt(ShapedRecipes.class, main, "recipeHeight", "field_77577_c", "b");
                        if (inputs != null) {
                            for (int x = 0; x < width; x++)
                                for (int y = 0; y < height; y++) {
                                    int index = y * width + x;
                                    if (inputs[index] == null)
                                        continue;
                                    recipe[index] = new PageUtil.StackPosition(inputs[index], x, y);
                                }
                        }
                    } else if (main instanceof ShapelessRecipes) {
                        List list = (List) SimpleUtil.getObject(ShapelessRecipes.class, main, "recipeItems", "field_77579_b", "b");
                        if (list != null && !list.isEmpty()) {
                            for (int i = 0; i < list.size(); i++) {
                                Object obj = list.get(i);
                                recipe[i] = new PageUtil.StackPosition(obj, PageUtil.stackorder[i][0], PageUtil.stackorder[i][1]);
                            }
                        }
                        shapeless = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return recipe;
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
            mc.fontRenderer.drawSplitString(output.getDisplayName(), drawX + 21, drawY + 1, this.wordWrap - 5, 0);
            mc.fontRenderer.drawSplitString(output.getDisplayName(), drawX + 20, drawY, this.wordWrap - 5, this.color);

            renderRecipe();

            super.renderItem(output, drawX, drawY - 5);

            if (toolTipStack != null)
                RenderHandler.renderItemToolTip(gui.getMouseX(), gui.getMouseY(), toolTipStack);
            toolTipStack = null;
        }
    }

    public void renderRecipe() {
        renderSlots();

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int yOffset = drawY + 20 + (20 * y);
                int xOffset = (drawX + 25) + (20 * x);
                Object obj = getObjectForPosition(x, y);
                int index = x + (y * 3);
                if (obj != null) {
                    this.renderItemObject(obj, xOffset, yOffset);
                }
            }
        }

        if (this.itemDescription != null)
            this.drawString(StatCollector.translateToLocal(this.itemDescription), this.drawX, this.drawY + 76, 0);

        if (this.shapeless)
            this.drawString(StatCollector.translateToLocal(BookText.SHAPELESS), this.drawX, this.drawY + 68, 0);
    }

    private Object getObjectForPosition(int x, int y) {
        if (recipe != null && recipe.length > 0) {
            for (int i = 0; i < recipe.length; i++) {
                PageUtil.StackPosition position = recipe[i];
                if (position != null && position.rlyX == x && position.rlyY == y)
                    return position.itemObj;
            }
        }
        return null;
    }

    public void renderSlots() {
        GL11.glPushMatrix();
        mc.renderEngine.bindTexture(((GuiRingBook) gui).guiTexture);
        GL11.glColor3f(1f, 1f, 1f);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int yOffset = drawY + 20 + (20 * y) - 1;
                int xOffset = (drawX + 25) + (20 * x) - 1;
                ((GuiScreen) gui).drawTexturedModalRect(xOffset, yOffset, 62, 196, 18, 18);
            }
        }
        GL11.glPopMatrix();
    }

    protected void renderItemObject(Object obj, int x, int y) {
        if (obj != null) {
            if (obj instanceof Item)
                renderItem(new ItemStack((Item) obj), x, y);
            else if (obj instanceof Block)
                renderItem(new ItemStack((Block) obj), x, y);
            else if (obj instanceof ItemStack)
                renderItem(((ItemStack) obj), x, y);
            else if (obj instanceof ArrayList<?>) {
                Object obj1 = ((ArrayList<?>) obj).get(0);

                if (obj1 instanceof Item)
                    renderItem(new ItemStack((Item) obj1), x, y);
                else if (obj1 instanceof Block)
                    renderItem(new ItemStack((Block) obj1), x, y);
                else if (obj1 instanceof ItemStack)
                    renderItem(((ItemStack) obj1), x, y);
            }
        }
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y) {
        super.renderItem(stack, x, y);
        if ((gui.getMouseX() >= x && gui.getMouseX() <= x + 16) && (gui.getMouseY() >= y && gui.getMouseY() <= y + 16))
            toolTipStack = stack;
    }
}

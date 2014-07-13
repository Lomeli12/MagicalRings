package net.lomeli.ring.client.page;

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.StatCollector;

import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import net.lomeli.ring.api.Page;
import net.lomeli.ring.client.gui.GuiSpellBook;
import net.lomeli.ring.core.SimpleUtil;
import net.lomeli.ring.lib.BookText;

/**
 * Some of this based off of NEI. Thanks ChickenBones
 */
public class PageRecipe extends Page {
    private PageUtil.StackPosition[] recipe;
    private Object[] cache;
    private ItemStack output;
    private String itemDescription;
    private int tick;
    private int[] arrayIndex;
    private List<PageUtil.ToolTipInfo> toolTips = new ArrayList<PageUtil.ToolTipInfo>();
    private boolean shapeless;

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
            this.renderItem(output, drawX, drawY - 5, 0);
            mc.fontRenderer.drawStringWithShadow(output.getDisplayName(), drawX + 20, drawY, Color.CYAN.getRGB());

            renderSlots();

            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    int yOffset = drawY + 15 + (20 * y);
                    int xOffset = (drawX + 25) + (20 * x);
                    Object obj = getObjectForPosition(x, y);
                    int index = x + (y * 3);
                    if (obj != null) {
                        this.renderItem(obj, xOffset, yOffset, index);
                    }
                }
            }

            if (this.itemDescription != null)
                this.drawString(StatCollector.translateToLocal(this.itemDescription), this.drawX, this.drawY + 75, 0);

            if (this.shapeless)
                this.drawString(StatCollector.translateToLocal(BookText.SHAPELESS), this.drawX, this.drawY + 65, 0);

            if (!toolTips.isEmpty())
                this.drawToolTips();
        }
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
        mc.renderEngine.bindTexture(((GuiSpellBook) gui).guiTexture);
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
            } else if (obj instanceof ArrayList<?>) {
                if (this.cache[index] == null)
                    this.cache[index] = ((ArrayList<?>) obj).get(rand.nextInt(((ArrayList<?>) obj).size()));

                if (++this.tick >= 25) {
                    arrayIndex[index]++;
                    if (arrayIndex[index] >= ((ArrayList<?>) obj).size())
                        arrayIndex[index] = 0;
                    this.cache[index] = ((ArrayList<?>) obj).get(arrayIndex[index]);
                }

                if (this.cache[index] instanceof Item) {
                    itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack((Item) this.cache[index]), x, y);
                    toolTip = new ItemStack((Item) this.cache[index]).getDisplayName();
                } else if (this.cache[index] instanceof Block) {
                    itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack((Block) this.cache[index]), x, y);
                    toolTip = new ItemStack((Block) this.cache[index]).getDisplayName();
                } else if (this.cache[index] instanceof ItemStack) {
                    itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, ((ItemStack) this.cache[index]), x, y);
                    toolTip = ((ItemStack) this.cache[index]).getDisplayName();
                }
            }
            if (toolTip != null)
                toolTips.add(new PageUtil.ToolTipInfo(x, y, toolTip));
        }
    }

    public void drawToolTips() {
        for (PageUtil.ToolTipInfo toolTip : toolTips) {
            int x = toolTip.getX(), y = toolTip.getY();
            ((GuiSpellBook) gui).drawToolTipOverArea(x, y, x + 16, y + 16, toolTip.getToolTipText());
        }
    }
}

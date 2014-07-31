package net.lomeli.ring.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.lomeli.ring.api.interfaces.IInfusionRegistry;
import net.lomeli.ring.api.interfaces.recipe.IInfusionRecipe;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.item.ModItems;

public class InfusionRegistry implements IInfusionRegistry {
    private List<IInfusionRecipe> infusionList;

    public InfusionRegistry() {
        infusionList = new ArrayList<IInfusionRecipe>();
        registerInfusionRecipe(new ItemStack(Items.potionitem, 1, 16), new ItemStack(ModItems.food, 1, 3), 0, Items.redstone, Items.redstone, "berryMana", "berryMana");
        registerInfusionRecipe(new ItemStack(Items.nether_star), new ItemStack(ModItems.materials, 1, 6), 20, "berryMana", "gemDiamond", "berryMana", "gemDiamond", "berryMana", "gemDiamond", "berryMana", "gemDiamond");
        registerInfusionRecipe(new ItemStack(ModItems.food, 1, 3), new ItemStack(ModItems.food, 1, 2), 60, Blocks.gold_block, "gemMana", new ItemStack(Items.potionitem, 1, 8197));
        registerInfusionRecipe("blockGlass", new ItemStack(ModBlocks.manaGlass, 5), 15, "berryMana", "berryMana", "berryMana", "berryMana", "blockGlass", "blockGlass", "blockGlass", "blockGlass");
        registerInfusionRecipe(Blocks.stonebrick, new ItemStack(ModBlocks.manaBrick, 5), 15, "berryMana", "berryMana", "berryMana", "berryMana", Blocks.stonebrick, Blocks.stonebrick, Blocks.stonebrick, Blocks.stonebrick);
    }

    @Override
    public void registerInfusionRecipe(Object baseItem, ItemStack output, int manaCost, Object... additionalInputs) {
        registerInfusionRecipe(new InfusionRecipe(baseItem, output, manaCost, additionalInputs));
    }

    @Override
    public void registerInfusionRecipe(IInfusionRecipe recipe) {
        infusionList.add(recipe);
    }

    @Override
    public IInfusionRecipe getRecipeFromOutput(ItemStack out) {
        for (IInfusionRecipe recipe : infusionList) {
            if (recipe != null && SimpleUtil.areStacksSame(out, recipe.getOutput()))
                return recipe;
        }
        return null;
    }

    @Override
    public IInfusionRecipe getRecipeFromItems(ItemStack... ingredients) {
        for (IInfusionRecipe recipe : infusionList) {
            Object[] main = recipe.getIngredients();
            List<Object> test = new ArrayList<Object>();
            for (Object i : main) {
                if (i != null)
                    test.add(i);
            }
            if (test.isEmpty())
                continue;

            ingredientLoop:
            for (ItemStack stack : ingredients) {
                if (stack != null) {
                    recipeLoop:
                    for (int i = 0; i < test.size(); i++) {
                        Object item = test.get(i);
                        if (item != null) {
                            if (item instanceof String) {
                                if (SimpleUtil.isStackRegisteredAsOreDic(stack, (String) item)) {
                                    test.remove(i);
                                    break recipeLoop;
                                }
                            } else {
                                ItemStack recipeStack = null;
                                if (item instanceof Item)
                                    recipeStack = new ItemStack((Item) item);
                                else if (item instanceof Block)
                                    recipeStack = new ItemStack((Block) item);
                                else if (item instanceof ItemStack)
                                    recipeStack = (ItemStack) item;
                                if (recipeStack != null) {
                                    if (SimpleUtil.areStacksSame(stack, recipeStack)) {
                                        test.remove(i);
                                        break recipeLoop;
                                    }
                                }
                            }
                        } else
                            test.remove(i);
                    }
                }
            }
            if (test.isEmpty())
                return recipe;
        }
        return null;
    }

    @Override
    public boolean isItemValid(Object item) {
        if (item != null) {
            String oreDic = null;
            ItemStack stack = null;
            if (item instanceof String)
                oreDic = (String) item;
            else {
                if (item instanceof Item)
                    stack = new ItemStack((Item) item);
                else if (item instanceof Block)
                    stack = new ItemStack((Block) item);
                else if (item instanceof ItemStack)
                    stack = (ItemStack) item;
            }
            if (oreDic == null && stack == null)
                return false;
            for (IInfusionRecipe recipe : infusionList) {
                if (recipe != null) {
                    Object main = recipe.getBaseItem();
                    if (main != null) {
                        if (oreDic != null) {
                            if (main instanceof String)
                                return oreDic.equals(main);
                            else {
                                ItemStack mainItem = null;
                                if (main instanceof Item)
                                    mainItem = new ItemStack((Item) main);
                                else if (main instanceof Block)
                                    mainItem = new ItemStack((Block) main);
                                else if (main instanceof ItemStack)
                                    mainItem = (ItemStack) main;
                                if (mainItem == null)
                                    return false;
                                return SimpleUtil.isStackRegisteredAsOreDic(mainItem, oreDic);
                            }
                        } else if (stack != null && stack.getItem() != null) {
                            if (main instanceof String)
                                return SimpleUtil.isStackRegisteredAsOreDic(stack, (String) main);
                            else {
                                ItemStack mainItem = null;
                                if (main instanceof Item)
                                    mainItem = new ItemStack((Item) main);
                                else if (main instanceof Block)
                                    mainItem = new ItemStack((Block) main);
                                else if (main instanceof ItemStack)
                                    mainItem = (ItemStack) main;
                                if (mainItem == null)
                                    return false;
                                return SimpleUtil.areStacksSame(stack, mainItem);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static class InfusionRecipe implements IInfusionRecipe {
        private Object[] ingredients;
        private Object baseItem;
        private ItemStack output;
        private int cost;

        public InfusionRecipe(Object baseItem, ItemStack output, int cost, Object... ingredients) {
            this.baseItem = baseItem;
            this.output = output;
            this.ingredients = ingredients;
            this.cost = cost;
        }

        @Override
        public int getManaCost() {
            return cost;
        }

        @Override
        public Object getBaseItem() {
            return baseItem;
        }

        @Override
        public ItemStack getOutput() {
            return output;
        }

        @Override
        public Object[] getIngredients() {
            return ingredients;
        }
    }
}

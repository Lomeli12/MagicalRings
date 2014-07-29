package net.lomeli.ring.magic;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.lomeli.ring.api.interfaces.IInfusionRegistry;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.item.ModItems;

public class InfusionRegistry implements IInfusionRegistry {
    private LinkedHashMap<ItemStack, Object[]> recipeList;
    private LinkedHashMap<ItemStack, Integer> costList;
    private LinkedHashMap<Object, ItemStack> baseItemList;

    public InfusionRegistry() {
        recipeList = new LinkedHashMap<ItemStack, Object[]>();
        baseItemList = new LinkedHashMap<Object, ItemStack>();
        costList = new LinkedHashMap<ItemStack, Integer>();
        registerInfusionRecipe(new ItemStack(Items.potionitem, 1, 16), new ItemStack(ModItems.food, 1, 3), 0, Items.redstone, Items.redstone, "berryMana", "berryMana");
        registerInfusionRecipe(new ItemStack(Items.nether_star), new ItemStack(ModItems.materials, 1, 6), 20, "berryMana", "gemDiamond", "berryMana", "gemDiamond", "berryMana", "gemDiamond", "berryMana", "gemDiamond");
        registerInfusionRecipe(new ItemStack(ModItems.food, 1, 3), new ItemStack(ModItems.food, 1, 2), 60, Blocks.gold_block, "gemMana", new ItemStack(Items.potionitem, 1, 8197));
        registerInfusionRecipe("blockGlass", new ItemStack(ModBlocks.ctBlock, 9), 15, "berryMana", "berryMana", "berryMana", "berryMana", "blockGlass", "blockGlass", "blockGlass", "blockGlass");
    }

    @Override
    public void registerInfusionRecipe(Object mainItem, ItemStack output, int manaCost, Object... additionalInputs) {
        if (baseItemList.containsKey(mainItem))
            return;
        baseItemList.put(mainItem, output);
        costList.put(output, manaCost);
        if (additionalInputs.length <= 8)
            this.recipeList.put(output, additionalInputs);
        else {
            Object[] obj1 = new Object[8];
            for (int i = 0; i < 8; i++) {
                obj1[i] = additionalInputs[i];
            }
            this.recipeList.put(output, obj1);
        }
    }

    @Override
    public Object getBaseFromOutput(ItemStack output) {
        if (output != null && output.getItem() != null) {
            for (Map.Entry<Object, ItemStack> entry : baseItemList.entrySet()) {
                ItemStack stack = entry.getValue();
                if (stack != null && stack.getItem() != null && SimpleUtil.areStacksSame(stack, output))
                    return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public int getCostFromBase(Object item) {
        ItemStack stack = getOutputFromBase(item);
        return (stack != null && stack.getItem() != null) ? costList.get(stack) : -1;
    }

    @Override
    public int getCostFromOutput(ItemStack output) {
        if (output != null && output.getItem() != null) {
            for (Map.Entry<ItemStack, Integer> entry : costList.entrySet()) {
                ItemStack stack = entry.getKey();
                if (stack != null && stack.getItem() != null && SimpleUtil.areStacksSame(output, stack))
                    return entry.getValue();
            }
        }
        return -1;
    }

    @Override
    public Object[] getRecipeFromOut(ItemStack output) {
        if (output != null && output.getItem() != null) {
            for (Map.Entry<ItemStack, Object[]> entry : recipeList.entrySet()) {
                ItemStack stack = entry.getKey();
                if (stack != null && stack.getItem() != null && SimpleUtil.areStacksSame(output, stack))
                    return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Object[] getRecipeFromBase(Object item) {
        ItemStack stack = getOutputFromBase(item);
        return (stack != null && stack.getItem() != null) ? recipeList.get(stack) : null;
    }

    @Override
    public ItemStack getOutputFromBase(Object item) {
        boolean match = false;
        if (item != null) {
            ItemStack stack = null;
            if (item instanceof Item)
                stack = new ItemStack((Item) item);
            else if (item instanceof Block)
                stack = new ItemStack((Block) item);
            else if (item instanceof ItemStack)
                stack = (ItemStack) item;
            for (Map.Entry<Object, ItemStack> entry : baseItemList.entrySet()) {
                Object obj = entry.getKey();
                if (obj != null) {
                    if (obj instanceof String) {
                        if (item instanceof String)
                            match = obj.equals(item);
                        else if (stack != null)
                            match = SimpleUtil.isStackRegisteredAsOreDic(stack, (String) obj);
                    } else if (obj instanceof Item)
                        match = (stack.getItem() == (Item) obj && stack.getItemDamage() == 0);
                    else if (obj instanceof Block) {
                        if (stack != null) {
                            Block block = Block.getBlockFromItem(stack.getItem());
                            if (block != null)
                                match = (block == (Block) obj && stack.getItemDamage() == 0);
                        }
                    } else if (obj instanceof ItemStack)
                        match = SimpleUtil.areStacksSame(stack, (ItemStack) obj);
                }

                if (match)
                    return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean isItemValid(Object item) {
        return getOutputFromBase(item) != null;
    }
}

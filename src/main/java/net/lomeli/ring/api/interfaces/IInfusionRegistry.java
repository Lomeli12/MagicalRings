package net.lomeli.ring.api.interfaces;

import net.minecraft.item.ItemStack;

/**
 * Do NOT implement! Use instance in RingAPI to use these methods.
 */
public interface IInfusionRegistry {
    /** Add new Infusion Recipe. Can use Item, Block, or OreDictionary name for the base item */
    public void registerInfusionRecipe(Object baseItem, ItemStack output, int manaCost, Object... additionalInputs);

    public Object getBaseFromOutput(ItemStack output);

    /** Get mana cost for recipe */
    public int getCostFromBase(Object item);

    /** Get mana cost for recipe */
    public int getCostFromOutput(ItemStack output);

    public Object[] getRecipeFromOut(ItemStack output);

    public Object[] getRecipeFromBase(Object item);

    public ItemStack getOutputFromBase(Object item);

    /** Check if object is valid base */
    public boolean isItemValid(Object item);
}

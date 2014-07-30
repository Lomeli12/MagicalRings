package net.lomeli.ring.api.interfaces;

import net.minecraft.item.ItemStack;

import net.lomeli.ring.api.interfaces.recipe.IInfusionRecipe;

/**
 * Do NOT implement! Use instance in RingAPI to use these methods.
 */
public interface IInfusionRegistry {
    /** Add new Infusion Recipe. Can use Item, Block, or OreDictionary name for the base item */
    public void registerInfusionRecipe(Object baseItem, ItemStack output, int manaCost, Object... additionalInputs);

    public void registerInfusionRecipe(IInfusionRecipe recipe);

    public IInfusionRecipe getRecipeFromOutput(ItemStack out);

    public IInfusionRecipe getRecipeFromItems(ItemStack... ingredients);

    /** Check if object is valid base */
    public boolean isItemValid(Object item);
}

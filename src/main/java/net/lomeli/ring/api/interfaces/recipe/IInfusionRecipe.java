package net.lomeli.ring.api.interfaces.recipe;

import net.minecraft.item.ItemStack;

public interface IInfusionRecipe {

    public int getManaCost();

    public Object getBaseItem();

    public ItemStack getOutput();

    public Object[] getIngredients();
}

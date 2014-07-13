package net.lomeli.ring.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Do NOT implement! Use instance in RingAPI to use these methods.
 */
public interface IMaterialRegistry {
    /**
     * Register itemstack as new ring material
     */
    public void registerMaterial(ItemStack stack, int rgb, int boost);

    /**
     * Register item as new ring material
     */
    public void registerMaterial(Item item, int rgb, int boost);

    /**
     * Register block as new ring material
     */
    public void registerMaterial(Block block, int rgb, int boost);

    /**
     * Register ore dictionary item as new ring material
     */
    public void registerMaterial(String oreDicName, int rgb, int boost);

    /**
     * Register itemstack as new ring gem
     */
    public void registerGem(ItemStack stack, int rgb, int boost);

    /**
     * Register item as new ring gem
     */
    public void registerGem(Item item, int rgb, int boost);

    /**
     * Register block as new ring gem
     */
    public void registerGem(Block block, int rgb, int boost);

    /**
     * Register ore dictionary item as new ring gem
     */
    public void registerGem(String oreDicName, int rgb, int boost);
}

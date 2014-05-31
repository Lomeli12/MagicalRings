package net.lomeli.ring.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IMaterialRegistry {
    public void registerMaterial(ItemStack stack, int rgb, int boost);
    public void registerMaterial(Item item, int rgb, int boost);
    public void registerMaterial(Block block, int rgb, int boost);
    public void registerMaterial(String oreDicName, int rgb, int boost);
    
    public void registerGem(ItemStack stack, int rgb, int boost);
    public void registerGem(Item item, int rgb, int boost);
    public void registerGem(Block block, int rgb, int boost);
    public void registerGem(String oreDicName, int rgb, int boost);
}

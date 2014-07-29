package net.lomeli.ring.entity.villager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipeList;

import cpw.mods.fml.common.registry.VillagerRegistry;

import net.lomeli.ring.item.ModItems;

public class RingVillager implements VillagerRegistry.IVillageTradeHandler {
    public static final int villagerID = 73346;
    private final List<ItemStack> allowedIngredients = new ArrayList<ItemStack>();

    public RingVillager() {
        super();
        allowedIngredients.add(new ItemStack(ModItems.oreItems, 7, 5));
        allowedIngredients.add(new ItemStack(ModItems.oreItems, 7, 6));
        allowedIngredients.add(new ItemStack(ModItems.oreItems, 7, 7));
        allowedIngredients.add(new ItemStack(ModItems.food, 15, 0));
        allowedIngredients.add(new ItemStack(ModItems.food, 5, 1));
        allowedIngredients.add(new ItemStack(Items.cooked_chicken, 5));
        allowedIngredients.add(new ItemStack(Items.cooked_beef, 5));
        allowedIngredients.add(new ItemStack(Items.cooked_fished, 5));
        allowedIngredients.add(new ItemStack(Items.cooked_porkchop, 5));
        allowedIngredients.add(new ItemStack(Items.cookie, 5));
    }

    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
        if (villager.getProfession() == villagerID) {
            ItemStack stack1, stack2, result;

        }
    }

    private int getNextInt(Random random, int min, int max) {
        return random.nextInt(Math.max(1, (max - min) + 1)) + min;
    }
}

package net.lomeli.ring.entity.villager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import cpw.mods.fml.common.registry.VillagerRegistry;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.api.interfaces.recipe.ISpellEntry;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;

// Mostly copied from Tinker's Construct
public class RingVillager implements VillagerRegistry.IVillageTradeHandler {
    private final List<ItemStack> allowedIngredients = new ArrayList<ItemStack>();
    private final List<ItemStack> outputs = new ArrayList<ItemStack>();
    private final int max = 17;
    private final int min = 7;

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

        outputs.add(new ItemStack(ModBlocks.manaFlower));
        outputs.add(new ItemStack(ModBlocks.altar, 1, 0));
        outputs.add(new ItemStack(ModBlocks.altar, 1, 1));
        outputs.add(new ItemStack(ModBlocks.ringForge));
        outputs.add(new ItemStack(Items.iron_ingot));
        outputs.add(new ItemStack(ModItems.diamondHammer));
        outputs.add(new ItemStack(ModItems.ironHammer));
        outputs.add(new ItemStack(ModItems.book, 1, 0));
        outputs.add(new ItemStack(ModItems.book, 1, 1));
        for (int i = 0; i < Rings.proxy.spellRegistry.getSpellList().size(); i++) {
            ISpellEntry entry = Rings.proxy.spellRegistry.getSpellList().get(i);
            if (entry != null && entry.getSpell() != null) {
                ItemStack stack = new ItemStack(ModItems.spellParchment);
                if (!stack.hasTagCompound())
                    stack.stackTagCompound = new NBTTagCompound();
                stack.getTagCompound().setString(ModLibs.SPELL_ID, entry.getSpellID());
                outputs.add(stack);
            }
        }
    }

    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random rand) {
        if (villager.getProfession() == ModLibs.villagerID) {
            ItemStack stack1, stack2;
            for (ItemStack result : outputs) {
                int num = rand.nextInt(Math.max(1, (max - min) + 1)) + min;

                stack1 = getIngredient(rand, num);
                if (stack1.stackSize < 13)
                    stack2 = getIngredient(rand, stack1);
                else
                    stack2 = null;

                ItemStack output = result.copy();
                output.stackSize = calcStackSize(stack1, stack2);
                recipeList.addToListWithCheck(new MerchantRecipe(stack1, stack2, output));
            }
        }
    }

    private int calcStackSize(ItemStack ingredient, ItemStack ingredient2) {
        if (ingredient == null)
            return 1;
        int num = ingredient.stackSize;
        if (ingredient2 != null)
            num += ingredient2.stackSize;

        return Math.max(1, Math.round((num - 5) / 4));
    }

    private ItemStack getIngredient(Random random, ItemStack ingredient) {
        int sc;
        ItemStack is;
        int tries = 0;
        while (true) {
            sc = getNextInt(random, 0, allowedIngredients.size() - 1);
            is = allowedIngredients.get(sc);

            if (is != ingredient || is.getItemDamage() != ingredient.getItemDamage())
                break;

            tries++;
            if (tries == 5)
                return null;
        }
        int num = getNextInt(random, 0, Math.min(is.stackSize, max - ingredient.stackSize));
        return is.copy().splitStack(num);
    }

    private ItemStack getIngredient(Random random, int num) {
        int sc = getNextInt(random, 0, allowedIngredients.size() - 1);
        ItemStack item = allowedIngredients.get(sc);
        return item.copy().splitStack(Math.min(num, item.stackSize));
    }

    private int getNextInt(Random random, int min, int max) {
        return random.nextInt(Math.max(1, (max - min) + 1)) + min;
    }
}

package net.lomeli.ring.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.api.interfaces.ISpellRegistry;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.magic.spells.*;

public class MagicHandler implements ISpellRegistry {
    private List<ISpell> registeredSpells = new ArrayList<ISpell>();
    private HashMap<ISpell, Object[]> spellRecipes = new HashMap<ISpell, Object[]>();

    public MagicHandler() {
        this.registerSpell(new EnderPort(), Items.ender_pearl, Items.ender_eye, Blocks.end_stone, Blocks.diamond_block);
        this.registerSpell(new FriendlyFire(), Items.ender_pearl, Items.diamond_sword, Items.bone, Items.spider_eye, Items.gunpowder, Items.blaze_rod, Items.ghast_tear, Items.magma_cream);
        this.registerSpell(new FireWrath(), Items.flint_and_steel, Items.blaze_powder, Items.blaze_rod, Items.fire_charge, Items.lava_bucket, new ItemStack(ModItems.materials, 1, 2));
        this.registerSpell(new HeavenStrike(), Items.iron_ingot, "gemDiamond", Items.water_bucket, Items.redstone, new ItemStack(ModItems.materials, 1, 3));
        this.registerSpell(new AngelKiss(), Items.feather, new ItemStack(Items.potionitem, 1, 8261));
        this.registerSpell(new Harvest(), new ItemStack(Items.potionitem, 1, 8193), Items.iron_hoe, Items.wheat_seeds, Blocks.red_flower, new ItemStack(Items.dye, 1, 15));
        this.registerSpell(new Disarm(), Blocks.chest, Items.stone_sword, Items.bow, Items.redstone, new ItemStack(ModItems.materials, 1, 4));
        this.registerSpell(new Rearm(), Items.stone_sword, Items.bow, Items.arrow, Items.flint);
        this.registerSpell(new SwiftWind(), Items.nether_star, Blocks.diamond_block, Items.feather, Items.ender_pearl, Items.fireworks, new ItemStack(ModItems.materials, 1, 0));
        this.registerSpell(new BeingDisplacement(), Items.lead, Items.ender_pearl, Items.wheat, Items.wheat_seeds, Items.carrot_on_a_stick);
    }

    public static List<ISpell> getAllSpells() {
        return getMagicHandler().registeredSpells;
    }

    public static MagicHandler getMagicHandler() {
        return Rings.proxy.magicHandler;
    }

    public static ISpell getSpellLazy(int index) {
        return getMagicHandler().getSpell(index);
    }

    @Override
    public void registerSpell(ISpell spell, Object... obj) {
        if (this.registeredSpells.contains(spell))
            return;
        this.registeredSpells.add(spell);
        if (obj.length <= 8)
            this.spellRecipes.put(spell, obj);
        else {
            Object[] obj1 = new Object[8];
            for (int i = 0; i < 8; i++) {
                obj1[i] = obj[i];
            }
            this.spellRecipes.put(spell, obj1);
        }
    }

    public ISpell getSpell(int index) {
        if (index >= 0 && index < this.registeredSpells.size())
            return this.registeredSpells.get(index);
        return null;
    }

    public Object[] getSpellRecipe(int index) {
        ISpell spell = getSpell(index);
        return spell == null ? null : this.spellRecipes.get(spell);
    }

    @Override
    public List<ISpell> getReisteredSpells() {
        return getAllSpells();
    }

    @Override
    public ISpell getSpell(String spell) {
        for (ISpell sp : getReisteredSpells()) {
            if (sp.getUnlocalizedName().equals(spell))
                return sp;
        }
        return null;
    }

    @Override
    public Object[] getSpellRecipe(ISpell spell) {
        for (ISpell sp : getReisteredSpells()) {
            if (sp.getUnlocalizedName().equals(spell.getUnlocalizedName()))
                return this.spellRecipes.get(sp);
        }
        return null;
    }
}
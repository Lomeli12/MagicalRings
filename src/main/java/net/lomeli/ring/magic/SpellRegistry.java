package net.lomeli.ring.magic;

import java.util.*;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.api.interfaces.ISpellRegistry;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.spells.*;

public class SpellRegistry implements ISpellRegistry {
    private LinkedHashMap<String, ISpell> registeredSpells;
    private LinkedHashMap<String, Object[]> spellRecipes;

    public SpellRegistry() {
        this.registeredSpells = new LinkedHashMap<String, ISpell>();
        this.spellRecipes = new LinkedHashMap<String, Object[]>();
        this.registerSpell(new EnderPort(), ModLibs.MOD_ID + ":enderPort", Items.ender_pearl, Items.ender_eye, Blocks.end_stone, Blocks.diamond_block);
        this.registerSpell(new FriendlyFire(), ModLibs.MOD_ID + ":friendlyFire", Items.ender_pearl, Items.diamond_sword, Items.bone, Items.spider_eye, Items.gunpowder, Items.blaze_rod, Items.ghast_tear, Items.magma_cream);
        this.registerSpell(new FireWrath(), ModLibs.MOD_ID + ":fireyWrath", Items.flint_and_steel, Items.blaze_powder, Items.blaze_rod, Items.fire_charge, Items.lava_bucket, new ItemStack(ModItems.materials, 1, 2));
        this.registerSpell(new HeavenStrike(), ModLibs.MOD_ID + ":heavenStrike", "ingotIron", "gemDiamond", Items.water_bucket, Items.redstone, new ItemStack(ModItems.materials, 1, 3));
        this.registerSpell(new AngelKiss(), ModLibs.MOD_ID + ":angelKiss", Items.feather, new ItemStack(Items.potionitem, 1, 8261));
        this.registerSpell(new Harvest(), ModLibs.MOD_ID + ":harvest", new ItemStack(Items.potionitem, 1, 8193), Items.iron_hoe, Items.wheat_seeds, Blocks.red_flower, new ItemStack(Items.dye, 1, 15));
        this.registerSpell(new Disarm(), ModLibs.MOD_ID + ":disarm", Blocks.chest, Items.stone_sword, Items.bow, Items.redstone, new ItemStack(ModItems.materials, 1, 4));
        this.registerSpell(new Rearm(), ModLibs.MOD_ID + ":rearm", Items.stone_sword, Items.bow, Items.arrow, Items.flint);
        this.registerSpell(new SwiftWind(), ModLibs.MOD_ID + ":swiftWind", Items.nether_star, Blocks.diamond_block, Items.feather, Items.ender_pearl, Items.fireworks, new ItemStack(ModItems.materials, 1, 0));
        this.registerSpell(new BeingDisplacement(), ModLibs.MOD_ID + ":beingDisplacement", Items.lead, Items.ender_pearl, Items.wheat, Items.wheat_seeds, Items.carrot_on_a_stick);
        this.registerSpell(new ClearWaters(), ModLibs.MOD_ID + ":clearWaters", Blocks.glass, Items.iron_helmet, new ItemStack(ModItems.materials, 1, 4), new ItemStack(Items.fish, 1, 3), new ItemStack(Items.potionitem, 1, 8230));
    }

    @Override
    public void registerSpell(ISpell spell, String id, Object... obj) {
        if (this.registeredSpells.containsKey(id))
            return;
        this.registeredSpells.put(id, spell);
        if (obj.length <= 8)
            this.spellRecipes.put(id, obj);
        else {
            Object[] obj1 = new Object[8];
            for (int i = 0; i < 8; i++) {
                obj1[i] = obj[i];
            }
            this.spellRecipes.put(id, obj1);
        }
    }

    @Override
    public LinkedHashMap<String, ISpell> getReisteredSpells() {
        return this.registeredSpells;
    }

    @Override
    public ISpell getSpell(String id) {
        return this.registeredSpells.get(id);
    }

    @Override
    public Object[] getSpellRecipe(String id) {
        return this.spellRecipes.get(id);
    }

    @Override
    public String getSpellID(ISpell spell) {
        for (Map.Entry<String, ISpell> entry : this.registeredSpells.entrySet()) {
            ISpell iSpell = entry.getValue();
            if (iSpell != null && iSpell.getUnlocalizedName().equals(spell.getUnlocalizedName()))
                return entry.getKey();
        }
        return null;
    }
}
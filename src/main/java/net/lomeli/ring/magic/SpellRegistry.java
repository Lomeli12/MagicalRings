package net.lomeli.ring.magic;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.api.interfaces.ISpellRegistry;
import net.lomeli.ring.api.interfaces.recipe.ISpellEntry;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.spells.*;

public class SpellRegistry implements ISpellRegistry {
    private List<ISpellEntry> spellList;
    //private LinkedHashMap<String, ISpell> registeredSpells;
    //private LinkedHashMap<String, Object[]> spellRecipes;

    public SpellRegistry() {
        this.spellList = new ArrayList<ISpellEntry>();
        //this.registeredSpells = new LinkedHashMap<String, ISpell>();
        //this.spellRecipes = new LinkedHashMap<String, Object[]>();
        this.registerSpell(new EnderPort(), ModLibs.MOD_ID + ":enderPort", Items.ender_pearl, Items.ender_eye, Blocks.end_stone, Blocks.diamond_block);
        this.registerSpell(new FriendlyFire(), ModLibs.MOD_ID + ":friendlyFire", Items.ender_pearl, Items.diamond_sword, Items.bone, Items.spider_eye, Items.gunpowder, Items.blaze_rod, Items.ghast_tear, Items.magma_cream);
        this.registerSpell(new FireWrath(), ModLibs.MOD_ID + ":fireyWrath", Items.flint_and_steel, Items.blaze_powder, Items.blaze_rod, Items.fire_charge, Items.lava_bucket, "magmaStone");
        this.registerSpell(new HeavenStrike(), ModLibs.MOD_ID + ":heavenStrike", "ingotIron", "gemDiamond", Items.water_bucket, Items.redstone, "gemCharged");
        this.registerSpell(new AngelKiss(), ModLibs.MOD_ID + ":angelKiss", Items.feather, new ItemStack(Items.potionitem, 1, 8261));
        this.registerSpell(new Harvest(), ModLibs.MOD_ID + ":harvest", new ItemStack(Items.potionitem, 1, 8193), Items.iron_hoe, Items.wheat_seeds, Blocks.red_flower, new ItemStack(Items.dye, 1, 15));
        this.registerSpell(new Disarm(), ModLibs.MOD_ID + ":disarm", Blocks.chest, Items.stone_sword, Items.bow, Items.redstone, "tentacle");
        this.registerSpell(new Rearm(), ModLibs.MOD_ID + ":rearm", Items.stone_sword, Items.bow, Items.arrow, Items.flint);
        this.registerSpell(new SwiftWind(), ModLibs.MOD_ID + ":swiftWind", Items.nether_star, Blocks.diamond_block, Items.feather, Items.ender_pearl, Items.fireworks, "batWing");
        this.registerSpell(new BeingDisplacement(), ModLibs.MOD_ID + ":beingDisplacement", Items.lead, Items.ender_pearl, Items.wheat, Items.wheat_seeds, Items.carrot_on_a_stick);
        this.registerSpell(new ClearWaters(), ModLibs.MOD_ID + ":clearWaters", Blocks.glass, Items.iron_helmet, "tentacle", new ItemStack(Items.fish, 1, 3), new ItemStack(Items.potionitem, 1, 8230));
    }

    @Override
    public void registerSpell(ISpell spell, String id, Object... obj) {
        Object[] obj1 = new Object[8];
        if (obj.length <= 8)
            obj1 = obj;
        else {
            for (int i = 0; i < 8; i++)
                obj1[i] = obj[i];
        }
        this.registerSpell(new SpellEntry(spell, id, obj1));
    }

    public void registerSpell(ISpellEntry entry) {
        if (!this.spellList.contains(entry))
            this.spellList.add(entry);
    }

    @Override
    public List<ISpellEntry> getSpellList() {
        return this.spellList;
    }

    @Override
    public ISpell getSpell(String id) {
        for (int i = 0; i < this.spellList.size(); i++) {
            ISpellEntry entry = this.spellList.get(i);
            if (entry != null && entry.getSpellID().equals(id))
                return entry.getSpell();
        }
        return null;
    }

    @Override
    public Object[] getSpellRecipe(String id) {
        for (int i = 0; i < this.spellList.size(); i++) {
            ISpellEntry entry = this.spellList.get(i);
            if (entry != null && entry.getSpellID().equals(id))
                return entry.requireMaterials();
        }
        return null;
    }

    @Override
    public String getSpellID(ISpell spell) {
        for (int i = 0; i < this.spellList.size(); i++) {
            ISpellEntry entry = this.spellList.get(i);
            if (entry != null && entry.getSpell().getUnlocalizedName().equals(spell.getUnlocalizedName()))
                return entry.getSpellID();
        }
        return null;
    }

    public static class SpellEntry implements ISpellEntry {
        private Object[] items;
        private String id;
        private ISpell spell;

        public SpellEntry(ISpell spell, String id, Object...items) {
            this.spell = spell;
            this.id = id;
            this.items = items;
        }

        @Override
        public Object[] requireMaterials() {
            return items;
        }

        @Override
        public String getSpellID() {
            return id;
        }

        @Override
        public ISpell getSpell() {
            return spell;
        }
    }
}
package net.lomeli.ring.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.spells.AngelKiss;
import net.lomeli.ring.magic.spells.EnderPort;
import net.lomeli.ring.magic.spells.FireWrath;
import net.lomeli.ring.magic.spells.FriendlyFire;
import net.lomeli.ring.magic.spells.HeavenStrike;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketUpdatePlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MagicHandler {
    private List<ISpell> registeredSpells = new ArrayList<ISpell>();
    private HashMap<ISpell, Object[]> spellRecipes = new HashMap<ISpell, Object[]>();

    public MagicHandler() {
        this.registerSpell(new EnderPort(), Items.ender_pearl, Items.ender_eye, Blocks.end_stone, Blocks.diamond_block);
        this.registerSpell(new FriendlyFire(), Items.ender_pearl, Items.iron_sword, Items.bone, Items.spider_eye, Items.gunpowder, Items.blaze_rod, Items.ghast_tear, Items.magma_cream);
        this.registerSpell(new FireWrath(), Items.flint_and_steel, Items.blaze_powder, Items.blaze_rod, Items.fire_charge);
        this.registerSpell(new HeavenStrike(), Items.iron_ingot, "gemDiamond", Items.water_bucket, Items.redstone);
        this.registerSpell(new AngelKiss(), Items.feather, new ItemStack(Items.potionitem, 1, 8261));
    }

    public static List<ISpell> getReisteredSpells() {
        return getMagicHandler().registeredSpells;
    }

    public static MagicHandler getMagicHandler() {
        return Rings.proxy.magicHandler;
    }

    public static ISpell getSpellLazy(int index) {
        return getMagicHandler().getSpell(index);
    }

    public static void modifyPlayerMP(EntityPlayer player, int cost) {
        if (!player.capabilities.isCreativeMode && player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
            NBTTagCompound tag = player.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA);
            int mp = tag.getInteger(ModLibs.PLAYER_MP), max = tag.getInteger(ModLibs.PLAYER_MAX);
            mp += cost;
            if (mp > max)
                mp = max;
            if (mp < 0)
                mp = 0;
            PacketHandler.sendEverywhere(new PacketUpdatePlayerMP(player, mp, max));
        }
    }

    public static void modifyPlayerMaxMP(EntityPlayer player, int newMax) {
        if (player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
            NBTTagCompound tag = player.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA);
            int mp = tag.getInteger(ModLibs.PLAYER_MP);
            if (mp > newMax)
                mp = newMax;
            PacketHandler.sendEverywhere(new PacketUpdatePlayerMP(player, mp, newMax));
        }else {
            PacketHandler.sendEverywhere(new PacketUpdatePlayerMP(player, 0, newMax));
        }
    }

    public static boolean canUse(EntityPlayer player, int cost) {
        if (player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
            NBTTagCompound tag = player.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA);
            return tag.getInteger(ModLibs.PLAYER_MP) >= cost;
        }
        return false;
    }

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

}
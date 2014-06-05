package net.lomeli.ring.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.IMagicHandler;
import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.spells.AngelKiss;
import net.lomeli.ring.magic.spells.Disarm;
import net.lomeli.ring.magic.spells.EnderPort;
import net.lomeli.ring.magic.spells.FireWrath;
import net.lomeli.ring.magic.spells.FriendlyFire;
import net.lomeli.ring.magic.spells.Harvest;
import net.lomeli.ring.magic.spells.HeavenStrike;
import net.lomeli.ring.magic.spells.Rearm;
import net.lomeli.ring.magic.spells.SwiftWind;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketModifyMp;

public class MagicHandler implements IMagicHandler {
    private List<ISpell> registeredSpells = new ArrayList<ISpell>();
    private HashMap<ISpell, Object[]> spellRecipes = new HashMap<ISpell, Object[]>();

    public MagicHandler() {
        this.registerSpell(new EnderPort(), Items.ender_pearl, Items.ender_eye, Blocks.end_stone, Blocks.diamond_block);
        this.registerSpell(new FriendlyFire(), Items.ender_pearl, Items.diamond_sword, Items.bone, Items.spider_eye, Items.gunpowder, Items.blaze_rod, Items.ghast_tear, Items.magma_cream);
        this.registerSpell(new FireWrath(), Items.flint_and_steel, Items.blaze_powder, Items.blaze_rod, Items.fire_charge, Items.lava_bucket);
        this.registerSpell(new HeavenStrike(), Items.iron_ingot, "gemDiamond", Items.water_bucket, Items.redstone);
        this.registerSpell(new AngelKiss(), Items.feather, new ItemStack(Items.potionitem, 1, 8261));
        this.registerSpell(new Harvest(), new ItemStack(Items.potionitem, 1, 8193), Items.iron_hoe, Items.wheat_seeds, Blocks.red_flower, new ItemStack(Items.dye, 1, 15));
        this.registerSpell(new Disarm(), Blocks.chest, Items.stone_sword, Items.bow, Items.redstone);
        this.registerSpell(new Rearm(), Items.stone_sword, Items.bow, Items.arrow, Items.flint);
        this.registerSpell(new SwiftWind(), Items.nether_star, Blocks.diamond_block, Items.feather, Items.ender_pearl, Items.fireworks);
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

    public static void modifyPlayerMP(EntityPlayer player, int cost) {
        if (!player.capabilities.isCreativeMode && getMagicHandler().getPlayerTag(player) != null)
            PacketHandler.sendToServer(new PacketModifyMp(player, cost, 0));
    }

    public static void modifyPlayerMaxMP(EntityPlayer player, int newMax) {
        PacketHandler.sendToServer(new PacketModifyMp(player, 0, newMax));
    }

    public static boolean canUse(EntityPlayer player, int cost) {
        if (getMagicHandler().getPlayerTag(player) != null) {
            NBTTagCompound tag = getMagicHandler().getPlayerTag(player);
            return tag.getInteger(ModLibs.PLAYER_MP) >= cost;
        }
        return false;
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
    public ISpell getSpell(ISpell spell) {
        for (ISpell sp : getReisteredSpells()) {
            if (sp.getUnlocalizedName().equals(spell.getUnlocalizedName()))
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

    @Override
    public boolean canPlayerUse(EntityPlayer player, int cost) {
        return canUse(player, cost);
    }

    @Override
    public void useMP(EntityPlayer player, int cost) {
        modifyPlayerMP(player, -cost);
    }

    @Override
    public void modifyMax(EntityPlayer player, int newMax) {
        modifyPlayerMaxMP(player, newMax);
    }

    @Override
    public int getPlayerMP(EntityPlayer player) {
        if (player.getEntityData() != null && getPlayerTag(player) != null) {
            NBTTagCompound tag = getPlayerTag(player);
            return tag.getInteger(ModLibs.PLAYER_MP);
        }
        return 0;
    }

    @Override
    public int getPlayerMaxMP(EntityPlayer player) {
        if (player.getEntityData() != null && getPlayerTag(player) != null) {
            NBTTagCompound tag = getPlayerTag(player);
            return tag.getInteger(ModLibs.PLAYER_MAX);
        }
        return 0;
    }

    @Override
    public boolean canPlayerUseMagic(EntityPlayer player) {
        return getPlayerTag(player) != null;
    }

    @Override
    public NBTTagCompound getPlayerTag(EntityPlayer player) {
        NBTTagCompound tag = null;

        if (player != null) {
            NBTTagCompound persisted = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            if (persisted == null)
                return tag;
            if (persisted.hasKey(ModLibs.PLAYER_DATA))
                tag = persisted.getCompoundTag(ModLibs.PLAYER_DATA);
        }

        return tag;
    }
}
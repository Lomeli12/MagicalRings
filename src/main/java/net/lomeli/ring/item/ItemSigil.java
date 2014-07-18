package net.lomeli.ring.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import net.lomeli.ring.api.interfaces.IBookEntry;
import net.lomeli.ring.lib.ModLibs;

public class ItemSigil extends ItemRings implements IBookEntry {
    public ItemSigil(String t) {
        super(t, true);
        this.setMaxDamage(40);
        this.setMaxStackSize(1);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.rare;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean par) {
        info.add(StatCollector.translateToLocal(ModLibs.USES_LEFT) + " " + (itemStack.getMaxDamage() - itemStack.getItemDamage()));
    }

    @Override
    public String getBookPage(int metadata) {
        return ModLibs.MOD_ID.toLowerCase() + ".sigil";
    }

    @Override
    public int getData() {
        return 0;
    }
}

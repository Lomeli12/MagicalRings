package net.lomeli.ring.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSpellParchment extends ItemRings {

    public ItemSpellParchment(String texture) {
        super(texture);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.rare;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < MagicHandler.getAllSpells().size(); i++) {
            ISpell spell = MagicHandler.getSpellLazy(i);
            if (spell != null)
                list.add(new ItemStack(item, 1, i));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ISpell spell = MagicHandler.getSpellLazy(stack.getItemDamage());
        if (spell != null) {
            list.add(StatCollector.translateToLocal(ModLibs.SPELL) + ": " + StatCollector.translateToLocal(spell.getUnlocalizedName()));
            if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())) {
                Object[] required = MagicHandler.getMagicHandler().getSpellRecipe(stack.getItemDamage());
                if (required != null && required.length > 0) {
                    list.add(StatCollector.translateToLocal(ModLibs.REQUIRED_ITEMS));
                    for (Object obj : required) {
                        if (obj instanceof String)
                            list.add(StatCollector.translateToLocal(ModLibs.ORE_DIC) + (String) obj);
                        else {
                            ItemStack item = null;
                            if (obj instanceof ItemStack)
                                item = (ItemStack) obj;
                            if (obj instanceof Item)
                                item = new ItemStack((Item) obj);
                            if (obj instanceof Block)
                                item = new ItemStack((Block) obj);

                            if (item != null)
                                list.add(item.getDisplayName());
                        }
                    }
                }
            }
        }
    }
}

package net.lomeli.ring.item;

import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IBookEntry;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.lib.ModLibs;

public class ItemSpellParchment extends ItemRings implements IBookEntry {

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (Map.Entry<String, ISpell> entry : Rings.proxy.spellRegistry.getReisteredSpells().entrySet()) {
            if (entry.getValue() != null) {
                ItemStack stack = new ItemStack(item);
                if (!stack.hasTagCompound())
                    stack.stackTagCompound = new NBTTagCompound();
                stack.getTagCompound().setString(ModLibs.SPELL_ID, entry.getKey());
                list.add(stack);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (stack != null && stack.hasTagCompound()) {
            String spellID = SimpleUtil.getSpellIdFromTag(stack.getTagCompound());
            ISpell spell = Rings.proxy.spellRegistry.getSpell(spellID);
            if (spell != null) {
                list.add(StatCollector.translateToLocal(ModLibs.SPELL) + ": " + StatCollector.translateToLocal(spell.getUnlocalizedName()));
                if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())) {
                    Object[] required = Rings.proxy.spellRegistry.getSpellRecipe(spellID);
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

    @Override
    public String getBookPage(int metadata) {
        return ModLibs.MOD_ID.toLowerCase() + ".spellParchment";
    }

    @Override
    public int getData() {
        return 0;
    }
}

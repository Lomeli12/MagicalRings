package net.lomeli.ring.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IBookEntry;
import net.lomeli.ring.lib.ModLibs;

public class ItemSpellBook extends ItemRings implements IBookEntry {
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public ItemSpellBook(String texture) {
        super(texture);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        iconArray = new IIcon[2];
        iconArray[0] = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":spellbook");
        iconArray[1] = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":materialbook");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return par1 < iconArray.length ? iconArray[par1] : iconArray[0];
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < iconArray.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player != null && !player.isSneaking()) {
            if (!world.isRemote && stack.getItemDamage() == 0) {
                if (!Rings.proxy.manaHandler.playerHasSession(player))
                    Rings.proxy.manaHandler.addPlayerSession(player, 0, ModLibs.BASE_MP);
            }
            player.openGui(Rings.instance, ModLibs.BOOK_GUI, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (player != null) {
            if (!world.isRemote && stack.getItemDamage() == 0) {
                if (!Rings.proxy.manaHandler.playerHasSession(player))
                    Rings.proxy.manaHandler.addPlayerSession(player, 0, ModLibs.BASE_MP);
            }
            player.openGui(Rings.instance, ModLibs.BOOK_GUI, world, x, y, z);
        }
        return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public String getBookPage(int metadata) {
        return ModLibs.MOD_ID.toLowerCase() + "." + (metadata == 0 ? "spellBook" : "materialBook");
    }

    @Override
    public int getData() {
        return 0;
    }
}

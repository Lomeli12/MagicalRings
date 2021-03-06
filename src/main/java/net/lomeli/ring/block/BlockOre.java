package net.lomeli.ring.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;

public class BlockOre extends BlockRings {
    private String blockTexture;

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public BlockOre(String texture) {
        super(Material.rock, texture);
        this.setHardness(4f);
        this.setResistance(20);
        this.blockTexture = texture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        iconArray = new IIcon[8];
        for (int i = 0; i < iconArray.length; i++) {
            iconArray[i] = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":ores/" + this.blockTexture + "_" + i);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 8; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.iconArray[meta % this.iconArray.length];
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        return this.damageDropped(world.getBlockMetadata(x, y, z));
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        if (metadata > 1)
            items.add(new ItemStack(ModItems.oreItems, 1 + (fortune > 0 ? fortune - 1 : 0), metadata + 1));
        else
            items.add(new ItemStack(this, 1, metadata));
        return items;
    }

    public static class ItemBlockOre extends ItemBlock {

        public ItemBlockOre(Block p_i45328_1_) {
            super(p_i45328_1_);
            this.setMaxDamage(0);
            this.setHasSubtypes(true);
        }

        @Override
        public int getMetadata(int par1) {
            return par1;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IIcon getIconFromDamage(int par1) {
            return this.field_150939_a.getIcon(0, par1);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        @SideOnly(Side.CLIENT)
        public void getSubItems(Item item, CreativeTabs tab, List list) {
            for (int i = 0; i < 8; i++) {
                list.add(new ItemStack(item, 1, i));
            }
        }

        @Override
        public String getUnlocalizedName(ItemStack stack) {
            return this.getUnlocalizedName() + "." + stack.getItemDamage();
        }

    }

}

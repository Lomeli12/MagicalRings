package net.lomeli.ring.item;

import java.util.List;

import net.lomeli.ring.Rings;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMagicFood extends ItemFood {
    protected String itemTexture;

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray = new IIcon[3];

    public ItemMagicFood() {
        super(0, 0, false);
        this.setCreativeTab(Rings.modTab);
        this.setHasSubtypes(true);
        this.setAlwaysEdible();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par1ItemStack.getItemDamage() == 2)
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
    }
    
    

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (!player.isSneaking() && stack.getItemDamage() == 0) {
            if (world.getBlock(x, y, z) == Blocks.farmland && world.isAirBlock(x, y + 1, z)) {
                world.setBlock(x, y + 1, z, ModBlocks.onionBlock);
                if (!player.capabilities.isCreativeMode)
                    stack.stackSize--;
                return true;
            }
        }
        return super.onItemUse(stack, player, world, x, y, z, side, par8, par9, par10);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return stack.getItemDamage() == 2 ? EnumRarity.epic : super.getRarity(stack);
    }

    @Override
    public int func_150905_g(ItemStack stack) {
        return stack.getItemDamage() == 0 ? 3 : stack.getItemDamage() == 1 ? 5 : 0;
    }

    @Override
    public float func_150906_h(ItemStack stack) {
        return stack.getItemDamage() == 0 ? 0.3f : stack.getItemDamage() == 1 ? 1f : 0;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        if (stack.getItemDamage() == 2) {
            if (player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
                NBTTagCompound tag = player.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA);
                int max = tag.getInteger(ModLibs.PLAYER_MAX);
                if (max < 1500)
                    MagicHandler.modifyPlayerMaxMP(player, max + 100);
                stack.stackSize--;
                return stack;
            }
        }
        return super.onEaten(stack, world, player);
    }

    @Override
    public IIcon getIconFromDamage(int par1) {
        return par1 < iconArray.length ? iconArray[par1] : iconArray[0];
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

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
    public Item setUnlocalizedName(String name) {
        return super.setUnlocalizedName(ModLibs.MOD_ID.toLowerCase() + "." + name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        for (int i = 0; i < iconArray.length; i++) {
            iconArray[i] = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":food_" + i);
        }
    }

}

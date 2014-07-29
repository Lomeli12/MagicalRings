package net.lomeli.ring.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IBookEntry;
import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.lib.ModLibs;

public class ItemMagicFood extends ItemFood implements IPlantable, IBookEntry {

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

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
            if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack)) {
                if (world.getBlock(x, y, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, this) && world.isAirBlock(x, y + 1, z) && side == 1) {
                    world.setBlock(x, y + 1, z, ModBlocks.onionBlock);
                    if (!player.capabilities.isCreativeMode)
                        stack.stackSize--;
                    return true;
                } else
                    return false;
            }
        }
        return super.onItemUse(stack, player, world, x, y, z, side, par8, par9, par10);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return stack.getItemDamage() == 2 || stack.getItemDamage() == 3 ? EnumRarity.epic : super.getRarity(stack);
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return stack.getItemDamage() == 2 || stack.getItemDamage() == 3 ? EnumAction.drink : EnumAction.eat;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack, int pass) {
        return stack.getItemDamage() >= 2;
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
        if (!world.isRemote) {
            if (stack.getItemDamage() == 2) {
                if (Rings.proxy.manaHandler.playerHasSession(player)) {
                    IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(player);
                    session.setMaxMana(session.getMaxMana() + 100);
                    Rings.proxy.manaHandler.updatePlayerSession(session, world.provider.dimensionId);
                }
                if (!player.capabilities.isCreativeMode) {
                    stack.stackSize--;
                    player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
                }
                return stack;
            } else if (stack.getItemDamage() == 3) {
                if (Rings.proxy.manaHandler.playerHasSession(player)) {
                    IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(player);
                    session.adjustMana(75, false);
                    Rings.proxy.manaHandler.updatePlayerSession(session, world.provider.dimensionId);
                }
                if (!player.capabilities.isCreativeMode) {
                    stack.stackSize--;
                    player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
                }
                return stack;
            }
        }
        return super.onEaten(stack, world, player);
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
    public Item setUnlocalizedName(String name) {
        return super.setUnlocalizedName(ModLibs.MOD_ID.toLowerCase() + "." + name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        iconArray = new IIcon[4];
        for (int i = 0; i < iconArray.length; i++) {
            iconArray[i] = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":food_" + i);
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return ModBlocks.onionBlock;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return 0;
    }

    @Override
    public String getBookPage(int metadata) {
        return metadata == 3 ? ModLibs.MOD_ID.toLowerCase() + ".manaPotion" : metadata == 2 ? ModLibs.MOD_ID.toLowerCase() + ".mysteriousPotion" : null;
    }

    @Override
    public int getData() {
        return 0;
    }
}

package net.lomeli.ring.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.lib.ModLibs;

public class ItemMaterial extends ItemRings implements IPlantable {
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public ItemMaterial() {
        super("material");
        this.setHasSubtypes(true);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        if (world.rand.nextInt(1000) < 5 && !world.isRemote) {
            switch (stack.getItemDamage()) {
                case 2:
                    if (entity instanceof EntityPlayer) {
                        if (!((EntityPlayer) entity).capabilities.isCreativeMode)
                            entity.setFire(10);
                    } else
                        entity.setFire(10);
                    break;
                case 3:
                    world.spawnEntityInWorld(new EntityLightningBolt(world, entity.posX + SimpleUtil.randDist(3), entity.posY + SimpleUtil.randDist(3), entity.posZ + SimpleUtil.randDist(3)));
                    break;
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (stack.getItemDamage() == 6) {
            if (!world.isRemote) {
                if (Rings.proxy.manaHandler.playerHasSession(player)) {
                    IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(player);
                    if (session != null && session.getMana() < session.getMaxMana()) {
                        session.adjustMana(session.getMaxMana() - session.getMana(), false);

                        Rings.proxy.manaHandler.updatePlayerSession(session, world.provider.dimensionId);
                        if (!player.capabilities.isCreativeMode)
                            stack.stackSize--;
                    }
                }
            }
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (stack.getItemDamage() == 2) {
            switch (side) {
                case 0:
                    --y;
                    break;
                case 1:
                    ++y;
                    break;
                case 2:
                    --z;
                    break;
                case 3:
                    ++z;
                    break;
                case 4:
                    --x;
                    break;
                case 5:
                    ++x;
                    break;
            }

            if (!player.canPlayerEdit(x, y, z, side, stack))
                return false;
            else {
                if (world.isAirBlock(x, y, z)) {
                    world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                    world.setBlock(x, y, z, Blocks.fire);
                }
                return true;
            }
        } else if (stack.getItemDamage() == 5) {
            if (!player.isSneaking() && player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack)) {
                if (world.getBlock(x, y, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, this) && world.isAirBlock(x, y + 1, z) && side == 1) {
                    world.setBlock(x, y + 1, z, ModBlocks.manaFlower);
                    if (!player.capabilities.isCreativeMode)
                        stack.stackSize--;
                    return true;
                } else
                    return false;
            }
        }
        return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        iconArray = new IIcon[7];
        for (int i = 0; i < iconArray.length; i++) {
            iconArray[i] = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":materials/" + this.itemTexture + "_" + i);
        }
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
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return ModBlocks.manaFlower;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return 5;
    }
}

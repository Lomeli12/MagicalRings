package net.lomeli.ring.item;

import java.awt.Color;
import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Interface(iface = "baubles.api.IBauble", modid = "Baubles")
public class ItemMagicRing extends ItemRings implements IBauble {
    @SideOnly(Side.CLIENT)
    private IIcon rl1, rl2, gem;

    public ItemMagicRing(String texture) {
        super(texture);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setCreativeTab(null);
    }

    public int getLayer1RGB(NBTTagCompound nbt) {
        return nbt.getInteger(ModLibs.L1RGB);
    }

    public int getLayer2RGB(NBTTagCompound nbt) {
        return nbt.getInteger(ModLibs.L2RGB);
    }

    public boolean isEdible(NBTTagCompound nbt) {
        return nbt.getBoolean(ModLibs.EDIBLE);
    }

    public boolean hasGem(NBTTagCompound nbt) {
        return nbt.hasKey(ModLibs.HAS_GEM) ? nbt.getBoolean(ModLibs.HAS_GEM) : false;
    }

    public int getGemRGB(NBTTagCompound nbt) {
        return nbt.getInteger(ModLibs.GEM_RGB);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
        if (Loader.isModLoaded("Baubles") ? !(entity instanceof EntityPlayer) : true) {
            if (stack.getTagCompound() != null) {
                NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
                if (tag != null) {
                    if (tag.hasKey(ModLibs.SPELL_ID)) {
                        int spellID = tag.getInteger(ModLibs.SPELL_ID);
                        ISpell spell = MagicHandler.getSpellLazy(spellID);
                        if (spell != null) {
                            int trueCost = spell.cost() + (tag.getInteger(ModLibs.MATERIAL_BOOST) * 5);
                            spell.onUpdateTick(stack, world, entity, par4, par5, tag.getInteger(ModLibs.MATERIAL_BOOST), trueCost, tag.getBoolean(ModLibs.ACTIVE_EFFECT_ENABLED));
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        // Thanks Drable for the fancy new textures
        rl1 = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":ring/ringEdge");
        rl2 = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":ring/ringInner");
        gem = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":ring/ringGem");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            if (stack.getTagCompound() != null) {
                NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
                if (tag != null) {
                    boolean active = tag.getBoolean(ModLibs.ACTIVE_EFFECT_ENABLED);
                    tag.setBoolean(ModLibs.ACTIVE_EFFECT_ENABLED, !active);
                    if (!world.isRemote)
                        player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(ModLibs.ACTIVE_EFFECT) + ": " + !active));
                }
            }
        }else
            this.useRing(stack, player, world, (int) player.posX, (int) player.posY, (int) player.posZ, 0, 0, 0, 0);
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return this.useRing(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    public boolean useRing(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (stack.getTagCompound() != null) {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
            if (tag != null) {
                if (tag.hasKey(ModLibs.SPELL_ID)) {
                    if (!MagicHandler.getMagicHandler().canPlayerUseMagic(player)) {
                        if (!world.isRemote)
                            player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(ModLibs.NO_MANA)));
                        return false;
                    }
                    int spellID = tag.getInteger(ModLibs.SPELL_ID);
                    ISpell spell = MagicHandler.getSpellLazy(spellID);
                    if (spell != null) {
                        int trueCost = -spell.cost() + (tag.getInteger(ModLibs.MATERIAL_BOOST) * 5);
                        if (spell.activateSpell(world, player, x, y, z, side, hitX, hitY, hitZ, tag.getInteger(ModLibs.MATERIAL_BOOST), trueCost)) {
                            if (this.isEdible(tag))
                                world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass) {
        if (renderPass == 1)
            return rl2;
        else if (renderPass == 2) {
            if (stack.getTagCompound() != null && this.hasGem(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG)))
                return gem;
            else
                return this.blankIcon;
        }else
            return rl1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (stack.getTagCompound() == null)
            return Color.WHITE.getRGB();
        else if (renderPass == 2) {
            if (this.hasGem(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG)))
                return this.getGemRGB(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG));
            else
                return this.getLayer1RGB(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG));
        }else {
            if (renderPass == 1)
                return this.getLayer2RGB(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG));
            else
                return this.getLayer1RGB(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey(ModLibs.RING_TAG)) {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
            if (tag.hasKey(ModLibs.SPELL_ID)) {
                int id = tag.getInteger(ModLibs.SPELL_ID);
                ISpell spell = MagicHandler.getSpellLazy(id);
                if (spell != null)
                    list.add(StatCollector.translateToLocal(ModLibs.SPELL) + ": " + StatCollector.translateToLocal(spell.getUnlocalizedName()));
            }
            if (tag.hasKey(ModLibs.MATERIAL_BOOST)) {
                int boost = tag.getInteger(ModLibs.MATERIAL_BOOST);
                if (boost != 0)
                    list.add((boost > 0 ? "+" + boost : boost) + " " + StatCollector.translateToLocal(ModLibs.BOOST));
            }
        }
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        if (stack.getTagCompound() != null) {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
            if (tag != null) {
                if (tag.hasKey(ModLibs.SPELL_ID)) {
                    int spellID = tag.getInteger(ModLibs.SPELL_ID);
                    ISpell spell = MagicHandler.getSpellLazy(spellID);
                    if (spell != null) {
                        int trueCost = spell.cost() + (tag.getInteger(ModLibs.MATERIAL_BOOST) * 5);
                        spell.onUpdateTick(stack, player.worldObj, player, 0, true, tag.getInteger(ModLibs.MATERIAL_BOOST), trueCost, tag.getBoolean(ModLibs.ACTIVE_EFFECT_ENABLED));
                    }
                }
            }
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        if (itemstack.getTagCompound() != null)
            return itemstack.getTagCompound().hasKey(ModLibs.RING_TAG);
        return false;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }
}

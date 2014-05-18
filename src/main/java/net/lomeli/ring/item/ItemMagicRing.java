package net.lomeli.ring.item;

import java.awt.Color;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.ISpell;
import net.lomeli.ring.magic.MagicHandler;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketUpdatePlayerMP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMagicRing extends ItemRings {
    @SideOnly(Side.CLIENT)
    private IIcon rl1, rl2, gem;

    public ItemMagicRing(String texture) {
        super(texture);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        // Uses textures from here: imgur.com/ianAIVE,7JoqqPp,TW4IRa2,sI9xD5N,jZGhRQ4,mLgOVCU
        // After modjam. Thanks Drable
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
                    int spellID = tag.getInteger(ModLibs.SPELL_ID);
                    ISpell spell = MagicHandler.getSpellLazy(spellID);
                    if (spell != null) {
                        int trueCost = -spell.cost() + (tag.getInteger(ModLibs.MATERIAL_BOOST) * 5);
                        if (spell.activateSpell(world, player, x, y, z, side, hitX, hitY, hitZ, tag.getInteger(ModLibs.MATERIAL_BOOST), trueCost))
                            return true;
                        else {
                            if (this.isEdible(tag)) {
                                world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                                stack.stackSize--;
                                return true;
                            }
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
            if (tag.hasKey(ModLibs.MATERIAL_BOOST))
                list.add("+" + tag.getInteger(ModLibs.MATERIAL_BOOST) + " " + StatCollector.translateToLocal(ModLibs.BOOST));
        }
    }

    /*
     * @Override public String getItemStackDisplayName(ItemStack stack) { if
     * (stack.getTagCompound() != null) { NBTTagCompound tag =
     * stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG); if (tag != null)
     * { if (tag.hasKey(ModLibs.SPELL_ID)) { int spellID =
     * tag.getInteger(ModLibs.SPELL_ID); ISpell spell =
     * MagicHandler.getSpellLazy(0); if (spell != null) return
     * StatCollector.translateToLocal(spell.getUnlocalizedName()) + " " +
     * super.getItemStackDisplayName(stack); } } } return
     * super.getItemStackDisplayName(stack); }
     */
}

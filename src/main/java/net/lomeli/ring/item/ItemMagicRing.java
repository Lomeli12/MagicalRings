package net.lomeli.ring.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;

import java.awt.Color;
import java.util.List;

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

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IBookEntry;
import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.api.event.SpellCastedEvent;
import net.lomeli.ring.core.SimpleUtil;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

@Interface(iface = "baubles.api.IBauble", modid = "Baubles")
public class ItemMagicRing extends ItemRings implements IBauble, IBookEntry {

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
                            if (entity instanceof EntityPlayer && Rings.proxy.manaHandler.playerHasSession((EntityPlayer) entity)) {
                                IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession((EntityPlayer) entity);
                                SpellCastedEvent spellEvent = new SpellCastedEvent((EntityLivingBase)entity, spell, session);
                                if (MinecraftForge.EVENT_BUS.post(spellEvent))
                                    return;
                                spell.onUpdateTick(stack, world, entity, session, par4, par5, tag.getInteger(ModLibs.MATERIAL_BOOST), trueCost, tag.getBoolean(ModLibs.ACTIVE_EFFECT_ENABLED));
                                if (serverSide() && !((EntityPlayer) entity).capabilities.isCreativeMode)
                                    Rings.proxy.manaHandler.updatePlayerSession(session, world.provider.dimensionId);
                            } else if (entity instanceof EntityLivingBase) {
                                SpellCastedEvent spellEvent = new SpellCastedEvent((EntityLivingBase)entity, spell, null);
                                if (MinecraftForge.EVENT_BUS.post(spellEvent))
                                    return;
                                spell.onUpdateTick(stack, world, entity, null, par4, par5, tag.getInteger(ModLibs.MATERIAL_BOOST), trueCost, tag.getBoolean(ModLibs.ACTIVE_EFFECT_ENABLED));
                            }
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
                    if (serverSide())
                        player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(ModLibs.ACTIVE_EFFECT) + ": " + !active));
                }
            }
        } else {
            NBTTagCompound tag = SimpleUtil.getRingTag(stack);
            if (tag != null && Rings.proxy.manaHandler.playerHasSession(player)) {
                ISpell spell = SimpleUtil.getSpell(tag);
                if (spell != null) {
                    int cost = getSpellCost(tag, spell);
                    int boost = getRingBoost(tag);
                    IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(player);
                    SpellCastedEvent spellEvent = new SpellCastedEvent(player, spell, session);
                    if (MinecraftForge.EVENT_BUS.post(spellEvent))
                        return stack;
                    spell.onUse(world, player, session, stack, boost, cost);
                    if (serverSide() && !player.capabilities.isCreativeMode)
                        Rings.proxy.manaHandler.updatePlayerSession(session, world.provider.dimensionId);
                    playBurp(tag, world, player);
                }
            }
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        NBTTagCompound tag = SimpleUtil.getRingTag(stack);
        if (tag != null && Rings.proxy.manaHandler.playerHasSession(player)) {
            ISpell spell = SimpleUtil.getSpell(tag);
            if (spell != null) {
                int cost = getSpellCost(tag, spell);
                int boost = getRingBoost(tag);
                IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(player);
                SpellCastedEvent spellEvent = new SpellCastedEvent(player, spell, session);
                if (MinecraftForge.EVENT_BUS.post(spellEvent))
                    return false;
                spell.useOnBlock(world, player, session, x, y, z, side, hitX, hitY, hitZ, boost, cost);
                if (serverSide() && !player.capabilities.isCreativeMode)
                    Rings.proxy.manaHandler.updatePlayerSession(session, world.provider.dimensionId);
                playBurp(tag, world, player);
                return true;
            }
        }
        return false;
    }

    public int getSpellCost(NBTTagCompound tag, ISpell spell) {
        if (tag != null && spell != null)
            return -spell.cost() + getRingBoost(tag);
        return 0;
    }

    public int getRingBoost(NBTTagCompound tag) {
        if (tag != null)
            return tag.getInteger(ModLibs.MATERIAL_BOOST) * 5;
        return 0;
    }

    public boolean canPlayerUseMagic(EntityPlayer player) {
        if (!Rings.proxy.manaHandler.playerHasSession(player)) {
            if (!player.worldObj.isRemote)
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(ModLibs.NO_MANA)));
            return false;
        }
        return true;
    }

    public void playBurp(NBTTagCompound tag, World world, EntityPlayer player) {
        if (isEdible(tag))
            world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
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
        } else
            return rl1;
    }

    @Override
    public boolean isFull3D() {
        return true;
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
        } else {
            if (renderPass == 1)
                return this.getLayer2RGB(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG));
            else
                return this.getLayer1RGB(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
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
        if (stack.getTagCompound() != null && player instanceof EntityPlayer) {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
            if (tag != null && Rings.proxy.manaHandler.playerHasSession((EntityPlayer) player)) {
                if (tag.hasKey(ModLibs.SPELL_ID)) {
                    int spellID = tag.getInteger(ModLibs.SPELL_ID);
                    ISpell spell = MagicHandler.getSpellLazy(spellID);
                    if (spell != null) {
                        int trueCost = spell.cost() + (tag.getInteger(ModLibs.MATERIAL_BOOST) * 5);
                        IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession((EntityPlayer) player);
                        SpellCastedEvent spellEvent = new SpellCastedEvent(player, spell, session);
                        if (MinecraftForge.EVENT_BUS.post(spellEvent))
                            return;
                        spell.onUpdateTick(stack, player.worldObj, player, session, 0, true, tag.getInteger(ModLibs.MATERIAL_BOOST), trueCost, tag.getBoolean(ModLibs.ACTIVE_EFFECT_ENABLED));
                        if (serverSide() && !((EntityPlayer)player).capabilities.isCreativeMode)
                            Rings.proxy.manaHandler.updatePlayerSession(session, ((EntityPlayer) player).worldObj.provider.dimensionId);
                    }
                }
            }
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        NBTTagCompound tag = SimpleUtil.getRingTag(itemstack);
        if (tag != null) {
            if (player instanceof EntityPlayer) {
                EntityPlayer pl = (EntityPlayer) player;
                if (canPlayerUseMagic(pl)) {
                    ISpell spell = SimpleUtil.getSpell(tag);
                    if (spell != null) {
                        IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(pl);
                        SpellCastedEvent spellEvent = new SpellCastedEvent(player, spell, session);
                        if (MinecraftForge.EVENT_BUS.post(spellEvent))
                            return;
                        spell.onEquipped(itemstack, player, session);
                        if (serverSide() && !pl.capabilities.isCreativeMode)
                            Rings.proxy.manaHandler.updatePlayerSession(session, pl.getEntityWorld().provider.dimensionId);
                        playBurp(tag, pl.worldObj, pl);
                    }
                }
            }
        }
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        NBTTagCompound tag = SimpleUtil.getRingTag(itemstack);
        if (tag != null) {
            if (player instanceof EntityPlayer && Rings.proxy.manaHandler.playerHasSession((EntityPlayer) player)) {
                EntityPlayer pl = (EntityPlayer) player;
                if (canPlayerUseMagic(pl)) {
                    ISpell spell = SimpleUtil.getSpell(tag);
                    if (spell != null) {
                        IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(pl);
                        SpellCastedEvent spellEvent = new SpellCastedEvent(player, spell, session);
                        if (MinecraftForge.EVENT_BUS.post(spellEvent))
                            return;
                        spell.onUnEquipped(itemstack, player, session);
                        if (serverSide() && !pl.capabilities.isCreativeMode)
                            Rings.proxy.manaHandler.updatePlayerSession(session, pl.getEntityWorld().provider.dimensionId);
                        playBurp(tag, pl.worldObj, pl);
                    }
                }
            }
        }
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return SimpleUtil.getRingTag(itemstack) != null && SimpleUtil.getRingTag(itemstack).hasKey(ModLibs.SPELL_ID);
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    public boolean serverSide() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    @Override
    public String getBookPage(int metadata) {
        return ModLibs.MOD_ID.toLowerCase() + ".ring";
    }

    @Override
    public int getData() {
        return 0;
    }
}

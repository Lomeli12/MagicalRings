package net.lomeli.ring.core.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.core.SimpleUtil;
import net.lomeli.ring.item.ItemMagicRing;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.lomeli.ring.network.PacketClientJoined;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketUpdateClient;

public class EntityHandler {

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        Entity target = event.target;
        EntityPlayer player = event.entityPlayer;
        if (target != null && player != null && MagicHandler.getMagicHandler().getPlayerTag(player) != null) {
            ItemStack stack = player.getCurrentEquippedItem();
            if (stack != null && stack.getItem() instanceof ItemMagicRing) {
                if (stack.getTagCompound() != null) {
                    NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
                    if (tag != null) {
                        int spellID = tag.getInteger(ModLibs.SPELL_ID);
                        ISpell spell = MagicHandler.getSpellLazy(spellID);
                        if (spell != null) {
                            int trueCost = -spell.cost() + (tag.getInteger(ModLibs.MATERIAL_BOOST) * 5);
                            spell.applyToMob(player, target, tag.getInteger(ModLibs.MATERIAL_BOOST), trueCost);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (!event.entityLiving.worldObj.isRemote) {
            EntityLivingBase entity = event.entityLiving;
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (FMLCommonHandler.instance().getSide().isServer())
                    PacketHandler.sendToServer(new PacketClientJoined(player));
                else {
                    NBTTagCompound tag = MagicHandler.getMagicHandler().getPlayerTag(player);
                    if (tag != null) {
                        int mp = tag.getInteger(ModLibs.PLAYER_MP);
                        int max = tag.getInteger(ModLibs.PLAYER_MAX);
                        PacketHandler.sendTo(new PacketUpdateClient(mp, max), player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRingPickUp(EntityItemPickupEvent event) {
        if (Loader.isModLoaded("Baubles")) {
            ItemStack item = event.item.getEntityItem();
            if (item != null && item.getItem() instanceof ItemMagicRing) {
                NBTTagCompound tag = SimpleUtil.getRingTag(item);
                if (tag != null) {
                    ISpell spell = SimpleUtil.getSpell(tag);
                    if (spell != null)
                        spell.onEquipped(item, event.entityPlayer);
                }
            }
        }
    }

    @SubscribeEvent
    public void ringDropped(ItemTossEvent event) {
        if (Loader.isModLoaded("Baubles")) {
            ItemStack item = event.entityItem.getEntityItem();
            if (item != null && item.getItem() instanceof ItemMagicRing) {
                NBTTagCompound tag = SimpleUtil.getRingTag(item);
                if (tag != null) {
                    ISpell spell = SimpleUtil.getSpell(tag);
                    if (spell != null)
                        spell.onUnEquipped(item, event.player);
                }
            }
        }
    }

    /*
    @SubscribeEvent
    public void entityTick(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != null && event.entity instanceof EntityPlayerMP && !event.entity.worldObj.isRemote) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            if (MagicHandler.getMagicHandler().canPlayerUseMagic(player)) {
                if (MagicHandler.getMagicHandler().getPlayerTag(player).hasKey(ModLibs.PLAYER_FLY)) {
                    //System.out.println(MagicHandler.getMagicHandler().getPlayerTag(player).getBoolean(ModLibs.PLAYER_FLY));
                    if (MagicHandler.getMagicHandler().getPlayerTag(player).getBoolean(ModLibs.PLAYER_FLY)) {
                        //System.out.println("s");
                        boolean continueFly = false, activated  = false;
                        if (Loader.isModLoaded(ModLibs.BAUBLES)) {
                            IInventory baubleInv = BaublesApi.getBaubles(player);
                            for (int i = 1; i < 3; i++) {
                                ItemStack stack = baubleInv.getStackInSlot(i);
                                //System.out.println("e");
                                if (stack != null) {
                                    if (isStackFlyRing(stack)) {
                                        continueFly = true;
                                        activated = isRingEnabled(stack);
                                        //System.out.println("YEs");
                                        break;
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                                ItemStack stack = player.inventory.getStackInSlot(i);
                                if (stack != null) {
                                    if (isStackFlyRing(stack)) {
                                        continueFly = true;
                                        activated = isRingEnabled(stack);
                                        break;
                                    }
                                }
                            }
                        }

                        if (continueFly) {
                            if (MagicHandler.canUse(player, 1) && activated) {
                                if (!player.capabilities.isCreativeMode && !player.capabilities.allowFlying )
                                    player.capabilities.allowFlying = false;
                                if (player.capabilities.isFlying && !player.capabilities.isCreativeMode)
                                    MagicHandler.modifyPlayerMP(player, -1);
                            } else {
                                if (!player.capabilities.isCreativeMode)
                                    player.capabilities.allowFlying = false;
                                if (player.capabilities.isFlying)
                                    player.capabilities.isFlying = false;

                                PacketHandler.sendToPlayerAndServer(new PacketAllowFlying(player, false), player);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isStackFlyRing(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemMagicRing) {
            if (stack.getTagCompound() != null) {
                NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
                if (tag != null) {
                    if (tag.hasKey(ModLibs.SPELL_ID)) {
                        int spellID = tag.getInteger(ModLibs.SPELL_ID);
                        ISpell spell = MagicHandler.getSpellLazy(spellID);
                        if (spell != null && spell instanceof SwiftWind)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isRingEnabled(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemMagicRing) {
            if (stack.getTagCompound() != null) {
                NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
                if (tag != null) {
                    if (tag.hasKey(ModLibs.SPELL_ID)) {
                        int spellID = tag.getInteger(ModLibs.SPELL_ID);
                        ISpell spell = MagicHandler.getSpellLazy(spellID);
                        if (spell != null && spell instanceof SwiftWind)
                            return tag.getBoolean(ModLibs.ACTIVE_EFFECT_ENABLED);
                    }
                }
            }
        }
        return false;
    }*/

}

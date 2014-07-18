package net.lomeli.ring.core.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.event.SpellCastedEvent;
import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.entity.EntityFireProofItem;
import net.lomeli.ring.entity.EntityFireStone;
import net.lomeli.ring.item.ItemMagicRing;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;

public class EntityHandler {

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        Entity target = event.target;
        EntityPlayer player = event.entityPlayer;
        if (target != null && player != null && Rings.proxy.manaHandler.playerHasSession(player)) {
            ItemStack stack = player.getCurrentEquippedItem();
            if (stack != null && stack.getItem() instanceof ItemMagicRing) {
                if (stack.hasTagCompound()) {
                    NBTTagCompound ringTag = SimpleUtil.getRingTag(stack);
                    String spellID = SimpleUtil.getSpellIdFromTag(ringTag);
                    if (spellID != null && ringTag != null) {
                        ISpell spell = Rings.proxy.spellRegistry.getSpell(spellID);
                        if (spell != null) {
                            int trueCost = -spell.cost() + (ringTag.getInteger(ModLibs.MATERIAL_BOOST) * 5);
                            IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(player);
                            SpellCastedEvent spellEvent = new SpellCastedEvent(player, spell, session);
                            if (MinecraftForge.EVENT_BUS.post(spellEvent))
                                return;
                            spell.applyToMob(player, session, target, ringTag.getInteger(ModLibs.MATERIAL_BOOST), trueCost);
                            if (FMLCommonHandler.instance().getEffectiveSide().isServer() && !player.capabilities.isCreativeMode)
                                Rings.proxy.manaHandler.updatePlayerSession(session, player.getEntityWorld().provider.dimensionId);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (!event.entityLiving.worldObj.isRemote) {
            EntityLivingBase entity = event.entityLiving;
            if (entity instanceof EntityBat) {
                if (((EntityBat) entity).worldObj.rand.nextInt(100) < 80) {
                    EntityItem batWing = new EntityItem(((EntityBat) entity).worldObj, ((EntityBat) entity).posX, ((EntityBat) entity).posY, ((EntityBat) entity).posZ, new ItemStack(ModItems.materials));
                    ((EntityBat) entity).worldObj.spawnEntityInWorld(batWing);
                }
            } else if (entity instanceof EntitySquid) {
                if (((EntitySquid) entity).worldObj.rand.nextInt(100) < 60) {
                    EntityItem tentacle = new EntityItem(((EntitySquid) entity).worldObj, ((EntitySquid) entity).posX, ((EntitySquid) entity).posY, ((EntitySquid) entity).posZ, new ItemStack(ModItems.materials, 1, 4));
                    ((EntitySquid) entity).worldObj.spawnEntityInWorld(tentacle);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRingPickUp(EntityItemPickupEvent event) {
        if (!Loader.isModLoaded("Baubles")) {
            ItemStack item = event.item.getEntityItem();
            if (item != null && item.getItem() instanceof ItemMagicRing) {
                NBTTagCompound tag = SimpleUtil.getRingTag(item);
                if (tag != null) {
                    ISpell spell = SimpleUtil.getSpell(tag);
                    if (spell != null) {
                        IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(event.entityPlayer);
                        SpellCastedEvent spellEvent = new SpellCastedEvent(event.entityPlayer, spell, session);
                        if (MinecraftForge.EVENT_BUS.post(spellEvent))
                            return;
                        spell.onEquipped(item, event.entityPlayer, session);
                        if (FMLCommonHandler.instance().getEffectiveSide().isServer() && !event.entityPlayer.capabilities.isCreativeMode)
                            Rings.proxy.manaHandler.updatePlayerSession(session, event.entityPlayer.getEntityWorld().provider.dimensionId);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void ringDropped(ItemTossEvent event) {
        ItemStack item = event.entityItem.getEntityItem();
        if (item != null && item.getItem() != null) {
            if (item.getItem() == ModItems.materials) {
                if (item.getItemDamage() == 1) {
                    EntityFireStone fireStone = new EntityFireStone(event.entityItem.worldObj, event.entityItem.posX, event.entityItem.posY, event.entityItem.posZ, item);
                    fireStone.motionX = event.entityItem.motionX;
                    fireStone.motionY = event.entityItem.motionY;
                    fireStone.motionZ = event.entityItem.motionZ;
                    fireStone.delayBeforeCanPickup = event.entityItem.delayBeforeCanPickup;
                    if (!event.entityItem.worldObj.isRemote) {
                        event.entityItem.worldObj.spawnEntityInWorld(fireStone);
                        event.entityItem.setDead();
                    }
                } else if (item.getItemDamage() == 2) {
                    EntityFireProofItem fireProofItem = new EntityFireProofItem(event.entityItem.worldObj, event.entityItem.posX, event.entityItem.posY, event.entityItem.posZ, item);
                    fireProofItem.motionX = event.entityItem.motionX;
                    fireProofItem.motionY = event.entityItem.motionY;
                    fireProofItem.motionZ = event.entityItem.motionZ;
                    fireProofItem.delayBeforeCanPickup = event.entityItem.delayBeforeCanPickup;
                    if (!event.entityItem.worldObj.isRemote) {
                        event.entityItem.worldObj.spawnEntityInWorld(fireProofItem);
                        event.entityItem.setDead();
                    }
                }
            }

            if (!Loader.isModLoaded("Baubles") && item.getItem() instanceof ItemMagicRing) {
                NBTTagCompound tag = SimpleUtil.getRingTag(item);
                if (tag != null) {
                    ISpell spell = SimpleUtil.getSpell(tag);
                    if (spell != null) {
                        IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(event.player);
                        SpellCastedEvent spellEvent = new SpellCastedEvent(event.player, spell, session);
                        if (MinecraftForge.EVENT_BUS.post(spellEvent))
                            return;
                        spell.onUnEquipped(item, event.player, Rings.proxy.manaHandler.getPlayerSession(event.player));
                        if (FMLCommonHandler.instance().getEffectiveSide().isServer() && !event.player.capabilities.isCreativeMode)
                            Rings.proxy.manaHandler.updatePlayerSession(session, event.player.getEntityWorld().provider.dimensionId);
                    }
                }
            }
        }
    }
}

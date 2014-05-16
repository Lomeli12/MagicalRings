package net.lomeli.ring.core.handler;

import net.lomeli.ring.item.ItemMagicRing;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.ISpell;
import net.lomeli.ring.magic.MagicHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntityHandler {

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        Entity target = event.target;
        EntityPlayer player = event.entityPlayer;
        if (target != null && player != null) {
            ItemStack stack = player.getCurrentEquippedItem();
            if (stack != null && stack.getItem() instanceof ItemMagicRing) {
                if (stack.getTagCompound() != null) {
                    NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
                    if (tag != null) {
                        int spellID = tag.getInteger(ModLibs.SPELL_ID);
                        ISpell spell = MagicHandler.getSpellLazy(0);// MagicHandler.getSpellLazy(spellID);
                        if (spell != null) {
                            if (MagicHandler.canUse(player, spell.cost())) {
                                MagicHandler.modifyPlayerMaxMP(player, -spell.cost());
                                spell.applyToMob(player, target);
                            }
                        }
                    }
                }
            }
        }
    }

}
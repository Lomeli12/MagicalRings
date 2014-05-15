package net.lomeli.modjam4.core.handler;

import net.lomeli.modjam4.item.ItemMagicRing;
import net.lomeli.modjam4.lib.ModLibs;
import net.lomeli.modjam4.magic.ISpell;
import net.lomeli.modjam4.magic.MagicHandler;
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
                if (stack.getTagCompound() == null)
                    stack.stackTagCompound = new NBTTagCompound();
                else {
                    NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
                    if (tag != null) {
                        int spellID = tag.getInteger(ModLibs.SPELL_ID);
                        ISpell spell = MagicHandler.getSpellLazy(0);//MagicHandler.getSpellLazy(spellID);
                        if (spell != null)
                            spell.applyToMob(player, target);
                    }
                }
            }
        }
    }

}

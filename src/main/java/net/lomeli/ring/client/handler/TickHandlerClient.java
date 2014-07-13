package net.lomeli.ring.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.item.ItemMagicRing;
import net.lomeli.ring.lib.ModLibs;

public class TickHandlerClient {
    @SubscribeEvent
    public void renderTick(RenderTickEvent tick) {
        if (tick.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.inGameHasFocus) {
                ItemStack hand = mc.thePlayer.getCurrentEquippedItem();
                if (hand != null) {
                    if (hand.getItem() instanceof ItemMagicRing) {
                        if (hand.getTagCompound() != null) {
                            NBTTagCompound handtag = hand.getTagCompound().hasKey(ModLibs.RING_TAG) ? hand.getTagCompound().getCompoundTag(ModLibs.RING_TAG) : null;
                            if (handtag != null && handtag.hasKey(ModLibs.SPELL_ID) && Rings.proxy.manaHandler.playerHasSession(mc.thePlayer)) {
                                IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(mc.thePlayer);
                                String mpInfo = StatCollector.translateToLocal(ModLibs.MANA) + ": " + session.getMana() + " / " + session.getMaxMana();
                                mc.fontRenderer.drawString(mpInfo, ModLibs.DISPLAY_X + 1, ModLibs.DISPLAY_Y + 1, 0x000000);
                                mc.fontRenderer.drawString(mpInfo, ModLibs.DISPLAY_X, ModLibs.DISPLAY_Y, 0x00C4C4);
                            }
                            /*
                            if (handtag != null && handtag.hasKey(ModLibs.SPELL_ID) && MagicHandler.getMagicHandler().getPlayerTag(mc.thePlayer) != null) {
                                int mp = MagicHandler.getMagicHandler().getPlayerMP(mc.thePlayer);
                                int max = MagicHandler.getMagicHandler().getPlayerMaxMP(mc.thePlayer);
                                String mpInfo = StatCollector.translateToLocal(ModLibs.MANA) + ": " + mp + " / " + max;
                                mc.fontRenderer.drawString(mpInfo, ModLibs.DISPLAY_X + 1, ModLibs.DISPLAY_Y + 1, 0x000000);
                                mc.fontRenderer.drawString(mpInfo, ModLibs.DISPLAY_X, ModLibs.DISPLAY_Y, 0x00C4C4);
                            }*/
                        }
                    }
                }
            }
        }
    }
}

package net.lomeli.ring.client.handler;

import net.lomeli.ring.item.ItemMagicRing;
import net.lomeli.ring.lib.ModLibs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

public class TickHandlerClient {
    @SubscribeEvent
    public void clientTick(ClientTickEvent tick) {

    }

    @SubscribeEvent
    public void renderTick(RenderTickEvent tick) {
        if (tick.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.inGameHasFocus) {
                if (mc.thePlayer.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
                    NBTTagCompound tag = mc.thePlayer.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA);
                    ItemStack hand = mc.thePlayer.getCurrentEquippedItem();
                    GuiIngame gui = mc.ingameGUI;
                    if (hand != null) {
                        if (hand.getItem() instanceof ItemMagicRing) {
                            int mp = tag.getInteger(ModLibs.PLAYER_MP);
                            int max = tag.getInteger(ModLibs.PLAYER_MAX);
                            String mpInfo = "MP: " + mp + " / " + max;
                            mc.fontRenderer.drawString(mpInfo, ModLibs.DISPLAY_X + 1, ModLibs.DISPLAY_Y + 1, 0x000000);
                            mc.fontRenderer.drawString(mpInfo, ModLibs.DISPLAY_X, ModLibs.DISPLAY_Y, 0x00C4C4);
                        }
                    }
                }
            }
        }
    }
}

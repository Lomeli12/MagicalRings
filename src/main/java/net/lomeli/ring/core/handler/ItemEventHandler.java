package net.lomeli.ring.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IPlayerSession;

public class ItemEventHandler {
    @SubscribeEvent
    public void finishItemUse(PlayerUseItemEvent.Finish event) {
        if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
            EntityPlayer player = event.entityPlayer;
            ItemStack stack = event.item;
            if (Rings.proxy.manaHandler.playerHasSession(player) && !player.capabilities.isCreativeMode) {
                if (stack.getItem() instanceof ItemFood) {
                    int foodPoints = ((ItemFood) stack.getItem()).func_150905_g(stack) * 2;
                    IPlayerSession playerSession = Rings.proxy.manaHandler.getPlayerSession(player);
                    playerSession.adjustMana(foodPoints, false);
                    Rings.proxy.manaHandler.updatePlayerSession(playerSession, player.getEntityWorld().provider.dimensionId);
                }
            }
            /*
            if (MagicHandler.getMagicHandler().getPlayerTag(player) != null && !player.capabilities.isCreativeMode) {
                if (stack.getItem() instanceof ItemFood) {
                    int foodPoints = ((ItemFood) stack.getItem()).func_150905_g(stack) * 2;
                    Rings.pktHandler.sendToServer(new PacketModifyMp(player, foodPoints, 0));
                }
            }*/
        }
    }
}

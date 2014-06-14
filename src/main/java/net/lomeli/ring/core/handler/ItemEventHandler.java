package net.lomeli.ring.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import net.lomeli.ring.core.SimpleUtil;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketModifyMp;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;

public class ItemEventHandler {
    @SubscribeEvent
    public void finishItemUse(PlayerUseItemEvent.Finish event) {
        if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
            EntityPlayer player = event.entityPlayer;
            ItemStack stack = event.item;
            if (MagicHandler.getMagicHandler().getPlayerTag(player) != null && !player.capabilities.isCreativeMode) {
                if (stack.getItem() instanceof ItemFood) {
                    int foodPoints = ((ItemFood) stack.getItem()).func_150905_g(stack) * 2;
                    PacketHandler.sendToServer(new PacketModifyMp(player, foodPoints, 0));
                }
            }
        }
    }
}

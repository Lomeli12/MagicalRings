package net.lomeli.ring.core.handler;

import java.util.ArrayList;
import java.util.List;

import net.lomeli.ring.magic.MagicHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class TickHandlerCore {
    public List<Integer> flyingPlayerList = new ArrayList<Integer>();

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            MinecraftServer ms = MinecraftServer.getServer();
            
            for (int i = 0; i < flyingPlayerList.size(); i++) {
                int entityId = flyingPlayerList.get(i);
                
                EntityPlayer player = (EntityPlayer) ms.getEntityWorld().getEntityByID(entityId);
                if (player.capabilities.isCreativeMode) {
                    flyingPlayerList.remove(i);
                    return;
                }
                
                if (MagicHandler.canUse(player, 1)) {
                    if (!player.capabilities.allowFlying)
                        player.capabilities.allowFlying = true;

                    if (player.capabilities.isFlying)
                        MagicHandler.modifyPlayerMP(player, -1);
                }else if (player.capabilities.allowFlying) {
                    player.capabilities.allowFlying = false;
                    flyingPlayerList.remove(i);
                }
            }
        }
    }
}

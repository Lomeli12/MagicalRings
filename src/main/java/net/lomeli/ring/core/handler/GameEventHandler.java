package net.lomeli.ring.core.handler;

import net.minecraft.entity.player.EntityPlayer;

import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketClientJoined;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketModifyMp;
import net.lomeli.ring.network.PacketRemovePlayer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class GameEventHandler {
    private int tick;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent tick) {
        if (tick.side == Side.SERVER) {
            EntityPlayer player = tick.player;
            if (tick.phase == TickEvent.Phase.END) {
                if (player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
                    if (++this.tick >= ModLibs.RECHARGE_WAIT_TIME) {
                        if (player.getFoodStats().getFoodLevel() > 4) {
                            PacketHandler.sendToServer(new PacketModifyMp(player, ((player.getFoodStats().getFoodLevel() - 3) / 5), 0));
                            this.tick = 0;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player != null)
            PacketHandler.sendToServer(new PacketClientJoined(event.player));
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player != null)
            PacketHandler.sendToServer(new PacketRemovePlayer(event.player));
    }
}

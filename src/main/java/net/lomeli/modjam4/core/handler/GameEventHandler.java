package net.lomeli.modjam4.core.handler;

import net.lomeli.modjam4.network.PacketClear;
import net.lomeli.modjam4.network.PacketConnect;
import net.lomeli.modjam4.network.PacketHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class GameEventHandler {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player != null) {
            PacketConnect packet = new PacketConnect(event.player);
            //if (packet != null)
            //    PacketHandler.sendToServer(packet);
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PacketHandler.sendTo(new PacketClear(), event.player);
    }
}

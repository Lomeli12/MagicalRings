package net.lomeli.ring.core.handler;

import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketUpdatePlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class GameEventHandler {
    private int tick;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent tick) {
        EntityPlayer player = tick.player;
        if (tick.phase == TickEvent.Phase.END) {
            if (player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
                NBTTagCompound tag = player.getEntityData().getCompoundTag(ModLibs.PLAYER_DATA);
                int mp = tag.getInteger(ModLibs.PLAYER_MP), max = tag.getInteger(ModLibs.PLAYER_MAX);
                if (mp < max) {
                    if (++this.tick >= ModLibs.RECHARGE_WAIT_TIME) {
                        if (player.getFoodStats().getFoodLevel() > 6) {
                            mp += ((player.getFoodStats().getFoodLevel() - 3) / 5);
                            if (mp > max)
                                mp = max;
                            PacketHandler.sendToPlayerAndServer(new PacketUpdatePlayerMP(player, mp, max), player);
                        }
                        this.tick = 0;
                    }
                    
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {

    }
}

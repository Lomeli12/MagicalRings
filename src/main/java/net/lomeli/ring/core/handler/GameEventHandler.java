package net.lomeli.ring.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.lomeli.ring.network.PacketClientJoined;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketModifyMp;
import net.lomeli.ring.network.PacketUpdateClient;

public class GameEventHandler {
    private int tick;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent tick) {
        if (tick.side == Side.SERVER) {
            EntityPlayer player = tick.player;
            if (tick.phase == TickEvent.Phase.END) {
                if (MagicHandler.getMagicHandler().getPlayerTag(player) != null) {
                    if (++this.tick >= ModLibs.RECHARGE_WAIT_TIME) {
                        if (player.getFoodStats().getFoodLevel() > 4) {
                            if (MagicHandler.getMagicHandler().getPlayerMP(player) < MagicHandler.getMagicHandler().getPlayerMaxMP(player)) {
                                PacketHandler.sendToServer(new PacketModifyMp(player, ((player.getFoodStats().getFoodLevel() - 3) / 5), 0));
                                this.tick = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player != null) {
            if (FMLCommonHandler.instance().getSide().isServer())
                PacketHandler.sendToServer(new PacketClientJoined(event.player));
            else {
                EntityPlayer player = event.player;
                NBTTagCompound tag = MagicHandler.getMagicHandler().getPlayerTag(player);
                if (tag != null) {
                    int mp = tag.getInteger(ModLibs.PLAYER_MP);
                    int max = tag.getInteger(ModLibs.PLAYER_MAX);
                    PacketHandler.sendTo(new PacketUpdateClient(mp, max), player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
    }
}

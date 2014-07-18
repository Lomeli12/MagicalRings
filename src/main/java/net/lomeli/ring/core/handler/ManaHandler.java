package net.lomeli.ring.core.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.event.SessionUpdatedEvent;
import net.lomeli.ring.api.interfaces.IManaHandler;
import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.core.helper.LogHelper;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.PlayerSession;
import net.lomeli.ring.magic.RingSaveData;

public class ManaHandler implements IManaHandler {
    private HashMap<Integer, HashMap<String, IPlayerSession>> dimensionSession;
    private RingSaveData saveData;

    public ManaHandler() {
        dimensionSession = new HashMap<Integer, HashMap<String, IPlayerSession>>();
    }

    public IPlayerSession getPlayerData(EntityPlayer player) {
        if (!saveData.getSavaData().isEmpty()) {
            if (saveData.getSavaData().containsKey(player.getGameProfile().getId().toString()))
                return saveData.getSavaData().get(player.getGameProfile().getId().toString());
        }
        return null;
    }

    public void beginNewSession(int dimensionID) {
        saveWorldData();
        HashMap<String, IPlayerSession> sessionList = null;
        if (dimensionSession.containsKey(dimensionID))
            sessionList = dimensionSession.get(dimensionID);
        if (sessionList != null) {
            sessionList.clear();
            dimensionSession.put(dimensionID, sessionList);
            for (int i = 0; i < MinecraftServer.getServer().getEntityWorld().playerEntities.size(); i++) {
                EntityPlayer player = (EntityPlayer) MinecraftServer.getServer().getEntityWorld().playerEntities.get(i);
                loadPlayerData(player);
            }
        }
    }

    @Override
    public void loadPlayerData(EntityPlayer player) {
        if (player != null) {
            IPlayerSession session = getPlayerData(player);
            if (session != null) {
                int id = player.getEntityWorld().provider.dimensionId;
                HashMap<String, IPlayerSession> sessionList = new HashMap<String, IPlayerSession>();
                if (dimensionSession.containsKey(id))
                    sessionList = dimensionSession.get(id);
                sessionList.put(player.getGameProfile().getId().toString(), session);
                dimensionSession.put(id, sessionList);
            }
        }
    }

    @Override
    public void unloadPlayerSession(EntityPlayer player) {
        if (player != null) {
            int id = player.getEntityWorld().provider.dimensionId;
            HashMap<String, IPlayerSession> sessionList = null;
            if (dimensionSession.containsKey(id))
                sessionList = dimensionSession.get(id);
            if (sessionList != null) {
                if (sessionList.containsKey(player.getGameProfile().getId().toString())) {
                    LogHelper.info("Saving session data for " + player.getDisplayName());
                    saveWorldData();
                    sessionList.remove(player.getGameProfile().getId().toString());
                    LogHelper.info("Removed " + player.getDisplayName() + " from session!");
                }
                dimensionSession.put(id, sessionList);
            }
        }
    }

    public void saveWorldData() {
        if (saveData != null) {
            HashMap<String, IPlayerSession> sessionList = new HashMap<String, IPlayerSession>();
            for (Map.Entry<Integer, HashMap<String, IPlayerSession>> entry : dimensionSession.entrySet()) {
                if (!entry.getValue().isEmpty())
                    sessionList.putAll(entry.getValue());
            }
            if (!sessionList.isEmpty())
                saveData.setSaveData(sessionList);
        }
    }

    @Override
    public boolean playerHasSession(EntityPlayer player) {
        if (player != null) {
            int id = player.getEntityWorld().provider.dimensionId;
            HashMap<String, IPlayerSession> sessionList = null;
            if (dimensionSession.containsKey(id))
                sessionList = dimensionSession.get(id);
            if (sessionList != null)
                return sessionList.containsKey(player.getGameProfile().getId().toString());
        }
        return false;
    }

    @Override
    public void addPlayerSession(EntityPlayer player, int mana, int max) {
        if (player != null)
            addPlayerSession(player.getGameProfile().getId().toString(), new PlayerSession(player, mana, max), player.getEntityWorld().provider.dimensionId);
    }

    @Override
    public void addPlayerSession(String uuid, IPlayerSession playerSession, int id) {
        if (playerSession != null) {
            HashMap<String, IPlayerSession> sessionList = new HashMap<String, IPlayerSession>();
            if (dimensionSession.containsKey(id))
                sessionList = dimensionSession.get(id);
            if (sessionList != null) {
                if (sessionList.containsKey(uuid))
                    return;
                sessionList.put(uuid, playerSession);
                dimensionSession.put(id, sessionList);
            }
        }
    }

    @Override
    public IPlayerSession getPlayerSession(EntityPlayer player) {
        if (player != null) {
            int id = player.getEntityWorld().provider.dimensionId;
            HashMap<String, IPlayerSession> sessionList = null;
            if (dimensionSession.containsKey(id))
                sessionList = dimensionSession.get(id);
            if (sessionList != null) {
                if (sessionList.containsKey(player.getGameProfile().getId().toString()))
                    return sessionList.get(player.getGameProfile().getId().toString());
            }
        }
        return null;
    }

    @Override
    public void updatePlayerSession(IPlayerSession playerSession, int id) {
        if (playerSession != null) {
            HashMap<String, IPlayerSession> sessionList = null;
            if (dimensionSession.containsKey(id))
                sessionList = dimensionSession.get(id);
            if (sessionList != null && !sessionList.isEmpty()) {
                SessionUpdatedEvent event = new SessionUpdatedEvent(playerSession, id, sessionList);
                if (MinecraftForge.EVENT_BUS.post(event))
                    return;
                if (sessionList.containsKey(playerSession.getPlayerID())) {
                    sessionList.put(playerSession.getPlayerID(), playerSession);
                    return;
                }
                dimensionSession.put(id, sessionList);
            }
        }
    }

    public void unloadAllData() {
        LogHelper.info("Saving server data to world...");
        saveWorldData();
        LogHelper.info("Clearing data...");
        saveData = null;
        dimensionSession.clear();
    }

    public void playerChangedDimension(EntityPlayer player, int oldDim, int newDim) {
        if (player != null) {
            IPlayerSession playerSession = null;
            HashMap<String, IPlayerSession> sessionList = null;
            if (dimensionSession.containsKey(oldDim))
                sessionList = dimensionSession.get(oldDim);
            if (sessionList != null) {
                String id = player.getGameProfile().getId().toString();
                if (sessionList.containsKey(id))
                    playerSession = sessionList.get(id);
                if (playerSession != null) {
                    sessionList.remove(id);
                    dimensionSession.put(oldDim, sessionList);
                    sessionList = new HashMap<String, IPlayerSession>();
                    if (dimensionSession.containsKey(newDim))
                        sessionList = dimensionSession.get(newDim);
                    sessionList.put(id, playerSession);
                    dimensionSession.put(newDim, sessionList);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            LogHelper.info("Saving current session for dimension " + event.world.provider.dimensionId + "!");
            Rings.proxy.manaHandler.saveWorldData();
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer() && event.world.provider.dimensionId == 0) {
            LogHelper.info("Saving all world data!");
            Rings.proxy.manaHandler.saveWorldData();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            if (event.world.provider.dimensionId == 0) {
                WorldServer world = (WorldServer) event.world;
                RingSaveData ringSaveData = (RingSaveData) world.perWorldStorage.loadData(RingSaveData.class, ModLibs.SAVE_DATA);
                if (ringSaveData == null) {
                    ringSaveData = new RingSaveData(ModLibs.SAVE_DATA);
                    world.perWorldStorage.setData(ModLibs.SAVE_DATA, ringSaveData);
                }
                Rings.proxy.manaHandler.saveData = ringSaveData;
            }
            LogHelper.info("Beginning new session " + event.world.provider.dimensionId + "!");
            Rings.proxy.manaHandler.beginNewSession(event.world.provider.dimensionId);
        }
    }
}

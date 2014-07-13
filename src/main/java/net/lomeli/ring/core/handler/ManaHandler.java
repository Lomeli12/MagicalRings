package net.lomeli.ring.core.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.PlayerSession;
import net.lomeli.ring.magic.RingSaveData;

public class ManaHandler implements IManaHandler {
    private HashMap<Integer, List<IPlayerSession>> dimensionSession;
    private RingSaveData saveData;

    public ManaHandler() {
        dimensionSession = new HashMap<Integer, List<IPlayerSession>>();
    }

    public IPlayerSession getPlayerData(EntityPlayer player) {
        if (!saveData.getSavaData().isEmpty()) {
            for (IPlayerSession session : saveData.getSavaData()) {
                if (session != null && session.getPlayerID().equalsIgnoreCase(player.getUniqueID().toString()))
                    return session;
            }
        }
        return null;
    }

    public void beginNewSession(int dimensionID) {
        saveWorldData();
        List<IPlayerSession> sessionList = null;
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
                List<IPlayerSession> sessionList = new ArrayList<IPlayerSession>();
                if (dimensionSession.containsKey(id))
                    sessionList = dimensionSession.get(id);
                sessionList.add(session);
                dimensionSession.put(id, sessionList);
            }
        }
    }

    @Override
    public void unloadPlayerSession(EntityPlayer player) {
        if (player != null) {
            int id = player.getEntityWorld().provider.dimensionId;
            List<IPlayerSession> sessionList = null;
            if (dimensionSession.containsKey(id))
                sessionList = dimensionSession.get(id);
            if (sessionList != null) {
                for (int i = 0; i < sessionList.size(); i++) {
                    IPlayerSession session = sessionList.get(i);
                    if (session != null && session.getPlayerID().equalsIgnoreCase(player.getUniqueID().toString())) {
                        Rings.log.log(Level.INFO, "Saving session data for " + player.getDisplayName());
                        saveWorldData();
                        sessionList.remove(i);
                        Rings.log.log(Level.INFO, "Removed " + player.getDisplayName() + " from session!");
                        return;
                    }
                }
                dimensionSession.put(id, sessionList);
            }
        }
    }

    public void saveWorldData() {
        if (saveData != null) {
            List<IPlayerSession> sessionList = new ArrayList<IPlayerSession>();
            for (Map.Entry<Integer, List<IPlayerSession>> entry : dimensionSession.entrySet()) {
                if (!entry.getValue().isEmpty())
                    sessionList.addAll(entry.getValue());
            }
            if (!sessionList.isEmpty())
                saveData.setSaveData(sessionList);
        }
    }

    @Override
    public boolean playerHasSession(EntityPlayer player) {
        if (player != null) {
            int id = player.getEntityWorld().provider.dimensionId;
            List<IPlayerSession> sessionList = null;
            if (dimensionSession.containsKey(id))
                sessionList = dimensionSession.get(id);
            if (sessionList != null) {
                for (int i = 0; i < sessionList.size(); i++) {
                    IPlayerSession session = sessionList.get(i);
                    if (session != null && session.getPlayerID().equalsIgnoreCase(player.getUniqueID().toString()))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void addPlayerSession(EntityPlayer player, int mana, int max) {
        if (player != null)
            addPlayerSession(new PlayerSession(player, mana, max), player.getEntityWorld().provider.dimensionId);
    }

    @Override
    public void addPlayerSession(IPlayerSession playerSession, int id) {
        if (playerSession != null) {
            List<IPlayerSession> sessionList = new ArrayList<IPlayerSession>();
            if (dimensionSession.containsKey(id))
                sessionList = dimensionSession.get(id);
            if (sessionList != null) {
                for (IPlayerSession session : sessionList) {
                    if (session != null && session.getPlayerID().equalsIgnoreCase(playerSession.getPlayerID()))
                        return;
                }
                sessionList.add(playerSession);
                dimensionSession.put(id, sessionList);
            }
        }
    }

    @Override
    public IPlayerSession getPlayerSession(EntityPlayer player) {
        if (player != null) {
            int id = player.getEntityWorld().provider.dimensionId;
            List<IPlayerSession> sessionList = null;
            if (dimensionSession.containsKey(id))
                sessionList = dimensionSession.get(id);
            if (sessionList != null) {
                for (IPlayerSession session : sessionList) {
                    if (session != null && session.getPlayerID().equalsIgnoreCase(player.getUniqueID().toString()))
                        return session;
                }
            }
        }
        return null;
    }

    @Override
    public void updatePlayerSession(IPlayerSession playerSession, int id) {
        if (playerSession != null) {
            List<IPlayerSession> sessionList = null;
            if (dimensionSession.containsKey(id))
                sessionList = dimensionSession.get(id);
            if (sessionList != null && !sessionList.isEmpty()) {
                SessionUpdatedEvent event = new SessionUpdatedEvent(playerSession, id, sessionList);
                if (MinecraftForge.EVENT_BUS.post(event))
                    return;
                for (int i = 0; i < sessionList.size(); i++) {
                    IPlayerSession session = sessionList.get(i);
                    if (session != null && session.getPlayerID().equalsIgnoreCase(playerSession.getPlayerID())) {
                        sessionList.remove(i);
                        sessionList.add(playerSession);
                        return;
                    }
                }
            }
        }
    }

    public void playerChangedDimension(EntityPlayer player, int oldDim, int newDim) {
        if (player != null) {
            IPlayerSession playerSession = null;
            List<IPlayerSession> sessionList = null;
            if (dimensionSession.containsKey(oldDim))
                sessionList = dimensionSession.get(oldDim);
            if (sessionList != null) {
                int index = -1;
                for (int i = 0; i < sessionList.size(); i++) {
                    IPlayerSession session = sessionList.get(i);
                    if (session != null && session.getPlayerID().equalsIgnoreCase(player.getUniqueID().toString())) {
                        playerSession = session;
                        break;
                    }
                }
                if (playerSession != null) {
                    sessionList.remove(index);
                    dimensionSession.put(oldDim, sessionList);
                    sessionList = new ArrayList<IPlayerSession>();
                    if (dimensionSession.containsKey(newDim))
                        sessionList = dimensionSession.get(newDim);
                    sessionList.add(playerSession);
                    dimensionSession.put(newDim, sessionList);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            Rings.log.log(Level.INFO, "Saving current session for dimension " + event.world.provider.dimensionId + "!");
            Rings.proxy.manaHandler.saveWorldData();
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer() && event.world.provider.dimensionId == 0) {
            Rings.log.log(Level.INFO, "Saving all world data!");
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
            Rings.log.log(Level.INFO, "Beginning new session " + event.world.provider.dimensionId + "!");
            Rings.proxy.manaHandler.beginNewSession(event.world.provider.dimensionId);
        }
    }
}

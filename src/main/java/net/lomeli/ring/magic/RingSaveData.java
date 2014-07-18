package net.lomeli.ring.magic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.lib.ModLibs;

public class RingSaveData extends WorldSavedData {
    private HashMap<String, IPlayerSession> sessions;

    public RingSaveData(String map) {
        super(map);
        sessions = new HashMap<String, IPlayerSession>();
    }

    public HashMap<String, IPlayerSession> getSavaData() {
        return sessions;
    }

    public void setSaveData(HashMap<String, IPlayerSession> list) {
        sessions = list;
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        Iterator iterator = tagCompound.func_150296_c().iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            if (s.startsWith(ModLibs.MOD_ID.toLowerCase() + "_")) {
                NBTTagCompound tag = tagCompound.getCompoundTag(s);
                String id = s.split("_")[1];
                int mana = tag.getInteger(ModLibs.PLAYER_MP);
                int max = tag.getInteger(ModLibs.PLAYER_MAX);
                sessions.put(id, new PlayerSession(id, mana, max));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        if (sessions != null) {
            for (Map.Entry<String, IPlayerSession> session : sessions.entrySet()) {
                IPlayerSession pSession = session.getValue();
                if (pSession != null) {
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setInteger(ModLibs.PLAYER_MP, pSession.getMana());
                    tag.setInteger(ModLibs.PLAYER_MAX, pSession.getMaxMana());
                    tagCompound.setTag(ModLibs.MOD_ID.toLowerCase() + "_" + session.getKey(), tag);
                }
            }
        }
    }
}

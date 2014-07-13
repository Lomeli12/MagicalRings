package net.lomeli.ring.magic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.lib.ModLibs;

public class RingSaveData extends WorldSavedData {
    private List<IPlayerSession> sessions;

    public RingSaveData(String map) {
        super(map);
        sessions = new ArrayList<IPlayerSession>();
    }

    public List<IPlayerSession> getSavaData() {
        return sessions;
    }

    public void setSaveData(List<IPlayerSession> list) {
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
                sessions.add(new PlayerSession(id, mana, max));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        if (sessions != null) {
            for (IPlayerSession session : sessions) {
                NBTTagCompound tag = new NBTTagCompound();
                //tag.setString("UUID", session.getPlayerID());
                tag.setInteger(ModLibs.PLAYER_MP, session.getMana());
                tag.setInteger(ModLibs.PLAYER_MAX, session.getMaxMana());
                tagCompound.setTag(ModLibs.MOD_ID.toLowerCase() + "_" + session.getPlayerID(), tag);
            }
        }
    }
}

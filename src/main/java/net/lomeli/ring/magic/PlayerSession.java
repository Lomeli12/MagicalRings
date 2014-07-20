package net.lomeli.ring.magic;

import net.minecraft.entity.player.EntityPlayer;

import net.lomeli.ring.api.interfaces.IPlayerSession;

public class PlayerSession implements IPlayerSession {
    private String playerID;
    private int mana, maxMana;

    public PlayerSession(String playerID, int mana, int maxMana) {
        this.playerID = playerID;
        this.mana = mana;
        this.maxMana = maxMana;
    }

    public PlayerSession(EntityPlayer player, int mana, int maxMana) {
        this(player.getUniqueID().toString(), mana, maxMana);
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public void setMana(int j) {
        mana = j;
        if (mana > maxMana)
            mana = maxMana;
        else if (mana < 0)
            mana = 0;
    }

    @Override
    public int getMaxMana() {
        return maxMana;
    }

    @Override
    public void setMaxMana(int j) {
        maxMana = j;
    }

    @Override
    public String getPlayerID() {
        return playerID;
    }

    @Override
    public int adjustMana(int j, boolean simulated) {
        int i = mana;
        i += j;
        if (i < 0)
            i = 0;
        else if (i > maxMana)
            i = maxMana;

        if (!simulated)
            mana = i;
        return i;
    }

    @Override
    public int useMana(int j, boolean simulated) {
        return adjustMana(-j, simulated);
    }

    @Override
    public boolean hasEnoughMana(int i) {
        return mana > i;
    }
}

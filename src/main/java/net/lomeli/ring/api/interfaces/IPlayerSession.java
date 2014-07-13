package net.lomeli.ring.api.interfaces;

import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerSession {
    public int getMana();

    public void setMana(int j);

    public int getMaxMana();

    public void setMaxMana(int j);

    /**
     * UUID of the player this session is associated with
     *
     * @return
     */
    public String getPlayerID();

    /**
     * Adjust the players current mana pool (i.e decrease mana, use negative integer, increase, use positive).
     * If simulated, will have no affect on player's mana pool
     *
     * @param j
     * @param simulated
     * @return mana player has left
     */
    public int adjustMana(int j, boolean simulated);

    public boolean hasEnoughMana(int i);

    /**
     * @param tagCompound Unused ATM
     */
    @Deprecated
    public void writeToNBT(NBTTagCompound tagCompound);

    /**
     * @param tagCompound Unused ATM
     */
    @Deprecated
    public void readFromNBT(NBTTagCompound tagCompound);
}

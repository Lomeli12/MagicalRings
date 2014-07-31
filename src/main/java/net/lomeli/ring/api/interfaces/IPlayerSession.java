package net.lomeli.ring.api.interfaces;

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

    /**
     * Basically the same as adjustMana, but integer will become negative
     *
     * @param j
     * @param simulated
     * @return
     */
    public int useMana(int j, boolean simulated);

    /**
     * Check if player has enough mana
     *
     * @param i
     * @return
     */
    public boolean hasEnoughMana(int i);
}

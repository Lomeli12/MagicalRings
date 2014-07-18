package net.lomeli.ring.api.interfaces;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Do NOT implement! Use instance in RingAPI to use these methods.
 */
public interface IManaHandler {
    /**
     * Load player save data from world
     */
    public void loadPlayerData(EntityPlayer player);

    /**
     * Unloads player from current session and saves world data
     */
    public void unloadPlayerSession(EntityPlayer player);

    /**
     * Checks to see if player has a session
     */
    public boolean playerHasSession(EntityPlayer player);

    /**
     * Adds player to session
     */
    public void addPlayerSession(EntityPlayer player, int mana, int max);

    public void addPlayerSession(String uuid, IPlayerSession playerSession, int dimensionID);

    /**
     * Gets the player's session
     */
    public IPlayerSession getPlayerSession(EntityPlayer player);

    /**
     * Updates the player's session
     */
    public void updatePlayerSession(IPlayerSession playerSession, int dimensionID);

    /**
     * Saves all data
     */
    public void saveWorldData();
}

package net.lomeli.ring.api.interfaces;

/**
 * Do NOT implement! Use instance in RingAPI to use these methods.
 */
public interface IRingAPI {
    IPageRegistry pageRegistry();

    ISpellRegistry spellRegistry();

    IMaterialRegistry materialRegistry();

    IManaHandler manaHandler();
}

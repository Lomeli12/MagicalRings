package net.lomeli.ring.api.interfaces;

import java.util.LinkedHashMap;

/**
 * Do NOT implement! Use instance in RingAPI to use these methods.
 */
public interface ISpellRegistry {
    /**
     * Register a new spell and it's required recipe. Can use Items, Blocks, ItemStacks, and OreDictionary names
     */
    public void registerSpell(ISpell spell, String id, Object... obj);

    /**
     * Get a list of all registered spells
     */
    public LinkedHashMap<String, ISpell> getReisteredSpells();

    /**
     * Get spell by it's id
     */
    public ISpell getSpell(String id);

    public Object[] getSpellRecipe(String id);

    public String getSpellID(ISpell spell);

}

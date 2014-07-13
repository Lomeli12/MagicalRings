package net.lomeli.ring.api.interfaces;

import java.util.List;

/**
 * Do NOT implement! Use instance in RingAPI to use these methods.
 */
public interface ISpellRegistry {
    /**
     * Register a new spell and it's required recipe. Can use Items, Blocks, ItemStacks, and OreDictionary names
     */
    public void registerSpell(ISpell spell, Object... obj);

    /**
     * Get a list of all registered spells
     */
    public List<ISpell> getReisteredSpells();

    /**
     * Get spell by it's Unlocalized name
     */
    public ISpell getSpell(String spell);

    public Object[] getSpellRecipe(ISpell spell);

}

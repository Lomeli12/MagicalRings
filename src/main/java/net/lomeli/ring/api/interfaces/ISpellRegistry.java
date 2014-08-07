package net.lomeli.ring.api.interfaces;

import java.util.List;

import net.lomeli.ring.api.interfaces.recipe.ISpellEntry;

/**
 * Do NOT implement! Use instance in RingAPI to use these methods.
 */
public interface ISpellRegistry {
    public void registerSpell(ISpellEntry entry);

    /**
     * Register a new spell and it's required recipe. Can use Items, Blocks, ItemStacks, and OreDictionary names
     */
    public void registerSpell(ISpell spell, String id, Object... obj);

    /**
     * Get a list of all registered spells
     */
    public List<ISpellEntry> getSpellList();

    /**
     * Get spell by it's id
     */
    public ISpell getSpell(String id);

    public Object[] getSpellRecipe(String id);

    public String getSpellID(ISpell spell);

}

package net.lomeli.ring.api.interfaces.recipe;

import net.lomeli.ring.api.interfaces.ISpell;

public interface ISpellEntry {
    public Object[] requireMaterials();

    public String getSpellID();

    public ISpell getSpell();
}

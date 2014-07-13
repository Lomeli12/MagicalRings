package net.lomeli.ring.core;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.*;

public class ApiRing implements IRingAPI {

    public static ApiRing instance;

    public static ApiRing loadInstance() {
        if (instance == null)
            instance = new ApiRing();
        return instance;
    }

    @Override
    public IPageRegistry pageRegistry() {
        return Rings.proxy.pageUtil;
    }

    @Override
    public ISpellRegistry spellRegistry() {
        return Rings.proxy.spellRegistry;
    }

    @Override
    public IMaterialRegistry materialRegistry() {
        return Rings.proxy.ringMaterials;
    }

    @Override
    public IManaHandler manaHandler() {
        return Rings.proxy.manaHandler;
    }
}

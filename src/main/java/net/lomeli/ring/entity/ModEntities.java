package net.lomeli.ring.entity;

import cpw.mods.fml.common.registry.EntityRegistry;

import net.lomeli.ring.Rings;

public class ModEntities {
    public static void registerEntities() {
        EntityRegistry.registerModEntity(EntityFireProofItem.class, "FireProofItem", EntityRegistry.findGlobalUniqueEntityId(), Rings.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityFireStone.class, "FireStoneItem", EntityRegistry.findGlobalUniqueEntityId(), Rings.instance, 64, 20, true);
    }
}

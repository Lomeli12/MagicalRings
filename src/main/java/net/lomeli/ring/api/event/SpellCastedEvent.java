package net.lomeli.ring.api.event;

import net.minecraft.entity.EntityLivingBase;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;

/**
 * Fired when an entity casts a spell using a ring
 * @author Lomeli12
 */
@Cancelable
public class SpellCastedEvent extends Event {
    /** May or may not be the player */
    public final EntityLivingBase entityLiving;
    /** Spell being cast */
    public ISpell spell;
    /** If the entity is not a player, this will be null */
    public IPlayerSession session;

    public SpellCastedEvent(EntityLivingBase entityLiving, ISpell spell, IPlayerSession session) {
        this.entityLiving = entityLiving;
        this.spell = spell;
    }
}

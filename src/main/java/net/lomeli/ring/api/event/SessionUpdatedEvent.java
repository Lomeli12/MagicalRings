package net.lomeli.ring.api.event;

import java.util.List;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

import net.lomeli.ring.api.interfaces.IPlayerSession;

/**
 * Fired when a player's session has been updated
 * @author Lomeli12
 */
@Cancelable
public class SessionUpdatedEvent extends Event{
    /** Player's session */
    public IPlayerSession session;
    /** Dimension the player was in when their session was updated */
    public final int dimensionId;
    /** List of all sessions for that dimension */
    public final List<IPlayerSession> dimensionList;

    public SessionUpdatedEvent(IPlayerSession session, int dimensionId, List<IPlayerSession> dimensionList) {
        this.session = session;
        this.dimensionId = dimensionId;
        this.dimensionList = dimensionList;
    }
}

package net.lomeli.ring.api.event;

import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

import net.lomeli.ring.api.interfaces.IPlayerSession;

/**
 * Fired when a player's session has been updated
 *
 * @author Lomeli12
 */
@Cancelable
public class SessionUpdatedEvent extends Event {
    /**
     * Dimension the player was in when their session was updated
     */
    public final int dimensionId;
    /**
     * List of all sessions for that dimension. Key is player UUID
     */
    public final HashMap<String, IPlayerSession> dimensionList;
    /**
     * Player's session
     */
    public IPlayerSession session;

    public SessionUpdatedEvent(IPlayerSession session, int dimensionId, HashMap<String, IPlayerSession> dimensionList) {
        this.session = session;
        this.dimensionId = dimensionId;
        this.dimensionList = dimensionList;
    }
}

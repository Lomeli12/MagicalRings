package net.lomeli.ring.api;

import net.lomeli.ring.api.interfaces.IRingAPI;

public class RingAPI {
    public static IRingAPI ringAPI;

    public static IRingAPI loadInstance() {
        if (ringAPI == null) {
            try {
                Class clazz = Class.forName("net.lomeli.ring.core.ApiRing");
                ringAPI = (IRingAPI) clazz.getField("instance").get(clazz);
            } catch (Throwable e) {
                return null;
            }
        }
        return ringAPI;
    }
}

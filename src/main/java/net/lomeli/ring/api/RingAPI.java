package net.lomeli.ring.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RingAPI {
    @SideOnly(Side.CLIENT)
    public static IPageRegistry pageRegistry;
    
    public static IMagicHandler magicHandler;
    
    public static IMaterialRegistry materialRegistry;
}

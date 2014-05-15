package net.lomeli.modjam4.lib;

public class ModLibs {
    public static final String MOD_ID = "MagicalRings";
    public static final String MOD_NAME = "Rings";
    public static final String VERSION = "1.0";
    
    public static final String PACKAGE = "net.lomeli.modjam4.";
    public static final String PROXY_SERVER = PACKAGE + "core.Proxy";
    public static final String PROXY_CLIENT = PACKAGE + "client.ProxyClient";
    
    public static final int BASE_MP = 1000;
    
    // Ring NBTTags
    public static final String RING_TAG = MOD_ID + "_ringData";
    public static final String SPELL_ID = "Spell_ID";
    public static final String LAYER1 = "layer1_";
    public static final String L1RGB = LAYER1 + "rgb";
    public static final String LAYER2 = "layer2_";
    public static final String L2RGB = LAYER2 + "rgb";
    public static final String HAS_GEM = "hasGem";
    public static final String GEM_RGB = "gem_rgb";
    public static final String EDIBLE = "edible";
}

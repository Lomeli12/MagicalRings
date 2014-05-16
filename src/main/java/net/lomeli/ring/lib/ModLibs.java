package net.lomeli.ring.lib;

public class ModLibs {
    public static final String MOD_ID = "MagicalRings";
    public static final String MOD_NAME = "Rings";
    public static final String VERSION = "1.0";
    
    public static final String PACKAGE = "net.lomeli.ring.";
    public static final String PROXY_SERVER = PACKAGE + "core.Proxy";
    public static final String PROXY_CLIENT = PACKAGE + "client.ProxyClient";
    
    public static int BASE_MP;
    
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
    public static final String MATERIAL_BOOST = "boost";
    public static final String ACTIVE_EFFECT_ENABLED = "activeEffect";
    
    public static final String CONTAINERS = MOD_ID.toLowerCase() + ".container.";
    public static final String ALTAR = CONTAINERS + "altar";
    public static final String RING_FORGE = CONTAINERS + "ringForge";
    
    // Player NBTTags
    public static final String PLAYER_DATA = MOD_ID + "_PlayerMagicData";
    public static final String PLAYER_MP = "mp";
    public static final String PLAYER_MAX = "max";
    
    public static final String MESSAGE = MOD_ID.toLowerCase() + ".message.";
    public static final String NO_EXP = MESSAGE + "enoughEXP";
    public static final String ACTIVE_EFFECT = MESSAGE + "effect";
    
    public static final int RING_FORGE_GUI = 0;
    
    public static int RECHARGE_WAIT_TIME;
    public static int DISPLAY_X;
    public static int DISPLAY_Y;
    
    public static int altarRenderID;
}

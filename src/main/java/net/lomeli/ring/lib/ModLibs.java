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

    // Message
    public static final String MESSAGE = MOD_ID.toLowerCase() + ".message.";
    public static final String NO_EXP = MESSAGE + "enoughEXP";
    public static final String NO_EXP_PLUS = MESSAGE + "enoughEXP2";
    public static final String ACTIVE_EFFECT = MESSAGE + "effect";
    public static final String SPELL = MESSAGE + "spell";
    public static final String BOOST = MESSAGE + "magicBoost";
    public static final String REQUIRED_ITEMS = MESSAGE + "requiedItems";
    public static final String ORE_DIC = MESSAGE + "oreDic";
    public static final String USES_LEFT = MESSAGE + "usesLeft";
    public static final String HAMMER_INFO = MESSAGE + "hammer";
    public static final String MORE_INFO = MESSAGE + "shift";
    public static final String MANA = MESSAGE + "mana";
    public static final String NO_MANA = MESSAGE + "nomana";

    public static final int RING_FORGE_GUI = 0;
    public static final int BOOK_GUI = 1;

    // Spell names
    public static final String SPELL_NAME = MOD_ID.toLowerCase() + ".spell.";
    public static final String ENDERPORT = SPELL_NAME + "enderPort";
    public static final String FIRE_WRATH = SPELL_NAME + "blazingWrath";
    public static final String THUNDER_SKY = SPELL_NAME + "skyAnger";
    public static final String FRIENDLY_FIRE = SPELL_NAME + "friendly";
    public static final String ANGEL_KISS = SPELL_NAME + "healTouch";
    public static final String WING = SPELL_NAME + "wings";
    public static final String HARVEST = SPELL_NAME + "harvest";
    public static final String DISARM = SPELL_NAME + "disarm";
    public static final String REARM = SPELL_NAME + "rearm";

    public static int RECHARGE_WAIT_TIME;
    public static int DISPLAY_X;
    public static int DISPLAY_Y;

    public static int altarRenderID;

    public static boolean tungstenSpawn;
    public static boolean platinumSpawn;
    public static boolean jadeSpawn;
    public static boolean amberSpawn;
    public static boolean peridotSpawn;
    public static boolean rubySpawn;
    public static boolean sapphireSpawn;
    public static boolean amethystSpawn;

    public static boolean activateRetroGen;

    public static int tungstenSize;
    public static int platinumSize;
    public static int jadeSize;
    public static int amberSize;
    public static int peridotSize;
    public static int rubySize;
    public static int sapphireSize;
    public static int amethystSize;

    public static int tungstenRate;
    public static int platinumRate;
    public static int jadeRate;
    public static int amberRate;
    public static int peridotRate;
    public static int rubyRate;
    public static int sapphireRate;
    public static int amethystRate;
}

package net.lomeli.ring.lib;

public class ModLibs {
    public static final String MOD_ID = "MagicalRings";
    public static final String MOD_NAME = "Magical Rings";
    public static final int MAJOR = 1, MINOR = 0, REVISION = 0;
    public static final String VERSION = MAJOR + "." + MINOR + "." + REVISION;
    public static final String PACKAGE = "net.lomeli.ring.";
    public static final String PROXY_SERVER = PACKAGE + "core.Proxy";
    public static final String PROXY_CLIENT = PACKAGE + "client.ProxyClient";
    public static final String CONFIG_FACTORY = "net.lomeli.ring.client.gui.GuiConfigFactory";
    // Ring NBTTags
    public static final String RING_TAG = MOD_ID + "_ringData";
    // Player NBTTags
    public static final String SAVE_DATA = "RingSaveData";
    public static final String BAUBLES = "Baubles";
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
    public static final String PLAYER_MP = "mp";
    public static final String PLAYER_MAX = "max";
    // Message
    public static final String MESSAGE = MOD_ID.toLowerCase() + ".message.";
    public static final String NO_EXP = MESSAGE + "EXP";
    public static final String EXP_1 = MESSAGE + "enoughEXP";
    public static final String EXP_2 = MESSAGE + "enoughEXP2";
    public static final String ACTIVE_EFFECT = MESSAGE + "effect";
    public static final String SPELL = MESSAGE + "spell";
    public static final String BOOST = MESSAGE + "magicBoost";
    public static final String REQUIRED_ITEMS = MESSAGE + "requiedItems";
    public static final String ORE_DIC = MESSAGE + "oreDic";
    public static final String USES_LEFT = MESSAGE + "usesLeft";
    public static final String HAMMER_INFO = MESSAGE + "hammer";
    public static final String MORE_INFO = MESSAGE + "shift";
    public static final String MANA = MESSAGE + "mana";
    public static final String NO_MANA = MESSAGE + "noMana";
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
    public static final String DISPLACE = SPELL_NAME + "displace";
    public static final String CLEAR_WATER = SPELL_NAME + "clearWater";

    // Command
    public static final String COMMAND = MOD_ID.toLowerCase() + ".command.";
    public static final String COMMAND_HELP = COMMAND + "basicInfo";
    public static final String COMMAND_MANA_POS = COMMAND + "manaDisplay";
    public static final String COMMAND_DISPLAY_MANA = COMMAND + "displayMana";
    public static final String COMMAND_DISPLAY_ON = COMMAND + "displayManaOn";
    public static final String COMMAND_DISPLAY_OFF = COMMAND + "displayManaOff";

    public static int BASE_MP = 100;
    public static int RECHARGE_WAIT_TIME = 70;
    public static int DISPLAY_X = 2;
    public static int DISPLAY_Y = 120;

    public static int altarRenderID;

    public static boolean tungstenSpawn = true;
    public static boolean platinumSpawn = true;
    public static boolean jadeSpawn = true;
    public static boolean amberSpawn = true;
    public static boolean peridotSpawn = true;
    public static boolean rubySpawn = true;
    public static boolean sapphireSpawn = true;
    public static boolean amethystSpawn = true;

    public static boolean activateRetroGen;

    public static int tungstenSize = 1;
    public static int platinumSize = 1;
    public static int jadeSize = 1;
    public static int amberSize = 2;
    public static int peridotSize = 4;
    public static int rubySize = 4;
    public static int sapphireSize = 4;
    public static int amethystSize = 2;

    public static int tungstenRate = 1;
    public static int platinumRate = 2;
    public static int jadeRate = 3;
    public static int amberRate = 4;
    public static int peridotRate = 5;
    public static int rubyRate = 5;
    public static int sapphireRate = 5;
    public static int amethystRate = 4;

    public static int manaFlowerQuantity = 3;
    public static int manaFlowerDensity = 2;

    public static int villagerID = 72380;

    public static int ctRenderID;
}

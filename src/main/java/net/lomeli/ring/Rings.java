package net.lomeli.ring;

import java.io.File;

import net.lomeli.ring.client.handler.GuiHandler;
import net.lomeli.ring.core.CommandRing;
import net.lomeli.ring.core.CreativeRing;
import net.lomeli.ring.core.Proxy;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketAdjustClientPos;
import net.lomeli.ring.network.PacketClientJoined;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketRingName;
import net.lomeli.ring.network.PacketUpdateClient;
import net.lomeli.ring.network.PacketUpdatePlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = ModLibs.MOD_ID, name = ModLibs.MOD_NAME, version = ModLibs.VERSION)
public class Rings {
    @Mod.Instance(ModLibs.MOD_ID)
    public static Rings instance;

    @SidedProxy(clientSide = ModLibs.PROXY_CLIENT, serverSide = ModLibs.PROXY_SERVER)
    public static Proxy proxy;

    public static File modConfig;

    public static PacketHandler packetHandler;

    public static final CreativeTabs modTab = new CreativeRing(ModLibs.MOD_ID.toLowerCase() + ".tab");

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandRing());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configureMod(event.getSuggestedConfigurationFile());
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        packetHandler = new PacketHandler(PacketUpdatePlayerMP.class, PacketAdjustClientPos.class, PacketRingName.class, PacketClientJoined.class, PacketUpdateClient.class);
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        packetHandler.init();
        proxy.postInit();
    }

    public void configureMod(File file) {
        modConfig = file;

        Configuration config = new Configuration(file);

        config.load();

        String modOptions = "Options";

        ModLibs.BASE_MP = config.get(modOptions, "StartingValue", 200, "The base Max MP players start with").getInt(200);
        ModLibs.RECHARGE_WAIT_TIME = config.get(modOptions, "rechargeWaitTime", 70, "Number of ticks between a player's MP recharging. 20 ticks per second").getInt(70);

        String world = "WorldGen";
        config.addCustomCategoryComment(world, "Adjust and enable/disable world gen. Rate = number of times a viens has to spawn per chunk");

        ModLibs.tungstenSpawn = config.get(world, "tungstenSpawn", true).getBoolean(true);
        ModLibs.platinumSpawn = config.get(world, "platinumSpawn", true).getBoolean(true);
        ModLibs.jadeSpawn = config.get(world, "jadeSpawn", true).getBoolean(true);
        ModLibs.amberSpawn = config.get(world, "amberSpawn", true).getBoolean(true);
        ModLibs.peridotSpawn = config.get(world, "peridotSpawn", true).getBoolean(true);
        ModLibs.rubySpawn = config.get(world, "rubySpawn", true).getBoolean(true);
        ModLibs.sapphireSpawn = config.get(world, "sapphireSpawn", true).getBoolean(true);
        ModLibs.amethystSpawn = config.get(world, "amethystSpawn", true).getBoolean(true);

        ModLibs.tungstenRate = config.get(world, "tungstenRate", 1).getInt(1);
        ModLibs.platinumRate = config.get(world, "platinumRate", 1).getInt(1);
        ModLibs.jadeRate = config.get(world, "jadeRate", 1).getInt(1);
        ModLibs.amberRate = config.get(world, "amberRate", 2).getInt(2);
        ModLibs.peridotRate = config.get(world, "peridotRate", 4).getInt(4);
        ModLibs.rubyRate = config.get(world, "rubyRate", 4).getInt(4);
        ModLibs.sapphireRate = config.get(world, "sapphireRate", 4).getInt(4);
        ModLibs.amethystRate = config.get(world, "amethystRate", 2).getInt(2);

        ModLibs.tungstenSize = config.get(world, "tungstenVienSize", 1).getInt(1);
        ModLibs.platinumSize = config.get(world, "platinumVienSize", 2).getInt(2);
        ModLibs.jadeSize = config.get(world, "jadeVienSize", 3).getInt(3);
        ModLibs.amberSize = config.get(world, "amberVienSize", 4).getInt(4);
        ModLibs.peridotSize = config.get(world, "peridotVienSize", 5).getInt(5);
        ModLibs.rubySize = config.get(world, "rubyVienSize", 5).getInt(5);
        ModLibs.sapphireSize = config.get(world, "sapphireVienSize", 5).getInt(5);
        ModLibs.amethystSize = config.get(world, "amethystVienSize", 4).getInt(4);

        config.save();
    }

}

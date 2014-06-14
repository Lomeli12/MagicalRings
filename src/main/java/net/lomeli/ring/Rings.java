package net.lomeli.ring;

import java.util.EnumMap;
import java.util.logging.Logger;

import net.minecraft.creativetab.CreativeTabs;

import net.lomeli.ring.client.handler.GuiHandler;
import net.lomeli.ring.core.CommandRing;
import net.lomeli.ring.core.CreativeRing;
import net.lomeli.ring.core.Proxy;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.ChannelHandler;
import net.lomeli.ring.network.PacketHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = ModLibs.MOD_ID, name = ModLibs.MOD_NAME, version = ModLibs.VERSION, dependencies="after:Baubles")
public class Rings {
    @Mod.Instance(ModLibs.MOD_ID)
    public static Rings instance;

    @SidedProxy(clientSide = ModLibs.PROXY_CLIENT, serverSide = ModLibs.PROXY_SERVER)
    public static Proxy proxy;

    public static final Logger log = Logger.getLogger(ModLibs.MOD_ID);

    public static EnumMap<Side, FMLEmbeddedChannel> channel;

    public static final CreativeTabs modTab = new CreativeRing(ModLibs.MOD_ID.toLowerCase() + ".tab");

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandRing());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.loadConfig(event.getSuggestedConfigurationFile());
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        channel = NetworkRegistry.INSTANCE.newChannel(ModLibs.MOD_ID, new ChannelHandler(), new PacketHandler());
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}

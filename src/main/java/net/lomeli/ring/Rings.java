package net.lomeli.ring;

import net.minecraft.creativetab.CreativeTabs;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;

import net.lomeli.ring.client.handler.GuiHandler;
import net.lomeli.ring.core.CommandRing;
import net.lomeli.ring.core.CreativeRing;
import net.lomeli.ring.core.Proxy;
import net.lomeli.ring.core.handler.ConfigurationHandler;
import net.lomeli.ring.core.helper.IMCHelper;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketHandler;

@Mod(modid = ModLibs.MOD_ID, name = ModLibs.MOD_NAME, version = ModLibs.VERSION, dependencies = "after:Baubles", guiFactory = ModLibs.CONFIG_FACTORY)
public class Rings {
    public static final CreativeTabs modTab = new CreativeRing(ModLibs.MOD_ID.toLowerCase() + ".tab");
    public static ConfigurationHandler configHandler;
    @Mod.Instance(ModLibs.MOD_ID)
    public static Rings instance;
    @SidedProxy(clientSide = ModLibs.PROXY_CLIENT, serverSide = ModLibs.PROXY_SERVER)
    public static Proxy proxy;
    public static PacketHandler pktHandler;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandRing());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configHandler = new ConfigurationHandler(new Configuration(event.getSuggestedConfigurationFile()));
        configHandler.updateConfig();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        pktHandler = new PacketHandler();
        FMLCommonHandler.instance().bus().register(configHandler);
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        Rings.proxy.manaHandler.unloadAllData();
    }

    @Mod.EventHandler
    public void handleIMCMessages(FMLInterModComms.IMCEvent event) {
        IMCHelper.processMessages(event.getMessages());
    }
}

package net.lomeli.ring;

import java.io.File;

import net.lomeli.ring.core.CommandRing;
import net.lomeli.ring.core.CreativeRing;
import net.lomeli.ring.core.Proxy;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketAdjustClientPos;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketUpdatePlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModLibs.MOD_ID, name=ModLibs.MOD_NAME, version=ModLibs.VERSION)
public class Rings {
    @Mod.Instance("temp")
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
        packetHandler = new PacketHandler(PacketUpdatePlayerMP.class, PacketAdjustClientPos.class);
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
        
        config.save();
    }
    
}

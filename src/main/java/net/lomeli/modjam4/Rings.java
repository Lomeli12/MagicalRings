package net.lomeli.modjam4;

import net.lomeli.modjam4.core.CreativeRing;
import net.lomeli.modjam4.core.Proxy;
import net.lomeli.modjam4.lib.ModLibs;
import net.lomeli.modjam4.network.PacketHandler;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModLibs.MOD_ID, name=ModLibs.MOD_NAME, version=ModLibs.VERSION)
public class Rings {
    @Mod.Instance("temp")
    public static Rings instance;
    
    @SidedProxy(clientSide = ModLibs.PROXY_CLIENT, serverSide = ModLibs.PROXY_SERVER)
    public static Proxy proxy;
    
    public static PacketHandler packetHandler;
    
    public static final CreativeTabs modTab = new CreativeRing(ModLibs.MOD_ID.toLowerCase() + ".tab");
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        packetHandler = new PacketHandler();
        proxy.init();
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        packetHandler.init();
        proxy.postInit();
    }

}

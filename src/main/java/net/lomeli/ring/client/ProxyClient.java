package net.lomeli.ring.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.RingAPI;
import net.lomeli.ring.block.tile.TileAltar;
import net.lomeli.ring.block.tile.TileItemAltar;
import net.lomeli.ring.client.gui.PageUtil;
import net.lomeli.ring.client.handler.HudHandler;
import net.lomeli.ring.client.handler.TickHandlerClient;
import net.lomeli.ring.client.render.RenderAltar;
import net.lomeli.ring.core.Proxy;
import net.lomeli.ring.lib.ModLibs;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ProxyClient extends Proxy {
    @Override
    public void preInit() {
        clientConfig();
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        ModLibs.altarRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderAltar altarRenderer = new RenderAltar();
        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, altarRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileItemAltar.class, altarRenderer);

        FMLCommonHandler.instance().bus().register(new TickHandlerClient());
        MinecraftForge.EVENT_BUS.register(new HudHandler());
    }

    @Override
    public void postInit() {
        super.postInit();

        RingAPI.pageRegistry = new PageUtil();
    }

    public void clientConfig() {
        Configuration config = new Configuration(Rings.modConfig);

        config.load();

        String clientOptions = "clientOptions";

        Minecraft mc = Minecraft.getMinecraft();

        int y = (mc.displayHeight / 4);

        ConfigCategory cat = config.getCategory(clientOptions) != null ? config.getCategory(clientOptions) : new ConfigCategory(clientOptions);
        cat.setComment("Change the x and y position where your MP is displayed.");

        Property propX = cat.get("displayX") != null ? cat.get("displayX") : new Property("displayX", 2 + "", Type.INTEGER);
        Property propY = cat.get("displayY") != null ? cat.get("displayY") : new Property("displayY", y + "", Type.INTEGER);

        ModLibs.DISPLAY_X = propX.getInt(2);
        ModLibs.DISPLAY_Y = propY.getInt(y);

        cat.put("displayX", propX);
        cat.put("displayY", propY);

        config.save();
    }

    @Override
    public EntityPlayer getPlayerFromNetHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer)
            return ((NetHandlerPlayServer) handler).playerEntity;
        else
            return Minecraft.getMinecraft().thePlayer;
    }
}

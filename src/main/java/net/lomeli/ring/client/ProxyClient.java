package net.lomeli.ring.client;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

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

    @Override
    public void loadConfig(File file) {
        Configuration config = new Configuration(file);

        config.load();

        baseConfig(config);

        int y = (Minecraft.getMinecraft().displayHeight / 4);
        clientConfig(config, 2, y);

        config.save();
        
        this.modConfig = file;
    }
    
    @Override
    public void changeClientConfig(int x, int y) {
        Configuration config = new Configuration(this.modConfig);

        config.load();

        baseConfig(config);

        clientConfig(config, x, y);

        config.save();
    }

    private void clientConfig(Configuration config, int x, int y) {
        String clientOptions = "clientOptions";

        ConfigCategory cat = config.getCategory(clientOptions) != null ? config.getCategory(clientOptions) : new ConfigCategory(clientOptions);
        cat.setComment("Change the x and y position where your MP is displayed.");

        Property propX = cat.get("displayX") != null ? cat.get("displayX") : new Property("displayX", x + "", Type.INTEGER);
        Property propY = cat.get("displayY") != null ? cat.get("displayY") : new Property("displayY", y + "", Type.INTEGER);

        ModLibs.DISPLAY_X = propX.getInt(x);
        ModLibs.DISPLAY_Y = propY.getInt(y);

        cat.put("displayX", propX);
        cat.put("displayY", propY);
    }

    @Override
    public EntityPlayer getPlayerFromNetHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer)
            return ((NetHandlerPlayServer) handler).playerEntity;
        else
            return Minecraft.getMinecraft().thePlayer;
    }
}

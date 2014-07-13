package net.lomeli.ring.client;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

import net.lomeli.ring.block.tile.TileAltar;
import net.lomeli.ring.block.tile.TileItemAltar;
import net.lomeli.ring.client.handler.HudHandler;
import net.lomeli.ring.client.handler.TickHandlerClient;
import net.lomeli.ring.client.render.RenderAltar;
import net.lomeli.ring.client.render.RenderGhostSword;
import net.lomeli.ring.client.render.RenderRing;
import net.lomeli.ring.client.render.RenderSpellParchment;
import net.lomeli.ring.core.Proxy;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;

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
        MinecraftForgeClient.registerItemRenderer(ModItems.spellParchment, new RenderSpellParchment());
        MinecraftForgeClient.registerItemRenderer(ModItems.ghostSword, new RenderGhostSword());
        MinecraftForgeClient.registerItemRenderer(ModItems.magicRing, new RenderRing());

        FMLCommonHandler.instance().bus().register(new TickHandlerClient());
        MinecraftForge.EVENT_BUS.register(new HudHandler());
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    @Override
    public void updateConfig() {
        config.load();

        baseConfig(config);

        clientConfig(config, ModLibs.DISPLAY_X, ModLibs.DISPLAY_Y);

        config.save();
    }

    @Override
    public void changeClientConfig(int x, int y) {
        config.load();

        baseConfig(config);

        clientConfig(config, x, y);

        config.save();
    }

    private void clientConfig(Configuration config, int x, int y) {
        String clientOptions = "clientoptions";

        ConfigCategory cat = config.getCategory(clientOptions) != null ? config.getCategory(clientOptions) : new ConfigCategory(clientOptions);
        cat.setComment("Change the x and y position where your MP is displayed.");

        Property propX = cat.get("displayX") != null ? cat.get("displayX") : new Property("displayX", x + "", Type.INTEGER);
        Property propY = cat.get("displayY") != null ? cat.get("displayY") : new Property("displayY", y + "", Type.INTEGER);

        ModLibs.DISPLAY_X = propX.getInt(x);
        ModLibs.DISPLAY_Y = propY.getInt(y);

        cat.put("displayX", propX);
        cat.put("displayY", propY);
    }
}

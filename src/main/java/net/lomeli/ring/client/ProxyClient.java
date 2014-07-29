package net.lomeli.ring.client;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

import net.lomeli.ring.block.tile.TileAltar;
import net.lomeli.ring.block.tile.TileItemAltar;
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

        FMLCommonHandler.instance().bus().register(tickClient);

        MinecraftForge.EVENT_BUS.register(renderHandler);
    }

    @Override
    public void postInit() {
        super.postInit();
    }
}

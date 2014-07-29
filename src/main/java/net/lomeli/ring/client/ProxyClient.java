package net.lomeli.ring.client;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.VillagerRegistry;

import net.lomeli.ring.block.tile.TileAltar;
import net.lomeli.ring.block.tile.TileItemAltar;
import net.lomeli.ring.client.render.*;
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
        ModLibs.ctRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderAltar altarRenderer = new RenderAltar();

        RenderingRegistry.registerBlockHandler(new RenderCTBlock(ModLibs.ctRenderID));

        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, altarRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileItemAltar.class, altarRenderer);

        MinecraftForgeClient.registerItemRenderer(ModItems.spellParchment, new RenderSpellParchment());
        MinecraftForgeClient.registerItemRenderer(ModItems.ghostSword, new RenderGhostSword());
        MinecraftForgeClient.registerItemRenderer(ModItems.magicRing, new RenderRing());



        FMLCommonHandler.instance().bus().register(tickClient);

        MinecraftForge.EVENT_BUS.register(renderHandler);

        VillagerRegistry.instance().registerVillagerSkin(ModLibs.villagerID, new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":textures/entities/manaVillager.png"));
    }

    @Override
    public void postInit() {
        super.postInit();
    }
}

package net.lomeli.ring.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.feature.WorldGenerator;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.lomeli.ring.worldgen.WorldGenManager.ChunkCoord;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class TickHandlerCore {
    public List<Integer> flyingPlayerList = new ArrayList<Integer>();

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            MinecraftServer ms = MinecraftServer.getServer();
            if (ModLibs.activateRetroGen) {
                int dim = ms.getEntityWorld().provider.dimensionId;
                ArrayList<ChunkCoord> chunks = Rings.proxy.genManager.pendingWork.get(Integer.valueOf(dim));
                if (chunks != null && !chunks.isEmpty()) {
                    ChunkCoord c = chunks.get(0);
                    long worldSeed = ms.getEntityWorld().getSeed();
                    Random rand = new Random(worldSeed);
                    long xSeed = rand.nextLong() >> 3;
                    long zSeed = rand.nextLong() >> 3;
                    rand.setSeed(xSeed * c.chunkX + zSeed * c.chunkZ ^ worldSeed);
                    for (Entry<WorldGenerator, Integer> ent : Rings.proxy.genManager.generatorList.entrySet()) {
                        WorldGenerator worldGen = ent.getKey();
                        if (worldGen != null)
                            worldGen.generate(ms.getEntityWorld(), rand, (int) xSeed, ent.getValue(), (int) zSeed);
                    }
                    chunks.remove(c);
                    Rings.proxy.genManager.pendingWork.put(Integer.valueOf(dim), chunks);
                    ArrayList<ChunkCoord> completedChunks = Rings.proxy.genManager.completedWork.get(Integer.valueOf(dim));
                    if (completedChunks == null) {
                        Rings.proxy.genManager.completedWork.put(Integer.valueOf(dim), new ArrayList<ChunkCoord>());
                        completedChunks = Rings.proxy.genManager.completedWork.get(Integer.valueOf(dim));
                    }
                    if (completedChunks != null) {
                        completedChunks.add(c);
                        Rings.proxy.genManager.completedWork.put(Integer.valueOf(dim), completedChunks);
                    }
                }
            }

            for (int i = 0; i < flyingPlayerList.size(); i++) {
                int entityId = flyingPlayerList.get(i);

                EntityPlayer player = (EntityPlayer) ms.getEntityWorld().getEntityByID(entityId);
                if (player.capabilities.isCreativeMode) {
                    flyingPlayerList.remove(i);
                    return;
                }

                if (MagicHandler.canUse(player, 1)) {
                    if (!player.capabilities.allowFlying)
                        player.capabilities.allowFlying = true;

                    if (player.capabilities.isFlying)
                        MagicHandler.modifyPlayerMP(player, -1);
                }else if (player.capabilities.allowFlying) {
                    player.capabilities.allowFlying = false;
                    flyingPlayerList.remove(i);
                }
            }

        }
    }
}

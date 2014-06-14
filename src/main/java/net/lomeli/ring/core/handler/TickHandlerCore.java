package net.lomeli.ring.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.lomeli.ring.worldgen.WorldGenManager.ChunkCoord;
import net.lomeli.ring.worldgen.WorldGenManager.GeneratorInfo;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class TickHandlerCore {

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (ModLibs.activateRetroGen) {
                int dim = event.world.provider.dimensionId;
                ArrayList<ChunkCoord> chunks = Rings.proxy.genManager.pendingWork.get(Integer.valueOf(dim));
                if (chunks != null && !chunks.isEmpty()) {
                    ChunkCoord c = (ChunkCoord) chunks.get(0);
                    Rings.log.log(Level.INFO, "Retroactively generating ores at " + c.toString() + ".");
                    ArrayList<ChunkCoord> completed = Rings.proxy.genManager.completedWork.get(Integer.valueOf(dim));
                    if (completed == null) {
                        Rings.proxy.genManager.completedWork.put(Integer.valueOf(dim), new ArrayList<ChunkCoord>());
                        completed = Rings.proxy.genManager.completedWork.get(Integer.valueOf(dim));
                    }

                    ArrayList<GeneratorInfo> overworld = Rings.proxy.genManager.genList.get(Integer.valueOf(dim));
                    if (overworld != null && !overworld.isEmpty()) {
                        for (GeneratorInfo info : overworld) {
                            Rings.proxy.genManager.generateOre(info.block, info.meta, event.world, event.world.rand, c.chunkX, c.chunkZ, info.numViens, info.veinSize, info.minY, info.maxY);
                        }
                    }

                    Rings.log.log(Level.INFO, "Finished retro gen for " + c.toString() + ". Removing from work load.");
                    chunks.remove(c);
                    Rings.proxy.genManager.pendingWork.put(Integer.valueOf(dim), chunks);

                    completed.add(c);
                    Rings.proxy.genManager.completedWork.put(Integer.valueOf(dim), completed);
                }
            }
        }
    }
}

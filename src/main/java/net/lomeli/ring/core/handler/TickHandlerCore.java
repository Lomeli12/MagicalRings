package net.lomeli.ring.core.handler;

import java.util.ArrayList;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import net.lomeli.ring.Rings;
import net.lomeli.ring.core.handler.WorldGenHandler.ChunkCoord;
import net.lomeli.ring.core.handler.WorldGenHandler.GeneratorInfo;
import net.lomeli.ring.core.helper.LogHelper;
import net.lomeli.ring.lib.ModLibs;

public class TickHandlerCore {

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (ModLibs.activateRetroGen) {
                int dim = event.world.provider.dimensionId;
                ArrayList<ChunkCoord> chunks = Rings.proxy.genManager.pendingWork.get(Integer.valueOf(dim));
                if (chunks != null && !chunks.isEmpty()) {
                    ChunkCoord c = chunks.get(0);
                    LogHelper.info("Retroactively generating ores at " + c.toString() + ".");
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

                    LogHelper.info("Finished retro gen for " + c.toString() + ". Removing from work load.");
                    chunks.remove(c);
                    Rings.proxy.genManager.pendingWork.put(Integer.valueOf(dim), chunks);

                    completed.add(c);
                    Rings.proxy.genManager.completedWork.put(Integer.valueOf(dim), completed);
                }
            }
        }
    }
}

package net.lomeli.ring.core.handler;

import net.minecraft.world.biome.BiomeGenBase;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.lib.ModLibs;

public class BiomeDecorHandler {
    private BiomeDictionary.Type[] validTypes = {BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MAGICAL};

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWorldDecoration(DecorateBiomeEvent.Decorate event) {
        BiomeGenBase biome = event.world.getBiomeGenForCoords(event.chunkX, event.chunkZ);
        if (biome != null && isValidBiomeType(biome)) {
            if ((event.getResult() == Event.Result.ALLOW || event.getResult() == Event.Result.DEFAULT) && event.type == DecorateBiomeEvent.Decorate.EventType.FLOWERS) {
                for(int i = 0; i < ModLibs.manaFlowerQuantity; i++) {
                    int x = event.chunkX + event.rand.nextInt(16) + 8;
                    int z = event.chunkZ + event.rand.nextInt(16) + 8;
                    int y = event.world.getTopSolidOrLiquidBlock(x, z);

                    for (int j = 0; j < ModLibs.manaFlowerDensity; j++) {
                        int x1 = x + event.rand.nextInt(8) - event.rand.nextInt(8);
                        int y1 = y + event.rand.nextInt(4) - event.rand.nextInt(4);
                        int z1 = z + event.rand.nextInt(8) - event.rand.nextInt(8);

                        if(event.world.isAirBlock(x1, y1, z1) && (!event.world.provider.hasNoSky || y1 < 127) && ModBlocks.manaFlower.canBlockStay(event.world, x1, y1, z1))
                            event.world.setBlock(x1, y1, z1, ModBlocks.manaFlower, event.rand.nextInt(3), 2);
                    }
                }
            }
        }
    }

    private boolean isValidBiomeType(BiomeGenBase biome) {
        for (int i = 0; i < validTypes.length; i++) {
            if (BiomeDictionary.isBiomeOfType(biome, validTypes[i]))
                return true;
        }
        return false;
    }
}

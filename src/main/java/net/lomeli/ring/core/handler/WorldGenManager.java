package net.lomeli.ring.core.handler;

import java.util.Random;

import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.lib.ModLibs;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenManager implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.dimensionId == 0)
            generateOverworld(world, random, chunkX, chunkZ);
    }

    private void generateOverworld(World world, Random rand, int x, int z) {
        if (ModLibs.tungstenSpawn)
            addOreSpawn(ModBlocks.oreBlocks, 0, world, rand, x, z, 16, 16, ModLibs.tungstenSize, ModLibs.tungstenRate, 0, 16);
        if (ModLibs.platinumSpawn)
            addOreSpawn(ModBlocks.oreBlocks, 1, world, rand, x, z, 16, 16, ModLibs.platinumSize, ModLibs.platinumRate, 0, 20);
        if (ModLibs.jadeSpawn)
            addOreSpawn(ModBlocks.oreBlocks, 2, world, rand, x, z, 16, 16, ModLibs.jadeSize, ModLibs.jadeRate, 0, 30);
        if (ModLibs.amberSpawn)
            addOreSpawn(ModBlocks.oreBlocks, 3, world, rand, x, z, 16, 16, ModLibs.amberSize, ModLibs.amberRate, 0, 40);
        if (ModLibs.peridotSpawn)
            addOreSpawn(ModBlocks.oreBlocks, 4, world, rand, x, z, 16, 16, ModLibs.peridotSize, ModLibs.peridotRate, 0, 55);
        if (ModLibs.rubySpawn)
            addOreSpawn(ModBlocks.oreBlocks, 5, world, rand, x, z, 16, 16, ModLibs.rubySize, ModLibs.rubyRate, 0, 55);
        if (ModLibs.sapphireSpawn)
            addOreSpawn(ModBlocks.oreBlocks, 6, world, rand, x, z, 16, 16, ModLibs.sapphireSize, ModLibs.sapphireRate, 0, 55);
        if (ModLibs.amethystSpawn)
            addOreSpawn(ModBlocks.oreBlocks, 7, world, rand, x, z, 16, 16, ModLibs.amethystSize, ModLibs.amethystRate, 0, 35);
    }

    private void addOreSpawn(Block block, World world, Random rand, int blockX, int blockZ, int maxX, int maxZ, int maxVienSize, int chance, int minY, int maxY) {
        addOreSpawn(block, 0, world, rand, blockX, blockZ, maxX, maxZ, maxVienSize, chance, minY, maxY);
    }

    private void addOreSpawn(Block block, int meta, World world, Random rand, int blockX, int blockZ, int maxX, int maxZ, int maxVienSize, int chance, int minY, int maxY) {
        assert maxY > minY;
        assert maxX > 0 && maxX <= 16;
        assert minY > 0;
        assert maxY < 256 && maxY > 0;
        assert maxZ > 0 && maxZ <= 16;

        int diff = maxY - minY;
        for (int x = 0; x < chance; x++) {
            int posX = blockX + rand.nextInt(maxX);
            int posY = minY + rand.nextInt(diff);
            int posZ = blockZ + rand.nextInt(maxZ);
            (new WorldGenMinable(block, meta, maxVienSize, Blocks.stone)).generate(world, rand, posX, posY, posZ);
        }
    }
}

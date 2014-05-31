package net.lomeli.ring.worldgen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

import net.minecraftforge.event.world.ChunkDataEvent;

import net.lomeli.ring.Rings;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.lib.ModLibs;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class WorldGenManager implements IWorldGenerator {
    public HashMap<WorldGenerator, Integer> generatorList;
    public HashMap<Integer, ArrayList<ChunkCoord>> pendingWork;
    public HashMap<Integer, ArrayList<ChunkCoord>> completedWork;

    public WorldGenManager() {
        this.generatorList = new HashMap<WorldGenerator, Integer>();
        this.pendingWork = new HashMap<Integer, ArrayList<ChunkCoord>>();
        this.completedWork = new HashMap<Integer, ArrayList<ChunkCoord>>();
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.dimensionId == 0)
            generateOverworld(world, random, chunkX, chunkZ);
    }

    private void generateOverworld(World world, Random rand, int x, int z) {
        if (ModLibs.tungstenSpawn)
            generateOre(ModBlocks.oreBlocks, world, rand, x, z, ModLibs.tungstenRate, ModLibs.tungstenSize, 0, 16);
        if (ModLibs.platinumSpawn)
            generateOre(ModBlocks.oreBlocks, 1, world, rand, x, z, ModLibs.platinumSize, ModLibs.platinumRate, 0, 20);
        if (ModLibs.jadeSpawn)
            generateOre(ModBlocks.oreBlocks, 2, world, rand, x, z, ModLibs.jadeSize, ModLibs.jadeRate, 0, 30);
        if (ModLibs.amberSpawn)
            generateOre(ModBlocks.oreBlocks, 3, world, rand, x, z, ModLibs.amberSize, ModLibs.amberRate, 0, 40);
        if (ModLibs.peridotSpawn)
            generateOre(ModBlocks.oreBlocks, 4, world, rand, x, z, ModLibs.peridotSize, ModLibs.peridotRate, 0, 55);
        if (ModLibs.rubySpawn)
            generateOre(ModBlocks.oreBlocks, 5, world, rand, x, z, ModLibs.rubySize, ModLibs.rubyRate, 0, 55);
        if (ModLibs.sapphireSpawn)
            generateOre(ModBlocks.oreBlocks, 6, world, rand, x, z, ModLibs.sapphireSize, ModLibs.sapphireRate, 0, 55);
        if (ModLibs.amethystSpawn)
            generateOre(ModBlocks.oreBlocks, 7, world, rand, x, z, ModLibs.amethystSize, ModLibs.amethystRate, 0, 35);
    }

    private void generateOre(Block block, int meta, World world, Random rand, int chunkX, int chunkZ, int numVeins, int veinSize, int minY, int maxY) {
        generateOre(block, meta, world, rand, chunkX, chunkZ, numVeins, veinSize, minY, maxY, Blocks.stone);
    }

    private void generateOre(Block block, World world, Random rand, int chunkX, int chunkZ, int numVeins, int veinSize, int minY, int maxY) {
        generateOre(block, world, rand, chunkX, chunkZ, numVeins, veinSize, minY, maxY, Blocks.stone);
    }

    private void generateOre(Block block, int meta, World world, Random rand, int chunkX, int chunkZ, int numVeins, int veinSize, int minY, int maxY, Block replace) {
        for (int i = 0; i < numVeins; i++) {
            if (veinSize <= 0)
                break;
            WorldGenerator worldGen = new WorldGenMinable(block, meta, veinSize, replace);

            int x = chunkX * 16 + rand.nextInt(16);
            int y = rand.nextInt(maxY - minY + 1) + minY;
            int z = chunkZ * 16 + rand.nextInt(16);

            this.generatorList.put(worldGen, y);

            worldGen.generate(world, rand, x, y, z);
        }
    }

    private void generateOre(Block block, World world, Random rand, int chunkX, int chunkZ, int numVeins, int veinSize, int minY, int maxY, Block replace) {
        generateOre(block, 0, world, rand, chunkX, chunkZ, numVeins, veinSize, minY, maxY, replace);
    }

    @SubscribeEvent
    public void handleChunkSaveEvent(ChunkDataEvent.Save event) {
        NBTTagCompound tag = new NBTTagCompound();
        ChunkCoord cCoord = new ChunkCoord(event.getChunk());
        int dim = event.world.provider.dimensionId;
        ArrayList<ChunkCoord> chunks = this.completedWork.get(Integer.valueOf(dim));
        if (chunks != null) {
            if (chunks.contains(cCoord))
                tag.setBoolean("AlreadyGened", true);
        }
        event.getData().setTag(ModLibs.MOD_ID, tag);
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        if (ModLibs.activateRetroGen) {
            ChunkCoord cCoord = new ChunkCoord(event.getChunk());
            Rings.log.log(Level.INFO, "Retroactively generating features for the chunk at " + cCoord.toString() + ".");
            int dim = event.world.provider.dimensionId;
            ArrayList<ChunkCoord> chunks = this.pendingWork.get(Integer.valueOf(dim));
            if (chunks == null) {
                this.pendingWork.put(Integer.valueOf(dim), new ArrayList<ChunkCoord>());
                chunks = this.pendingWork.get(Integer.valueOf(dim));
            }

            if (chunks != null) {
                NBTTagCompound tag = event.getData().hasKey(ModLibs.MOD_ID) ? event.getData().getCompoundTag(ModLibs.MOD_ID) : new NBTTagCompound();
                boolean flag = tag.getBoolean("AlreadyGened");
                if (this.completedWork.containsKey(Integer.valueOf(dim))) {
                    ArrayList<ChunkCoord> completed = this.completedWork.get(Integer.valueOf(dim));
                    if (completed != null && !completed.isEmpty()) {
                        if (completed.contains(cCoord))
                            flag = true;
                    }
                }
                if (flag) {
                    if (chunks.contains(cCoord))
                        chunks.remove(cCoord);
                }else {
                    chunks.add(cCoord);
                    this.pendingWork.put(Integer.valueOf(dim), chunks);
                }
            }
        }
    }

    public static final class ChunkCoord implements Comparable<ChunkCoord>, Serializable {
        private static final long serialVersionUID = 1L;
        public static final int[][] SIDE_COORD_MOD = { { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 }, { -1, 0, 0 }, { 1, 0, 0 } };
        public int chunkX;
        public int chunkZ;

        public ChunkCoord(Chunk chunk) {
            this.chunkX = chunk.xPosition;
            this.chunkZ = chunk.zPosition;
        }

        public ChunkCoord(int x, int z) {
            this.chunkX = x;
            this.chunkZ = z;
        }

        public int getCenterX() {
            return (this.chunkX << 4) + 8;
        }

        public int getCenterZ() {
            return (this.chunkZ << 4) + 8;
        }

        public void step(int dir) {
            this.chunkX = SIDE_COORD_MOD[dir][0];
            this.chunkZ = SIDE_COORD_MOD[dir][2];
        }

        public void step(int dir, int dist) {
            switch(dir) {
            case 2 :
                this.chunkZ -= dist;
                break;
            case 3 :
                this.chunkZ += dist;
                break;
            case 4 :
                this.chunkX -= dist;
                break;
            case 5 :
                this.chunkX += dist;
                break;
            }
        }

        public ChunkCoord copy() {
            return new ChunkCoord(this.chunkX, this.chunkZ);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ChunkCoord)) {
                return false;
            }
            ChunkCoord other = (ChunkCoord) obj;
            return (this.chunkX == other.chunkX) && (this.chunkZ == other.chunkZ);
        }

        @Override
        public int hashCode() {
            int hash = this.chunkX;
            hash *= (31 + this.chunkZ);
            return hash;
        }

        @Override
        public String toString() {
            return "[" + this.chunkX + ", " + this.chunkZ + "]";
        }

        @Override
        public int compareTo(ChunkCoord other) {
            return this.chunkX == other.chunkX ? this.chunkZ - other.chunkZ : this.chunkX - other.chunkX;
        }
    }
}

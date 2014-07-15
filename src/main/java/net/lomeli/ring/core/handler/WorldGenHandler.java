package net.lomeli.ring.core.handler;

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

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.ring.Rings;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.core.helper.LogHelper;
import net.lomeli.ring.lib.ModLibs;

public class WorldGenHandler implements IWorldGenerator {
    public HashMap<Integer, ArrayList<GeneratorInfo>> genList;
    public HashMap<Integer, ArrayList<ChunkCoord>> completedWork;
    public HashMap<Integer, ArrayList<ChunkCoord>> pendingWork;

    public WorldGenHandler() {
        this.genList = new HashMap<Integer, ArrayList<GeneratorInfo>>();
        this.completedWork = new HashMap<Integer, ArrayList<ChunkCoord>>();
        this.pendingWork = new HashMap<Integer, ArrayList<ChunkCoord>>();
        ArrayList<GeneratorInfo> overworld = new ArrayList<GeneratorInfo>();
        if (ModLibs.tungstenSpawn)
            overworld.add(new GeneratorInfo(ModBlocks.oreBlocks, ModLibs.tungstenRate, ModLibs.tungstenSize, 0, 16));
        if (ModLibs.platinumSpawn)
            overworld.add(new GeneratorInfo(ModBlocks.oreBlocks, 1, ModLibs.platinumSize, ModLibs.platinumRate, 0, 20));
        if (ModLibs.jadeSpawn)
            overworld.add(new GeneratorInfo(ModBlocks.oreBlocks, 2, ModLibs.jadeSize, ModLibs.jadeRate, 0, 30));
        if (ModLibs.amberSpawn)
            overworld.add(new GeneratorInfo(ModBlocks.oreBlocks, 3, ModLibs.amberSize, ModLibs.amberRate, 0, 40));
        if (ModLibs.peridotSpawn)
            overworld.add(new GeneratorInfo(ModBlocks.oreBlocks, 4, ModLibs.peridotSize, ModLibs.peridotRate, 0, 55));
        if (ModLibs.rubySpawn)
            overworld.add(new GeneratorInfo(ModBlocks.oreBlocks, 5, ModLibs.rubySize, ModLibs.rubyRate, 0, 55));
        if (ModLibs.sapphireSpawn)
            overworld.add(new GeneratorInfo(ModBlocks.oreBlocks, 6, ModLibs.sapphireSize, ModLibs.sapphireRate, 0, 55));
        if (ModLibs.amethystSpawn)
            overworld.add(new GeneratorInfo(ModBlocks.oreBlocks, 7, ModLibs.amethystSize, ModLibs.amethystRate, 0, 35));
        this.genList.put(0, overworld);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.dimensionId == 0)
            generateOverworld(world, random, chunkX, chunkZ);
    }

    private void generateOverworld(World world, Random rand, int x, int z) {
        ArrayList<GeneratorInfo> overworld = this.genList.get(0);
        if (overworld != null && !overworld.isEmpty()) {
            for (GeneratorInfo info : overworld) {
                if (info != null)
                    this.generateOre(info.block, info.meta, world, rand, x, z, info.numViens, info.veinSize, info.minY, info.maxY);
            }
        }
    }

    public void generateOre(Block block, int meta, World world, Random rand, int chunkX, int chunkZ, int numVeins, int veinSize, int minY, int maxY) {
        generateOre(block, meta, world, rand, chunkX, chunkZ, numVeins, veinSize, minY, maxY, Blocks.stone);
    }

    private void generateOre(Block block, int meta, World world, Random rand, int chunkX, int chunkZ, int numVeins, int veinSize, int minY, int maxY, Block replace) {
        for (int i = 0; i < numVeins; i++) {
            if (veinSize <= 0)
                break;
            WorldGenerator worldGen = new WorldGenMinable(block, meta, veinSize, replace);

            int x = chunkX * 16 + rand.nextInt(16);
            int y = rand.nextInt(maxY - minY + 1) + minY;
            int z = chunkZ * 16 + rand.nextInt(16);

            worldGen.generate(world, rand, x, y, z);
        }
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
            if (event.getData().hasKey(ModLibs.MOD_ID) && event.getData().getCompoundTag(ModLibs.MOD_ID).getBoolean("AlreadyGened"))
                return;
            else {
                int dim = event.world.provider.dimensionId;
                ArrayList<ChunkCoord> chunks = this.pendingWork.get(dim);
                if (chunks == null) {
                    this.pendingWork.put(Integer.valueOf(dim), new ArrayList<ChunkCoord>());
                    chunks = this.pendingWork.get(Integer.valueOf(dim));
                }
                if (chunks != null) {
                    NBTTagCompound tag = null;
                    if (event.getData().hasKey(ModLibs.MOD_ID))
                        tag = event.getData().getCompoundTag(ModLibs.MOD_ID);

                    boolean flag = false;

                    if (tag != null && tag.getBoolean("AlreadyGened"))
                        flag = true;

                    if (this.completedWork.containsKey(Integer.valueOf(dim))) {
                        ArrayList<ChunkCoord> completed = this.completedWork.get(Integer.valueOf(dim));
                        if (completed != null && !completed.isEmpty()) {
                            if (completed.contains(cCoord))
                                flag = true;
                        }
                    }
                    if (!flag) {
                        LogHelper.info("Adding chunk " + cCoord.toString() + " to retro gen work load.");
                        chunks.add(cCoord);
                        this.pendingWork.put(Integer.valueOf(dim), chunks);
                    }
                }
            }
        }
    }

    public static final class GeneratorInfo {
        public Block block;
        public int meta, numViens, veinSize, minY, maxY;

        public GeneratorInfo(Block blk, int i0, int i1, int i2, int i3, int i4) {
            this.block = blk;
            this.meta = i0;
            this.numViens = i1;
            this.veinSize = i2;
            this.minY = i3;
            this.maxY = i4;
        }

        public GeneratorInfo(Block blk, int i1, int i2, int i3, int i4) {
            this(blk, 0, i1, i2, i3, i4);
        }
    }

    public static final class ChunkCoord implements Comparable<ChunkCoord>, Serializable {
        private static final long serialVersionUID = 1L;
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

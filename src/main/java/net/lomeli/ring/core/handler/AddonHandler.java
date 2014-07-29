package net.lomeli.ring.core.handler;

import WayofTime.alchemicalWizardry.api.harvest.HarvestRegistry;
import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Interface;

import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.item.ModItems;

@Interface(iface = "WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler", modid = "AWWayofTime")
public class AddonHandler implements IHarvestHandler {

    public void registerHarvestHandler() {
        if (Loader.isModLoaded("AWWayofTime"))
            HarvestRegistry.registerHarvestHandler(this);
    }

    @Override
    public boolean harvestAndPlant(World world, int x, int y, int z, Block block, int meta) {
        ItemStack stack;
        if (block == ModBlocks.onionBlock) {
            if (meta == 7) {
                stack = new ItemStack(ModItems.food, 1 + world.rand.nextInt(3), 0);
                if (!world.isRemote)
                    world.spawnEntityInWorld(new EntityItem(world, x, y, z, stack));
                world.setBlockMetadataWithNotify(x, y, z, 0, 2);
                block.onNeighborBlockChange(world, x, y, z, block);
                return true;
            }
        } else if (block == ModBlocks.manaFlower) {
            if (meta == 3) {
                stack = new ItemStack(ModItems.materials, 1 + world.rand.nextInt(2), 5);
                if (!world.isRemote)
                    world.spawnEntityInWorld(new EntityItem(world, x, y, z, stack));
                world.setBlockMetadataWithNotify(x, y, z, 0, 2);
                block.onNeighborBlockChange(world, x, y, z, block);
                return true;
            }
        }
        return false;
    }
}

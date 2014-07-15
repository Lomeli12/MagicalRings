package net.lomeli.ring.magic.spells;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;

import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.lib.ModLibs;

public class Harvest implements ISpell {
    private int tick;

    @Override
    public boolean useOnBlock(World world, EntityPlayer player, IPlayerSession session, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        for (int j = -(1 + boost); j < (2 + boost); j++)
            for (int k = -(1 + boost); k < (2 + boost); k++) {
                if (session.hasEnoughMana(cost())) {
                    Block blk = world.getBlock(x + j, y, z + k);
                    if (blk != null && !world.isAirBlock(x + j, y, z + k)) {
                        BonemealEvent event = new BonemealEvent(player, world, blk, x + j, y, z + k);
                        if (MinecraftForge.EVENT_BUS.post(event))
                            return false;
                        if (blk instanceof IGrowable) {
                            IGrowable grow = (IGrowable) blk;
                            if (grow.func_149851_a(world, x + j, y, z + k, world.isRemote)) {
                                world.spawnParticle("happyVillager", x + j + 0.5, y + 0.5, z + k + 0.5, world.rand.nextDouble() + 2, world.rand.nextDouble() + 2, world.rand.nextDouble() + 2);
                                if (!world.isRemote) {
                                    if (grow.func_149852_a(world, world.rand, x + j, y, z + k)) {
                                        session.adjustMana(-cost(), false);
                                        //blk.updateTick(world, x + j, y, z + k, world.rand);
                                        grow.func_149853_b(world, world.rand, x + j, y, z + k);
                                    }
                                }
                            }
                        }
                    }
                } else
                    break;
            }
        return false;
    }

    @Override
    public void onUse(World world, EntityPlayer player, IPlayerSession session, ItemStack stack, int boost, int cost) {

    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {

    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {

    }

    @Override
    public void applyToMob(EntityPlayer player, IPlayerSession session, Entity target, int boost, int cost) {
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, IPlayerSession session, int par4, boolean par5, int boost, int cost, boolean bool) {
        if (entity != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (++tick >= 140) {
                tick = 0;
                for (int x = -(1 + boost); x < (2 + boost); x++)
                    for (int z = -(1 + boost); z < (2 + boost); z++) {
                        if (session.hasEnoughMana(cost())) {
                            int blkX = (int) player.posX + x, blkY = (int) player.posY, blkZ = (int) player.posZ + z;
                            Block blk = world.getBlock(blkX, blkY, blkZ);
                            if (blk != null && !world.isAirBlock(blkX, blkY, blkZ)) {
                                if (blk instanceof IGrowable) {
                                    if (((IGrowable)blk).func_149851_a(world, blkX, blkY, blkZ, world.isRemote)) {
                                        if (!world.isRemote)
                                            blk.updateTick(world, blkX, blkY, blkZ, world.rand);
                                        session.adjustMana(-cost(), false);
                                    }
                                }
                            }
                        } else
                            break;
                    }
            }
        }
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.HARVEST;
    }

    @Override
    public int cost() {
        return 15;
    }

    @Override
    public String getSpellDescription() {
        return ModLibs.HARVEST + "Desc";
    }
}

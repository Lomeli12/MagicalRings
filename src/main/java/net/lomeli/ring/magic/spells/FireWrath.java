package net.lomeli.ring.magic.spells;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.lib.ModLibs;

public class FireWrath implements ISpell {

    @Override
    public boolean useOnBlock(World world, EntityPlayer player, IPlayerSession session, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        Block bl = world.getBlock(x, y, z);
        if (bl != null && !world.isAirBlock(x, y, z) && !world.isRemote) {
            for (int i = -(boost / 2); i < (boost / 2); i++)
                for (int j = -(boost / 2); j < (boost / 2); j++)
                    for (int l = -(boost / 2); l < (boost / 2); l++) {
                        if (session.hasEnoughMana(cost() + 1)) {
                            Block o = world.getBlock(x + i, y + j, z + l);
                            ItemStack output = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(o, 1, world.getBlockMetadata(x + i, y + j, z + l)));
                            if (output != null) {
                                if (output.getItem() instanceof ItemBlock) {
                                    Block bk = Block.getBlockFromItem(output.getItem());
                                    session.adjustMana(-(cost() + 1), false);
                                    world.setBlock(x + i, y + j, z + l, bk);
                                } else {
                                    output.stackSize = 1;
                                    EntityItem entityItem = new EntityItem(world, x + i + 0.5, y + j + 0.5, +z + l + 0.5, output);
                                    session.adjustMana(-(cost() + 1), false);
                                    world.spawnEntityInWorld(entityItem);
                                    world.setBlockToAir(x + i, y + j, z + l);
                                }
                            }

                        } else
                            break;
                    }
        }
        return false;
    }

    @Override
    public void onUse(World world, EntityPlayer player, IPlayerSession session, ItemStack stack, int boost, int cost) {
        if (session.hasEnoughMana(cost())) {
            Vec3 look = player.getLookVec();
            EntitySmallFireball fireBall = new EntitySmallFireball(world, player, 0, 0, 0);
            fireBall.setSprinting(true);
            fireBall.setPosition(player.posX + look.xCoord * 4.2, player.posY + look.yCoord + (player.getEyeHeight() / 2), player.posZ + look.zCoord * 4.2);
            fireBall.accelerationX = look.xCoord * 0.5;
            fireBall.accelerationY = look.yCoord * 0.5;
            fireBall.accelerationZ = look.zCoord * 0.5;
            if (!world.isRemote)
                world.spawnEntityInWorld(fireBall);
            session.adjustMana(-cost(), false);
        }
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {

    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {

    }

    @Override
    public void applyToMob(EntityPlayer player, IPlayerSession session, Entity target, int boost, int cost) {
        if (target instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) target;
            if (session.hasEnoughMana(cost)) {
                session.adjustMana(-cost, false);
                living.setFire(cost);
            }
        }
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, IPlayerSession session, int par4, boolean par5, int boost, int cost, boolean bool) {
        if (bool && entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            if (!living.isPotionActive(Potion.fireResistance.id)) {
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    if (session.hasEnoughMana(cost)) {
                        session.adjustMana(-cost, false);
                        player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2000 + (100 * boost), 1, true));
                    }
                } else
                    living.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2000, 1, true));
            }
        }
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.FIRE_WRATH;
    }

    @Override
    public int cost() {
        return 35;
    }

    @Override
    public String getSpellDescription() {
        return ModLibs.FIRE_WRATH + "Desc";
    }
}

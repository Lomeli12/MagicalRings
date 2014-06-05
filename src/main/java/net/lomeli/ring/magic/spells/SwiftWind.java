package net.lomeli.ring.magic.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.ISpell;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketAllowFlying;
import net.lomeli.ring.network.PacketHandler;

public class SwiftWind implements ISpell {

    @Override
    public boolean activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        return false;
    }

    @Override
    public void applyToMob(EntityPlayer player, Entity target, int boost, int cost) {
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost, boolean enabled) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!Rings.proxy.tickHandler.flyingPlayerList.contains(entity.getEntityId()))
                PacketHandler.sendToServer(new PacketAllowFlying(player));
        }
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.WING;
    }

    @Override
    public int cost() {
        // TODO Auto-generated method stub
        return 0;
    }

}

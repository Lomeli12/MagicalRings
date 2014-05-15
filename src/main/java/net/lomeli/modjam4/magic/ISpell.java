package net.lomeli.modjam4.magic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ISpell {
    public void activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, int hitX, int hitY, int hitZ);
    
    public String getUnlocalizedName();
}

package net.lomeli.ring.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISpell {
    public boolean activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
    
    public void applyToMob(EntityPlayer player, Entity target);
    
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5);
    
    public String getUnlocalizedName();
    
    public int cost();
    
    public float spellStrength();
    
    public void setSpellStrength(float boost);
    
    public void addBoost(float boost);
}

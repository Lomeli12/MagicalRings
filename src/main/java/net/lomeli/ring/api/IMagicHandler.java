package net.lomeli.ring.api;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Do NOT implement! Use instance in RingAPI to use these methods.
 */
public interface IMagicHandler {
    public void registerSpell(ISpell spell, Object... obj);

    public List<ISpell> getReisteredSpells();

    public ISpell getSpell(ISpell spell);

    public Object[] getSpellRecipe(ISpell spell);
    
    public boolean canPlayerUse(EntityPlayer player, int cost);
    
    public void useMP(EntityPlayer player, int cost);
    
    public void modifyMax(EntityPlayer player, int newMax);
    
    public int getPlayerMP(EntityPlayer player);
    
    public int getPlayerMaxMP(EntityPlayer player);
    
    public boolean canPlayerUseMagic(EntityPlayer player);

    public NBTTagCompound getPlayerTag(EntityPlayer player);
}

package net.lomeli.ring.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISpell {
    /**
     * Runs when the player right clicks the air or a block
     * @param world
     * @param player
     * @param x
     * @param y
     * @param z
     * @param side
     * @param hitX
     * @param hitY
     * @param hitZ
     * @param boost
     * @param cost - base cost plus extra to compensate for the boost. Use cost() if no boosts are to be applied
     * @return
     */
    public boolean activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost);

    /**
     * When the player right-clicks a mob with the ring
     * @param player
     * @param target
     * @param boost
     * @param cost - base cost plus extra to compensate for the boost. Use cost() if no boosts are to be applied
     */
    public void applyToMob(EntityPlayer player, Entity target, int boost, int cost);

    /**
     * Runs when ring is set to active cast mode and is equipped (in inventory with Vanilla, in ring slot with Baubles)
     * @param stack
     * @param world
     * @param entity
     * @param par4
     * @param par5
     * @param boost
     * @param cost - base cost plus extra to compensate for the boost. Use cost() if no boosts are to be applied
     * @param enabled
     */
    public void onUpdateTick(ItemStack stack, World world, Entity entity, int par4, boolean par5, int boost, int cost, boolean enabled);

    public String getUnlocalizedName();

    /**
     * Base cost of the spell
     * @return
     */
    public int cost();
}

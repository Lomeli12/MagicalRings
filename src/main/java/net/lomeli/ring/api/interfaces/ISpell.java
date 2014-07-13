package net.lomeli.ring.api.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISpell {
    /**
     * Runs when the player right clicks on a block
     *
     * @param world
     * @param player
     * @param session - Player session (if entity is instance of EntityPlayer)
     * @param x
     * @param y
     * @param z
     * @param side
     * @param hitX
     * @param hitY
     * @param hitZ
     * @param boost
     * @param cost    - base cost plus extra to compensate for the boost. Use cost() if no boosts are to be applied
     * @return
     */
    public boolean useOnBlock(World world, EntityPlayer player, IPlayerSession session, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost);

    /**
     * Called when the player right clicks
     *
     * @param world
     * @param player
     * @param session - Player session (if entity is instance of EntityPlayer)
     * @param stack
     * @param boost
     * @param cost
     */
    public void onUse(World world, EntityPlayer player, IPlayerSession session, ItemStack stack, int boost, int cost);

    /**
     * Called when the player picks up the ring (vanilla) or when first equipped (Baubles). Do NOT put actual spells here.
     *
     * @param stack
     * @param entity
     * @param session - Player session (if entity is instance of EntityPlayer)
     */
    public void onEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session);

    /**
     * Called when the player drops the ring (vanilla) or when they unequip it (Baubles). Do NOT put actual spells here
     *
     * @param stack
     * @param entity
     * @param session - Player session (if entity is instance of EntityPlayer)
     */
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session);

    /**
     * When the player right-clicks a mob with the ring
     *
     * @param player
     * @param session - Player session (if entity is instance of EntityPlayer)
     * @param target
     * @param boost
     * @param cost    - base cost plus extra to compensate for the boost. Use cost() if no boosts are to be applied
     */
    public void applyToMob(EntityPlayer player, IPlayerSession session, Entity target, int boost, int cost);

    /**
     * Runs when ring is set to active cast mode and is equipped (in inventory with Vanilla, in ring slot with Baubles)
     *
     * @param stack
     * @param world
     * @param entity
     * @param session - Player session (if entity is instance of EntityPlayer)
     * @param par4
     * @param par5
     * @param boost
     * @param cost    - base cost plus extra to compensate for the boost. Use cost() if no boosts are to be applied
     * @param enabled
     */
    public void onUpdateTick(ItemStack stack, World world, Entity entity, IPlayerSession session, int par4, boolean par5, int boost, int cost, boolean enabled);

    /**
     * Unlocalized name of the spell, also the name the spell is registered with.
     */
    public String getUnlocalizedName();

    public String getSpellDescription();

    /**
     * Base cost of the spell
     *
     * @return
     */
    public int cost();
}

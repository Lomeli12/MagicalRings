package net.lomeli.modjam4.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class DebugSpell implements ISpell{
    
    private float strength;
    
    public DebugSpell() {
        this.strength = 1.0f;
    }

    @Override
    public boolean activateSpell(World world, EntityPlayer player, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 10, 1000));
        return false;
    }

    @Override
    public String getUnlocalizedName() {
        return "debug";
    }

    @Override
    public int cost() {
        return 0;
    }

    @Override
    public void applyToMob(EntityPlayer player, Entity target) {
        target.motionY += 0.5f;
    }

    @Override
    public float spellStrength() {
        return 1.0f;
    }

    @Override
    public void setSpellStrength(float boost) {
        this.strength = boost;
    }

    @Override
    public void addBoost(float boost) {
        this.setSpellStrength(this.spellStrength() + boost);
    }

}

package net.lomeli.ring.item;

public class ItemHammer extends ItemRings{

    public ItemHammer(String texture, int damage) {
        super(texture);
        this.setMaxDamage(damage);
        this.setMaxStackSize(1);
    }

}

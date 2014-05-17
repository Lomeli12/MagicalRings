package net.lomeli.ring.item;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSpellBook extends ItemRings {

    public ItemSpellBook(String texture) {
        super(texture);
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!player.getEntityData().hasKey(ModLibs.PLAYER_DATA))
            MagicHandler.modifyPlayerMaxMP(player, ModLibs.BASE_MP);
        player.openGui(Rings.instance, ModLibs.BOOK_GUI, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        return super.onItemRightClick(stack, world, player);
    }

}

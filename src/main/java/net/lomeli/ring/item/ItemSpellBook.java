package net.lomeli.ring.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketCheckPlayerMP;
import net.lomeli.ring.network.PacketHandler;

public class ItemSpellBook extends ItemRings {

    public ItemSpellBook(String texture) {
        super(texture);
        this.setMaxStackSize(1);
    }
    
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!player.isSneaking()) {
            PacketHandler.sendToServer(new PacketCheckPlayerMP(player));
            player.openGui(Rings.instance, ModLibs.BOOK_GUI, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        PacketHandler.sendToServer(new PacketCheckPlayerMP(player));
        player.openGui(Rings.instance, ModLibs.BOOK_GUI, world, x, y, z);
        return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

}

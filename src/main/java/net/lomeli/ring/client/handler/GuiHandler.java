package net.lomeli.ring.client.handler;

import net.lomeli.ring.block.IBookEntry;
import net.lomeli.ring.block.tile.TileRingForge;
import net.lomeli.ring.client.gui.GuiRingForge;
import net.lomeli.ring.client.gui.GuiSpellBook;
import net.lomeli.ring.inventory.ContainerRingForge;
import net.lomeli.ring.lib.ModLibs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (ID == ModLibs.RING_FORGE_GUI) {
            if (tile instanceof TileRingForge)
                return new ContainerRingForge((TileRingForge)tile, player.inventory, world, x, y, z);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (ID == ModLibs.RING_FORGE_GUI) {
            if (tile instanceof TileRingForge)
                return new GuiRingForge((TileRingForge)tile, player.inventory, world, x, y, z);
        } else if (ID == ModLibs.BOOK_GUI) {
            Block bl = world.getBlock(x, y, z);
            if (bl != null && bl instanceof IBookEntry)
                return new GuiSpellBook(((IBookEntry)bl).getPage(world.getBlockMetadata(x, y, z)));
            return new GuiSpellBook();
        }
        return null;
    }

}

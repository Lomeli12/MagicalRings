package net.lomeli.ring.client.handler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.Page;
import net.lomeli.ring.api.interfaces.IBookEntry;
import net.lomeli.ring.block.tile.TileRingForge;
import net.lomeli.ring.client.gui.GuiRingForge;
import net.lomeli.ring.client.gui.GuiSpellBook;
import net.lomeli.ring.client.page.PageUtil;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.inventory.ContainerRingForge;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketSavePage;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (ID == ModLibs.RING_FORGE_GUI) {
            if (tile instanceof TileRingForge)
                return new ContainerRingForge((TileRingForge) tile, player.inventory, world, x, y, z);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Minecraft mc = Minecraft.getMinecraft();
        TileEntity tile = world.getTileEntity(x, y, z);
        if (ID == ModLibs.RING_FORGE_GUI) {
            if (tile instanceof TileRingForge)
                return new GuiRingForge((TileRingForge) tile, player.inventory, world, x, y, z);
        } else if (ID == ModLibs.BOOK_GUI) {
            ItemStack stack = player.getCurrentEquippedItem();
            if (stack != null) {
                GuiSpellBook bookGui = new GuiSpellBook();
                int pageNum = 0;
                Block bl = world.getBlock(x, y, z);
                Entity entity = SimpleUtil.getEntityPointedAt(world, player, 0.5d, 10d, true);
                if (stack.hasTagCompound())
                    pageNum = stack.getTagCompound().getInteger("LastSavedPage");
                if (stack.getItemDamage() == 0) {
                    PageUtil.loadBaseBook(bookGui);
                    bookGui.setGuiTexture(new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":gui/book.png"));
                    if (bl != null && bl instanceof IBookEntry && player.isSneaking())
                        pageNum = getPageByID(((IBookEntry) bl).getBookPage(world.getBlockMetadata(x, y, z)), bookGui);
                    else if (entity != null) {
                        if (entity instanceof EntityItem) {
                            ItemStack item = ((EntityItem) entity).getEntityItem();
                            if (item != null && item.getItem() != null && player.isSneaking()) {
                                if (item.getItem() instanceof ItemBlock) {
                                    bl = Block.getBlockFromItem(item.getItem());
                                    if (bl != null && bl instanceof IBookEntry) {
                                        String key = ((IBookEntry) bl).getBookPage(item.getItemDamage());
                                        if (key != null)
                                            pageNum = getPageByID(key, bookGui);
                                    }
                                } else if (item.getItem() instanceof IBookEntry) {
                                    String key = ((IBookEntry) item.getItem()).getBookPage(item.getItemDamage());
                                    if (key != null)
                                    pageNum = getPageByID(key, bookGui);
                                }
                            }
                        }
                    }
                    if (pageNum > 0 && pageNum < bookGui.avaliablePages.size()) {
                        Rings.pktHandler.sendToServer(new PacketSavePage(player, pageNum));
                        bookGui.setPageNumber(pageNum);
                    }
                    return bookGui;
                } else if (stack.getItemDamage() == 1) {
                    PageUtil.loadMaterialBook(bookGui);
                    bookGui.setGuiTexture(new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":gui/material.png"));
                    if (pageNum > 0 && pageNum < bookGui.avaliablePages.size()) {
                        Rings.pktHandler.sendToServer(new PacketSavePage(player, pageNum));
                        bookGui.setPageNumber(pageNum);
                    }
                    return bookGui;
                }
            }
        }
        return null;
    }

    private int getPageByID(String id, GuiSpellBook gui) {
        for (int i = 0; i < gui.avaliablePages.size(); i++) {
            Page page = gui.avaliablePages.get(i);
            if (page != null && page.pageID() != null) {
                if (page.pageID().equals(id))
                    return i;
            }
        }
        return 0;
    }

}

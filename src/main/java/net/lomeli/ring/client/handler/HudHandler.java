package net.lomeli.ring.client.handler;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.ring.api.interfaces.IBookEntry;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.BookText;

/**
 * Most of this is based off of Botania, with some modification to meet my needs. Thanks Vazkii!
 */
public class HudHandler {

    private static final RenderItem itemRender = new RenderItem();

    @SubscribeEvent
    public void onDrawScreen(RenderGameOverlayEvent.Post event) {
        if (event.type == ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.inGameHasFocus) {
                MovingObjectPosition pos = mc.thePlayer.rayTrace(5, event.partialTicks);
                ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
                if (pos != null && stack != null) {
                    if (stack.getItem() == ModItems.book && stack.getItemDamage() == 0) {
                        if (pos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                            if (!mc.theWorld.isAirBlock(pos.blockX, pos.blockY, pos.blockZ)) {
                                Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
                                if (block != null && block instanceof IBookEntry) {
                                    int meta = mc.theWorld.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);
                                    String key = ((IBookEntry) block).getBookPage(meta);
                                    if (key != null) {
                                        renderInfoDisplay(new ItemStack(block, 1, meta), event.resolution);
                                        return;
                                    }
                                }
                            }
                        }
                        Entity entity = SimpleUtil.getEntityPointedAt(mc.thePlayer.getEntityWorld(), mc.thePlayer, 0.5d, 10d, true);
                        if (entity != null) {
                            if (entity instanceof EntityItem) {
                                ItemStack item = ((EntityItem) entity).getEntityItem();
                                if (item != null && item.getItem() != null) {
                                    if (item.getItem() instanceof ItemBlock) {
                                        Block bl = Block.getBlockFromItem(item.getItem());
                                        if (bl != null && bl instanceof IBookEntry) {
                                            String key = ((IBookEntry) bl).getBookPage(item.getItemDamage());
                                            if (key != null) {
                                                renderInfoDisplay(item, event.resolution);
                                                return;
                                            }
                                        }
                                    } else if (item.getItem() instanceof IBookEntry) {
                                        String key = ((IBookEntry) item.getItem()).getBookPage(item.getItemDamage());
                                        if (key != null) {
                                            renderInfoDisplay(item, event.resolution);
                                            return;
                                        }
                                    }
                                }
                            } else {
                                if (entity instanceof IBookEntry) {
                                    renderInto(entity.getCommandSenderName(), event.resolution);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void renderInfoDisplay(ItemStack stack, ScaledResolution res) {
        renderInto(stack.getDisplayName(), res);
    }

    private void renderInto(String info, ScaledResolution res) {
        int color = 0x8000C4C4;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Minecraft mc = Minecraft.getMinecraft();

        int x = res.getScaledWidth() / 2;
        int y = res.getScaledHeight() / 2;

        mc.fontRenderer.drawStringWithShadow(info, x + 20, y + 6, color);
        mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(BookText.INFO), x + 20, y + 18, color);

        itemRender.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModItems.book, 1, 0), x + 4, y + 2);

        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }
}

package net.lomeli.ring.client.handler;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import net.lomeli.ring.api.IBookEntry;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.BookText;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HudHandler {

    private static final RenderItem itemRender = new RenderItem();

    @SubscribeEvent
    public void onDrawScreen(RenderGameOverlayEvent.Post event) {
        if (event.type == ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.inGameHasFocus) {
                MovingObjectPosition pos = mc.objectMouseOver;
                if (pos != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() == ModItems.book) {
                    Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
                    int meta = mc.theWorld.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);
                    if (block != null && block instanceof IBookEntry)
                        renderInfoDisplay(new ItemStack(block, 1, meta), event.resolution);
                }
            }
        }
    }

    private void renderInfoDisplay(ItemStack stack, ScaledResolution res) {
        int color = 0x8000C4C4;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Minecraft mc = Minecraft.getMinecraft();

        int x = res.getScaledWidth() / 2 + 8;
        int y = res.getScaledHeight() / 2 - 4;

        mc.fontRenderer.drawStringWithShadow(stack.getDisplayName(), x + 18, y, color);
        mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(BookText.INFO), x + 18, y + 11, color);
        itemRender.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModItems.book), x, y);

        GL11.glDisable(GL11.GL_LIGHTING);

        mc.fontRenderer.drawStringWithShadow("?", x + 10, y + 8, 0xFFFFFF);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }
}

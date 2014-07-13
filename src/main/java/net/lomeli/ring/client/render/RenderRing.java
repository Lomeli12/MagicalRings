package net.lomeli.ring.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import net.minecraftforge.client.IItemRenderer;

public class RenderRing implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        Tessellator tess = Tessellator.instance;
        for (int i = 0; i < 3; i++) {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            IIcon icon = item.getItem().getIcon(item, i);
            int rgb = item.getItem().getColorFromItemStack(item, i);
            float r = (rgb >> 16 & 255) / 255.0F;
            float g = (rgb >> 8 & 255) / 255.0F;
            float b = (rgb & 255) / 255.0F;

            if (icon == null)
                continue;

            float uv1 = icon.getMinU();
            float uv2 = icon.getMaxU();
            float uv3 = icon.getMinV();
            float uv4 = icon.getMaxV();

            GL11.glColor4f(r, g, b, 1f);

            ItemRenderer.renderItemIn2D(tess, uv2, uv3, uv1, uv4, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);

            GL11.glColor4f(1f, 1f, 1f, 1f);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }
}

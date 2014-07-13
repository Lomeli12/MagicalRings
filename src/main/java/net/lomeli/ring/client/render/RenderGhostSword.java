package net.lomeli.ring.client.render;

import org.lwjgl.opengl.GL11;

import java.util.Random;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import net.minecraftforge.client.IItemRenderer;

public class RenderGhostSword implements IItemRenderer {
    RenderItem renderItem = new RenderItem();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.EQUIPPED || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper == ItemRendererHelper.ENTITY_BOBBING;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.EQUIPPED)
            render3D(item);
        else if (type == ItemRenderType.ENTITY) {
            Random rand = new Random(187L);
            byte iterations = 1;
            if (item.stackSize > 1)
                iterations = 2;
            if (item.stackSize > 15)
                iterations = 3;
            if (item.stackSize > 31)
                iterations = 4;
            if (RenderManager.instance.options.fancyGraphics) {
                EntityItem entity = (EntityItem) data[1];
                GL11.glPushMatrix();

                float offsetZ = 0.0625F + 0.021875F;

                GL11.glRotatef(((entity.age + 1.0F) / 20.0F + entity.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-0.5F, -0.25F, -(offsetZ * iterations / 2.0F));

                for (int count = 0; count < iterations; ++count) {
                    if (count > 0) {
                        float offsetX = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                        float offsetY = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                        float z = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                        GL11.glTranslatef(offsetX, offsetY, offsetZ);
                    } else
                        GL11.glTranslatef(0f, 0f, offsetZ);
                    render3D(item);
                }
                GL11.glPopMatrix();
            } else {
                GL11.glPushMatrix();


                for (int ii = 0; ii < iterations; ++ii) {
                    GL11.glPushMatrix();
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(180 - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);

                    if (ii > 0) {
                        float var12 = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        float var13 = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        float var14 = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        GL11.glTranslatef(var12, var13, var14);
                    }

                    GL11.glTranslatef(0.5f, 0.8f, 0);
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glScalef(1f / 16f, 1f / 16f, 1);

                    render(ItemRenderType.ENTITY, item);
                    GL11.glPopMatrix();
                }
                GL11.glPopMatrix();
            }
        }
    }

    private void render(ItemRenderType type, ItemStack stack) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        IIcon icon = stack.getItem().getIcon(stack, 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1f, 1f, 1f, 0.5f);
        renderItem.renderIcon(0, 0, icon, 16, 16);

        if (type == ItemRenderType.ENTITY)
            GL11.glTranslatef(0, 0, -0.01f);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public void render3D(ItemStack item) {
        GL11.glPushMatrix();
        IIcon icon = item.getItem().getIcon(item, 0);
        if (icon == null)
            return;
        Tessellator tess = Tessellator.instance;
        float uv1 = icon.getMinU();
        float uv2 = icon.getMaxU();
        float uv3 = icon.getMinV();
        float uv4 = icon.getMaxV();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1f, 1f, 1f, 0.5f);
        ItemRenderer.renderItemIn2D(tess, uv2, uv3, uv1, uv4, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}

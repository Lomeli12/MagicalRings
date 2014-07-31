package net.lomeli.ring.client.render;

import java.util.Random;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import net.minecraftforge.client.IItemRenderer;

import static org.lwjgl.opengl.GL11.*;

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
                glPushMatrix();

                float offsetZ = 0.0625F + 0.021875F;

                glRotatef(((entity.age + 1.0F) / 20.0F + entity.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                glTranslatef(-0.5F, -0.25F, -(offsetZ * iterations / 2.0F));

                for (int count = 0; count < iterations; ++count) {
                    if (count > 0) {
                        float offsetX = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                        float offsetY = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                        glTranslatef(offsetX, offsetY, offsetZ);
                    } else
                        glTranslatef(0f, 0f, offsetZ);
                    render3D(item);
                }
                glPopMatrix();
            } else {
                glPushMatrix();


                for (int ii = 0; ii < iterations; ++ii) {
                    glPushMatrix();
                    glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                    glRotatef(180 - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);

                    if (ii > 0) {
                        float var12 = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        float var13 = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        float var14 = (rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        glTranslatef(var12, var13, var14);
                    }

                    glTranslatef(0.5f, 0.8f, 0);
                    glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    glScalef(1f / 16f, 1f / 16f, 1);

                    render(ItemRenderType.ENTITY, item);
                    glPopMatrix();
                }
                glPopMatrix();
            }
        }
    }

    private void render(ItemRenderType type, ItemStack stack) {
        glPushMatrix();
        glDisable(GL_LIGHTING);
        glEnable(GL_ALPHA_TEST);
        IIcon icon = stack.getItem().getIcon(stack, 0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(1f, 1f, 1f, 0.5f);
        renderItem.renderIcon(0, 0, icon, icon.getIconWidth(), icon.getIconHeight());

        if (type == ItemRenderType.ENTITY)
            glTranslatef(0, 0, -0.01f);
        glColor4f(1f, 1f, 1f, 1f);
        glDisable(GL_BLEND);
        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    public void render3D(ItemStack item) {
        glPushMatrix();
        IIcon icon = item.getItem().getIcon(item, 0);
        if (icon == null)
            return;
        Tessellator tess = Tessellator.instance;
        float uv1 = icon.getMinU();
        float uv2 = icon.getMaxU();
        float uv3 = icon.getMinV();
        float uv4 = icon.getMaxV();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(1f, 1f, 1f, 0.5f);
        ItemRenderer.renderItemIn2D(tess, uv2, uv3, uv1, uv4, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
        glColor4f(1f, 1f, 1f, 1f);
        glDisable(GL_BLEND);
        glPopMatrix();
    }
}

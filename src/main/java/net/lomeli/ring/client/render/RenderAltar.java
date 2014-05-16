package net.lomeli.ring.client.render;

import java.awt.Color;

import net.lomeli.ring.block.tile.TileAltar;
import net.lomeli.ring.block.tile.TileItemAltar;
import net.lomeli.ring.client.model.ModelAltar;
import net.lomeli.ring.lib.ModLibs;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

public class RenderAltar extends TileEntitySpecialRenderer {

    private ModelAltar model = new ModelAltar();
    private RenderBlocks renderBlock = new RenderBlocks();
    private Minecraft mc = Minecraft.getMinecraft();
    private final float size = 0.0625f;
    private float angle = 0;

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        if (var1 instanceof TileAltar)
            renderAltar((TileAltar) var1, var2, var4, var6);
        else if (var1 instanceof TileItemAltar)
            renderItemAltar((TileItemAltar) var1, var2, var4, var6);
    }

    public void renderAltar(TileAltar tile, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.85f, (float) z + 0.5f);
        GL11.glRotatef(180f, 1f, 0f, 0f);
        GL11.glScaled(1.25, 1.25, 1.25);

        mc.renderEngine.bindTexture(new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":models/Altar.png"));
        model.render(size);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.7f, (float) z + 0.5f);

        if (angle < 360)
            angle += 0.4f;
        if (angle >= 360)
            angle = 0;
        if (tile.getStackInSlot(0) != null) {
            ItemStack stack = tile.getStackInSlot(0);

            mc.renderEngine.bindTexture(stack.getItem() instanceof ItemBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
            if (!ForgeHooksClient.renderEntityItem(new EntityItem(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, stack), stack, 0F, 0F, tile.getWorldObj().rand, mc.renderEngine, renderBlock, 1)) {
                GL11.glRotatef(angle, 0, 1f, 0);
                if (stack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType())) {
                    GL11.glScalef(0.25f, 0.25f, 0.25f);
                    GL11.glTranslatef(0f, 0.9f, 0f);
                    renderBlock.renderBlockAsItem(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage(), 1f);
                    GL11.glTranslatef(0f, -0.9f, 0f);
                    GL11.glScalef(1f, 1f, 1f);
                }else {
                    GL11.glScalef(0.4f, 0.4f, 0.4f);
                    GL11.glTranslatef(-0.5f, 0f, 0);
                    for (int renderPass = 0; renderPass < stack.getItem().getRenderPasses(stack.getItemDamage()); renderPass++) {
                        IIcon icon = stack.getItem().getIcon(stack, renderPass);
                        if (icon != null) {
                            int rgb = stack.getItem().getColorFromItemStack(stack, renderPass);
                            float r = (rgb >> 16 & 255) / 255f;
                            float g = (rgb >> 8 & 255) / 255f;
                            float b = (rgb & 255) / 255f;
                            GL11.glColor3f(r, g, b);
                            float f = icon.getMinU();
                            float f1 = icon.getMaxU();
                            float f2 = icon.getMinV();
                            float f3 = icon.getMaxV();
                            ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1f / 16f);
                        }
                    }
                    GL11.glTranslatef(0.5f, 0, 0);
                    GL11.glColor3f(1f, 1f, 1f);
                }
            }
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

    }

    public void renderItemAltar(TileItemAltar tile, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.85f, (float) z + 0.5f);
        GL11.glRotatef(180f, 1f, 0f, 0f);
        GL11.glScaled(1.25, 1.25, 1.25);

        mc.renderEngine.bindTexture(new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":models/Altar.png"));
        model.render(size);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.7f, (float) z + 0.5f);

        if (angle < 360)
            angle += 0.4f;
        if (angle >= 360)
            angle = 0;
        if (tile.getStackInSlot(0) != null) {
            ItemStack stack = tile.getStackInSlot(0);

            mc.renderEngine.bindTexture(stack.getItem() instanceof ItemBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
            if (!ForgeHooksClient.renderEntityItem(new EntityItem(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, stack), stack, 0F, 0F, tile.getWorldObj().rand, mc.renderEngine, renderBlock, 1)) {
                GL11.glRotatef(angle, 0, 1f, 0);
                if (stack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType())) {
                    GL11.glScalef(0.25f, 0.25f, 0.25f);
                    GL11.glTranslatef(0f, 0.9f, 0f);
                    renderBlock.renderBlockAsItem(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage(), 1f);
                    GL11.glTranslatef(0f, -0.9f, 0f);
                    GL11.glScalef(1f, 1f, 1f);
                }else {
                    GL11.glScalef(0.4f, 0.4f, 0.4f);
                    GL11.glTranslatef(-0.5f, 0f, 0);
                    for (int renderPass = 0; renderPass < stack.getItem().getRenderPasses(stack.getItemDamage()); renderPass++) {
                        IIcon icon = stack.getItem().getIcon(stack, renderPass);
                        if (icon != null) {
                            int rgb = stack.getItem().getColorFromItemStack(stack, renderPass);
                            float r = (rgb >> 16 & 255) / 255f;
                            float g = (rgb >> 8 & 255) / 255f;
                            float b = (rgb & 255) / 255f;
                            GL11.glColor3f(r, g, b);
                            float f = icon.getMinU();
                            float f1 = icon.getMaxU();
                            float f2 = icon.getMinV();
                            float f3 = icon.getMaxV();
                            ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1f / 16f);
                        }
                    }
                    GL11.glTranslatef(0.5f, 0, 0);
                    GL11.glColor3f(1f, 1f, 1f);
                }
            }
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

}

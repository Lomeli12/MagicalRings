package net.lomeli.ring.client.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.ForgeHooksClient;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Render3DItem extends Render {

    private Random random = new Random();
    private RenderBlocks renderBlocksRi = new RenderBlocks();
    public boolean renderWithColor = true;
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private void renderDroppedItem(EntityItem par1EntityItem, IIcon par2Icon, int par3, float par4, float par5, float par6, float par7, int pass) {
        Tessellator tessellator = Tessellator.instance;

        if (par2Icon == null) {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = texturemanager.getResourceLocation(par1EntityItem.getEntityItem().getItemSpriteNumber());
            par2Icon = ((TextureMap) texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        float f14 = par2Icon.getMinU();
        float f15 = par2Icon.getMaxU();
        float f4 = par2Icon.getMinV();
        float f5 = par2Icon.getMaxV();
        float f7 = 0.5F;
        float f8 = 0.25F;
        float f10;

        GL11.glPushMatrix();

        GL11.glRotatef(((par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);

        float f9 = 0.0625F;
        f10 = 0.021875F;
        ItemStack itemstack = par1EntityItem.getEntityItem();
        int j = itemstack.stackSize;
        byte b0;

        if (j < 2)
            b0 = 1;
        else if (j < 16)
            b0 = 2;
        else if (j < 32)
            b0 = 3;
        else
            b0 = 4;

        b0 = getMiniItemCount(itemstack, b0);

        GL11.glTranslatef(-f7, -f8, -((f9 + f10) * b0 / 2.0F));

        for (int k = 0; k < b0; ++k) {
            // Makes items offset when in 3D, like when in 2D, looks much
            // better. Considered a vanilla bug...
            GL11.glTranslatef(0f, 0f, f9 + f10);

            if (itemstack.getItemSpriteNumber() == 0)
                this.bindTexture(TextureMap.locationBlocksTexture);
            else
                this.bindTexture(TextureMap.locationItemsTexture);

            GL11.glColor4f(par5, par6, par7, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, f15, f4, f14, f5, par2Icon.getIconWidth(), par2Icon.getIconHeight(), f9);

            if (itemstack.hasEffect(pass)) {
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                this.renderManager.renderEngine.bindTexture(RES_ITEM_GLINT);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                float f11 = 0.76F;
                GL11.glColor4f(0.5F * f11, 0.25F * f11, 0.8F * f11, 1.0F);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glPushMatrix();
                float f12 = 0.125F;
                GL11.glScalef(f12, f12, f12);
                float f13 = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
                GL11.glTranslatef(f13, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(f12, f12, f12);
                f13 = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
                GL11.glTranslatef(-f13, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                GL11.glPopMatrix();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }
        }

        GL11.glPopMatrix();
    }

    private void renderDroppedItem(EntityItem par1EntityItem, IIcon par2Icon, int par3, float par4, float par5, float par6, float par7) {
        this.renderDroppedItem(par1EntityItem, par2Icon, par3, par4, par5, par6, par7, 0);
    }

    @Override
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
        this.doRender((EntityItem) var1, var2, var4, var6, var8, var9);
    }

    public void doRender(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
        ItemStack itemstack = par1EntityItem.getEntityItem();

        if (itemstack.getItem() != null) {
            this.bindEntityTexture(par1EntityItem);
            this.random.setSeed(187L);
            GL11.glPushMatrix();
            float f2 = 0F;
            float f3 = ((par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverStart) * (180F / (float) Math.PI);
            byte b0 = 1;

            if (par1EntityItem.getEntityItem().stackSize > 1)
                b0 = 2;

            if (par1EntityItem.getEntityItem().stackSize > 5)
                b0 = 3;

            if (par1EntityItem.getEntityItem().stackSize > 20)
                b0 = 4;

            if (par1EntityItem.getEntityItem().stackSize > 40)
                b0 = 5;

            b0 = getMiniBlockCount(itemstack, b0);

            GL11.glTranslatef((float) par2, (float) par4 + f2, (float) par6);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float f6;
            float f7;
            int k;

            if (ForgeHooksClient.renderEntityItem(par1EntityItem, itemstack, f2, f3, random, renderManager.renderEngine, field_147909_c, b0)) {
                ;
            }else // Code Style break here to prevent the patch from editing
                  // this line
            if (itemstack.getItemSpriteNumber() == 0 && itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
                Block block = Block.getBlockFromItem(itemstack.getItem());
                GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);

                float f9 = 0.25F;
                k = block.getRenderType();

                if (k == 1 || k == 19 || k == 12 || k == 2)
                    f9 = 0.5F;

                if (block.getRenderBlockPass() > 0) {
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    GL11.glEnable(GL11.GL_BLEND);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                }

                GL11.glScalef(f9, f9, f9);

                for (int l = 0; l < b0; ++l) {
                    GL11.glPushMatrix();

                    if (l > 0) {
                        f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
                        f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
                        float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
                        GL11.glTranslatef(f6, f7, f8);
                    }

                    this.renderBlocksRi.renderBlockAsItem(block, itemstack.getItemDamage(), 1.0F);
                    GL11.glPopMatrix();
                }

                if (block.getRenderBlockPass() > 0)
                    GL11.glDisable(GL11.GL_BLEND);
            }else {
                float f5;

                if (/* itemstack.getItemSpriteNumber() == 1 && */itemstack.getItem().requiresMultipleRenderPasses()) {
                    GL11.glScalef(0.5F, 0.5F, 0.5F);

                    for (int j = 0; j < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++j) {
                        this.random.setSeed(187L);
                        IIcon iicon1 = itemstack.getItem().getIcon(itemstack, j);

                        if (this.renderWithColor) {
                            k = itemstack.getItem().getColorFromItemStack(itemstack, j);
                            f5 = (k >> 16 & 255) / 255.0F;
                            f6 = (k >> 8 & 255) / 255.0F;
                            f7 = (k & 255) / 255.0F;
                            GL11.glColor4f(f5, f6, f7, 1.0F);
                            this.renderDroppedItem(par1EntityItem, iicon1, b0, par9, f5, f6, f7, j);
                        }else
                            this.renderDroppedItem(par1EntityItem, iicon1, b0, par9, 1.0F, 1.0F, 1.0F, j);
                    }
                }else {
                    if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
                        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                        GL11.glEnable(GL11.GL_BLEND);
                        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    }

                    GL11.glScalef(0.5F, 0.5F, 0.5F);

                    IIcon iicon = itemstack.getIconIndex();

                    if (this.renderWithColor) {
                        int i = itemstack.getItem().getColorFromItemStack(itemstack, 0);
                        float f4 = (i >> 16 & 255) / 255.0F;
                        f5 = (i >> 8 & 255) / 255.0F;
                        f6 = (i & 255) / 255.0F;
                        this.renderDroppedItem(par1EntityItem, iicon, b0, par9, f4, f5, f6);
                    }else
                        this.renderDroppedItem(par1EntityItem, iicon, b0, par9, 1.0F, 1.0F, 1.0F);

                    if (itemstack != null && itemstack.getItem() instanceof ItemCloth)
                        GL11.glDisable(GL11.GL_BLEND);
                }
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    public byte getMiniItemCount(ItemStack stack, byte original) {
        return original;
    }

    public byte getMiniBlockCount(ItemStack stack, byte original) {
        return original;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return this.getEntityTexture((EntityItem) entity);
    }

    protected ResourceLocation getEntityTexture(EntityItem item) {
        return this.renderManager.renderEngine.getResourceLocation(item.getEntityItem().getItemSpriteNumber());
    }
}

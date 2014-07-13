package net.lomeli.ring.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.api.Page;
import net.lomeli.ring.core.SimpleUtil;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

public class RenderSpellParchment implements IItemRenderer {
    protected static RenderItem itemRenderer = new RenderItem();
    private static Minecraft mc = Minecraft.getMinecraft();
    private FontRenderer fontRenderer;

    public Timer getTimer(Minecraft mc) {
        try {
            return ((Timer) SimpleUtil.getObject(Minecraft.class, mc, "timer", "Q", "field_71428_T"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Timer(20.0F);
    }

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
        EntityPlayer player = mc.thePlayer;
        float renderTick = getTimer(mc).renderPartialTicks;
        float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * renderTick;
        float f1 = 0.8f;

        float pep = (Float) SimpleUtil.getObject(ItemRenderer.class, mc.entityRenderer.itemRenderer, "prevEquippedProgress", "g", "field_78451_d");
        float ep = (Float) SimpleUtil.getObject(ItemRenderer.class, mc.entityRenderer.itemRenderer, "equippedProgress", "f", "field_78454_c");
        float f2 = pep + (ep - pep) * renderTick;
        if (player.isSneaking()) {
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(2.5f, 2.5f, 2.5f);

            float pi = 3.141593f;

            float f4 = player.getSwingProgress(renderTick);
            float f5 = MathHelper.sin(f4 * f4 * pi);
            float f6 = MathHelper.sin(MathHelper.sqrt_float(f4) * pi);

            GL11.glRotatef(-f6 * 80f, -1f, 0f, 0f);
            GL11.glRotatef(-f6 * 20f, 0f, 0f, -1f);
            GL11.glRotatef(-f5 * 20f, 0f, -1f, 0f);
            GL11.glRotatef(45f, 0f, -1f, 0f);
            GL11.glTranslatef(-0.7f * f1, -(-0.65f * f1 - (1.0f - f1) * 0.6f), 0.9f * f1);

            GL11.glPushMatrix();

            int i0 = 65536;
            int x = MathHelper.floor_double(player.posX), y = MathHelper.floor_double(player.posY), z = MathHelper.floor_double(player.posZ);
            float f7 = 1f;
            int i = mc.theWorld.getLightBrightnessForSkyBlocks(x, y, z, 0);
            int i1 = i % i0;
            int i2 = i / i0;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1 / f7, i2 / f7);
            GL11.glColor4f(1f, 1f, 1f, 1f);

            i = item.getItem().getColorFromItemStack(item, 0);
            f4 = (i >> 16 & 0xFF) / 255f;
            f5 = (i >> 8 & 0xFF) / 255f;
            f6 = (i & 0xFF) / 255f;
            GL11.glColor4f(f7 * f4, f1 * f5, f1 * f6, 1f);

            f5 = MathHelper.sin(f4 * pi);
            f6 = MathHelper.sin(MathHelper.sqrt_float(f4) * pi);
            GL11.glTranslatef(-f6 * 0.4f, MathHelper.sin(MathHelper.sqrt_float(f4) * pi * 2f) * 0.2f, -f5);

            f4 = f7 - f / 45f + 0.1F;
            if (f4 < 0f)
                f4 = 0f;
            if (f4 > 1f)
                f4 = 1f;
            f4 = -MathHelper.cos(f4 * pi) * 0.5f + 0.5f;

            GL11.glTranslatef(0f, 0f * f1 - (1f - f2) * 1.2f - f4 * 0.5f + 0.04f, -0.9f * f1);
            GL11.glRotatef(90f, 0f, 1f, 0f);
            GL11.glRotatef(f4 * -85f, 0f, 0f, 1f);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);

            mc.renderEngine.bindTexture(mc.thePlayer.getLocationSkin());

            for (i2 = 0; i2 < 2; i2++) {
                int i3 = i2 * 2 - 1;
                GL11.glPushMatrix();
                GL11.glTranslatef(-0f, -0.6f, 1.1f * i3);
                GL11.glRotatef(-45 * i3, 1f, 0f, 0f);
                GL11.glRotatef(-90f, 0f, 0f, 1f);
                GL11.glRotatef(59f, 0f, 0f, 1f);
                GL11.glRotatef(-65 * i3, 0f, 1f, 0f);
                Render render = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
                RenderPlayer renderplayer = (RenderPlayer) render;
                GL11.glScalef(1f, 1f, 1f);
                renderplayer.renderFirstPersonArm(mc.thePlayer);
                GL11.glPopMatrix();
            }

            f5 = player.getSwingProgress(renderTick);
            f6 = MathHelper.sin(f5 * f5 * pi);
            float f8 = MathHelper.sin(MathHelper.sqrt_float(f5) * pi);

            GL11.glRotatef(-f6 * 20f, 0f, 1f, 0f);
            GL11.glRotatef(-f8 * 20f, 0f, 0f, 1f);
            GL11.glRotatef(-f8 * 80f, 1f, 0f, 0f);

            float f9 = 0.38f;

            GL11.glScalef(f9, f9, f9);
            GL11.glRotatef(90f, 0f, 1f, 0f);
            GL11.glRotatef(180f, 0f, 0f, 1f);
            GL11.glTranslatef(-1f, -1f, 0f);

            float f10 = 0.015625f;

            GL11.glScalef(f10, f10, f10);

            renderSpellParchment(item, mc.thePlayer);

            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            IIcon icon = item.getItem().getIcon(item, 0);
            if (icon == null)
                return;
            Tessellator tess = Tessellator.instance;
            float uv1 = icon.getMinU();
            float uv2 = icon.getMaxU();
            float uv3 = icon.getMinV();
            float uv4 = icon.getMaxV();
            ItemRenderer.renderItemIn2D(tess, uv2, uv3, uv1, uv4, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
            GL11.glPopMatrix();
        }
    }

    public void renderSpellParchment(ItemStack item, EntityPlayer player) {
        this.fontRenderer = mc.fontRenderer;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        mc.renderEngine.bindTexture(new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":gui/parchment.png"));
        Tessellator tess = Tessellator.instance;
        GL11.glNormal3f(0f, 0f, -1f);

        tess.startDrawingQuads();
        byte b0 = 7;
        tess.addVertexWithUV((double) (0 - b0), (double) (128 + b0), 0d, 0d, 1d);
        tess.addVertexWithUV((double) (128 + b0), (double) (128 + b0), 0d, 1d, 1d);
        tess.addVertexWithUV((double) (128 + b0), (double) (0 - b0), 0d, 1d, 0d);
        tess.addVertexWithUV((double) (0 - b0), (double) (0 - b0), 0d, 0d, 0d);
        tess.draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        ISpell spell = getSpell(item);
        if (spell != null) {
            RenderHelper.enableGUIStandardItemLighting();

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glTranslatef(0f, 0f, -1f);

            String title = StatCollector.translateToLocal(spell.getUnlocalizedName());
            String text = StatCollector.translateToLocal(spell.getSpellDescription());
            if (title != null)
                fontRenderer.drawString(title, 2, 2, 0);

            if (text != null)
                drawString(text, 2, 8, 0, 130);

            Object[] required = MagicHandler.getMagicHandler().getSpellRecipe(item.getItemDamage());
            if (required != null && required.length > 0) {
                for (int i = 0; i < required.length; i++) {
                    Object obj = required[i];
                    ItemStack itemstack = null;
                    boolean translate = false;
                    if (obj != null) {
                        if (obj instanceof Item)
                            itemstack = new ItemStack((Item) obj);
                        else if (obj instanceof Block) {
                            translate = true;
                            itemstack = new ItemStack((Block) obj);
                        } else if (obj instanceof ItemStack)
                            itemstack = (ItemStack) obj;
                        else if (obj instanceof String) {
                            List<ItemStack> list = OreDictionary.getOres((String) obj);
                            if (!list.isEmpty())
                                itemstack = list.get(player.getEntityWorld().rand.nextInt(list.size()));
                        }
                    }
                    if (itemstack != null) {
                        GL11.glPushMatrix();
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                        GL11.glColor4f(1f, 1f, 1f, 1f);

                        if (translate)
                            GL11.glTranslatef(0f, 0.25f, -15f);
                        int drawX = 25, drawY = 93;
                        itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, (drawX + (17 * (i - (i < 4 ? 0 : 4)))), drawY + (i < 4 ? 0 : 20));

                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glPopMatrix();
                    }
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public ISpell getSpell(ItemStack stack) {
        return MagicHandler.getSpellLazy(stack.getItemDamage());
    }

    public void drawString(String text, int xOffset, int yOffset, int color, int wordWrap) {
        boolean unicode = fontRenderer.getUnicodeFlag();
        fontRenderer.setUnicodeFlag(true);
        String translated = StatCollector.translateToLocal(text).replaceAll("&", "\u00a7");
        String[] textEntries = translated.split("<br>");

        String lastFormat = "";
        String pendingFormat = "";
        for (String s : textEntries) {
            List<String> wrappedLines = new ArrayList<String>();
            String workingOn = "";

            int i = 0;
            String[] tokens = s.split(" ");
            for (String s1 : tokens) {
                boolean skipPending = false;
                String format = Page.getFormatFromString(s1);

                if (!format.isEmpty() && s1.length() > 0 && s1.charAt(0) != '\u00a7') {
                    skipPending = true;
                    pendingFormat = format;
                    format = "";
                }

                if (!pendingFormat.isEmpty() && !skipPending) {
                    format = pendingFormat;
                    pendingFormat = "";
                }

                if (MathHelper.stringNullOrLengthZero(format))
                    format = lastFormat;

                if (fontRenderer.getStringWidth(workingOn + " " + s1) >= wordWrap) {
                    wrappedLines.add(workingOn);
                    workingOn = "";
                }
                workingOn = workingOn + format + " " + s1;

                if (i == tokens.length - 1)
                    wrappedLines.add(workingOn);

                ++i;
                lastFormat = format;
            }

            for (String s1 : wrappedLines) {
                yOffset += 9;
                fontRenderer.drawString(s1, xOffset, yOffset, color);
            }

            yOffset += 4;
        }

        fontRenderer.setUnicodeFlag(unicode);
    }
}

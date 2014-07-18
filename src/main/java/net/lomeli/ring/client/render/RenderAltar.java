package net.lomeli.ring.client.render;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import net.lomeli.ring.block.tile.TileAltar;
import net.lomeli.ring.block.tile.TileItemAltar;
import net.lomeli.ring.client.model.ModelAltar;
import net.lomeli.ring.lib.ModLibs;

public class RenderAltar extends TileEntitySpecialRenderer {

    private final float size = 0.0625f;
    private ModelAltar model = new ModelAltar();
    private Render3DItem render3D = new Render3DItem();
    private Minecraft mc = Minecraft.getMinecraft();

    public RenderAltar() {
        render3D.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        renderAltar((TileItemAltar) var1, var2, var4, var6);
    }

    public void renderAltar(TileItemAltar tile, double x, double y, double z) {
        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef((float) x + 0.5f, (float) y + 1.85f, (float) z + 0.5f);
        glRotatef(180f, 1f, 0f, 0f);
        glScaled(1.25, 1.25, 1.25);

        if (tile instanceof TileAltar)
            glColor3f(0f, (15 / 255f), (90 / 255f));
        else
            glColor3f(1f, 1f, 1f);

        mc.renderEngine.bindTexture(new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":models/Altar.png"));

        model.render(size);

        glColor3f(1f, 1f, 1f);

        glEnable(GL_LIGHTING);
        glPopMatrix();

        renderItem(tile, x, y, z, tile.getStackInSlot(0));
    }

    private void renderItem(TileEntity tile, double x, double y, double z, ItemStack stack) {
        glPushMatrix();
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glTranslatef((float) x + 0.5f, (float) y + 0.7f, (float) z + 0.5f);

        if (stack != null) {
            float scale = getItemScaleFactor(stack);
            float angle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

            EntityItem ghostEntityItem = new EntityItem(tile.getWorldObj());
            ghostEntityItem.hoverStart = 0.0f;
            ghostEntityItem.setEntityItemStack(stack);

            glRotatef(angle, 0, 1f, 0);
            glScalef(scale, scale, scale);
            glTranslatef(0f, 0.2f, 0f);

            render3D.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
        }

        glEnable(GL_LIGHTING);
        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }

    private float getItemScaleFactor(ItemStack itemStack) {
        return itemStack != null ? (itemStack.getItem() instanceof ItemBlock ? 0.9f : 0.75f) : 1f;
    }
}

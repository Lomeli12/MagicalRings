package net.lomeli.ring.client.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.lomeli.ring.lib.ModLibs;

import static org.lwjgl.opengl.GL11.*;

public class EntityManaFX extends EntityFX {
    private static final ResourceLocation texture = new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":textures/misc/wisp.png");
    private Minecraft mc;

    public EntityManaFX(World world, double x, double y, double z, float size, float red, float green, float blue, float alpha, int m) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.particleGravity = 0;
        this.particleAlpha = alpha;
        this.motionY = this.motionX = this.motionZ = 0;
        this.particleScale = size;
        setMaxAge(3 * m);
        this.noClip = true;
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void renderParticle(Tessellator tess, float partialTick, float f, float f1, float f2, float f3, float f4) {
        glPushMatrix();
        mc.renderEngine.bindTexture(texture);
        glDepthMask(false);
        glEnable(GL_BLEND);
        glBlendFunc(GL11.GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glAlphaFunc(GL_GREATER, 0.003921569f);
        tess.startDrawingQuads();
        tess.setBrightness(getBrightnessForRender(partialTick));
        float scale = this.particleScale * partialTick;
        float x = (float) (prevPosX + (posX - prevPosX) * partialTick - interpPosX);
        float y = (float) (prevPosY + (posY - prevPosY) * partialTick - interpPosY);
        float z = (float) (prevPosZ + (posZ - prevPosZ) * partialTick - interpPosZ);
        glColor4f(particleRed, particleGreen, particleBlue, particleAlpha);
        tess.addVertexWithUV(x - f * scale - f3 * scale, y - f1 * scale, z - f2 * scale - f4 * scale, 0, 0);
        tess.addVertexWithUV(x - f * scale + f3 * scale, y + f1 * scale, z - f2 * scale + f4 * scale, 1, 0);
        tess.addVertexWithUV(x + f * scale + f3 * scale, y + f1 * scale, z + f2 * scale + f4 * scale, 1, 1);
        tess.addVertexWithUV(x + f * scale - f3 * scale, y - f1 * scale, z + f2 * scale - f4 * scale, 0, 1);
        tess.draw();
        glColor4f(1f, 1f, 1f, 1f);
        glDisable(GL_BLEND);
        glDepthMask(true);
        glAlphaFunc(GL_GREATER, 1f);
        glPopMatrix();
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    public EntityManaFX setMaxAge(int max) {
        particleMaxAge = max;
        return this;
    }
}

package net.lomeli.ring.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class NextPageButton extends GuiButton {
    private final boolean field_146151_o;
    private final ResourceLocation texture;

    public NextPageButton(int par1, int par2, int par3, boolean par4, ResourceLocation texture) {
        super(par1, par2, par3, 23, 13, "");
        this.field_146151_o = par4;
        this.texture = texture;
    }

    @Override
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
        if (this.visible) {
            boolean flag = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            p_146112_1_.getTextureManager().bindTexture(texture);
            int k = 0;
            int l = 192;

            if (flag) {
                k += 23;
            }

            if (!this.field_146151_o) {
                l += 13;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
        }
    }
}

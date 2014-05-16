package net.lomeli.ring.client.gui;

import net.lomeli.ring.block.tile.TileRingForge;
import net.lomeli.ring.inventory.ContainerRingForge;
import net.lomeli.ring.item.ItemHammer;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketHandler;
import net.lomeli.ring.network.PacketRingName;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRingForge extends GuiContainer {

    private TileRingForge tile;
    private GuiTextField textField;
    private final ResourceLocation guiTexture = new ResourceLocation(ModLibs.MOD_ID.toLowerCase() + ":gui/ringForge.png");

    public GuiRingForge(TileRingForge tile, InventoryPlayer inventory, World world, int x, int y, int z) {
        super(new ContainerRingForge(tile, inventory, world, z, y, z));
        this.tile = tile;

    }

    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.textField = new GuiTextField(this.fontRendererObj, i + 89, j + 69, 78, 12);
        this.textField.setTextColor(-1);
        this.textField.setDisabledTextColour(-1);
        this.textField.setEnableBackgroundDrawing(false);
        this.textField.setMaxStringLength(40);
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.textField.mouseClicked(par1, par2, par3);
    }

    protected void keyTyped(char par1, int par2) {
        if (this.textField.textboxKeyTyped(par1, par2))
            sendNamePacket();
        else
            super.keyTyped(par1, par2);
    }

    private void sendNamePacket() {
        PacketHandler.sendToServer(new PacketRingName(this.textField.getText(), (int)this.tile.xCoord, (int)this.tile.yCoord, (int)this.tile.zCoord));
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        this.textField.drawTextBox();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        this.fontRendererObj.drawString(I18n.format(ModLibs.RING_FORGE, new Object[0]), 8, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTexture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        ItemStack hammer = tile.getStackInSlot(3);
        if (hammer == null || !(hammer.getItem() instanceof ItemHammer))
            this.drawTexturedModalRect(k + 31, l + 37, 176, 0, 12, 12);

        this.drawTexturedModalRect(k + 85, l + 65, 0, 182, 84, 16);
        if (this.textField.isFocused())
            this.drawTexturedModalRect(k + 85, l + 65, 0, 166, 84, 16);
    }

}

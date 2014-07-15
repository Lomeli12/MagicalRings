package net.lomeli.ring.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.client.event.EntityViewRenderEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderHandler {
    public static boolean clearFog = false;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onSetupFog(EntityViewRenderEvent.FogDensity event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (clearFog && player != null && player.isInWater())
            event.density = 0f;//.025F;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onSetupFog(EntityViewRenderEvent.FogColors event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (clearFog && player != null && player.isInWater()) {
            float multi = 7.5F;
            event.red *= multi;
            event.blue *= multi;
            event.green *= multi;
        }
    }
}

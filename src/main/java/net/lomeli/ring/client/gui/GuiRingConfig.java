package net.lomeli.ring.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.common.config.ConfigElement;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

import net.lomeli.ring.Rings;
import net.lomeli.ring.lib.ModLibs;

public class GuiRingConfig extends GuiConfig {
    public GuiRingConfig(GuiScreen parent) {
        super(parent, getConfigElements(), ModLibs.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(Rings.configHandler.getConfig().toString()));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(new ConfigElement(Rings.configHandler.getConfig().getCategory("options")));
        list.add(new ConfigElement(Rings.configHandler.getConfig().getCategory("worldgen")));
        list.add(new ConfigElement(Rings.configHandler.getConfig().getCategory("clientoptions")));
        return list;
    }
}

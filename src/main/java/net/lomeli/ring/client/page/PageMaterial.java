package net.lomeli.ring.client.page;

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.Page;
import net.lomeli.ring.client.gui.GuiSpellBook;
import net.lomeli.ring.lib.ModLibs;

public class PageMaterial extends Page {
    private Object[] objs;
    private int color;

    public PageMaterial(GuiSpellBook guiSpellBook, int color, Object... obj) {
        super(guiSpellBook);
        this.color = color;
        objs = new Object[5];
        for (int i = 0; i < objs.length; i++) {
            if (i < obj.length)
                objs[i] = obj[i];
        }
    }

    @Override
    public PageMaterial setID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public void draw() {
        super.draw();
        for (int i = 0; i < objs.length; i++) {
            drawObjectInfo(objs[i], 30 * i);
        }
    }

    private void drawObjectInfo(Object obj, int padding) {
        if (obj != null) {
            ItemStack stack = getStackFromObj(obj);
            if (stack != null && stack.getItem() != null) {
                GL11.glColor4f(1f, 1f, 1f, 1f);
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, drawX, drawY - 5 + padding);
                mc.fontRenderer.drawStringWithShadow(stack.getDisplayName(), drawX + 20, drawY + padding, color);
                int boost = 0;
                if (Rings.proxy.ringMaterials.doesMaterialMatch(stack))
                    boost = Rings.proxy.ringMaterials.getMaterialBoost(stack);
                else if (Rings.proxy.ringMaterials.doesGemMatch(stack))
                    boost = Rings.proxy.ringMaterials.getGemBoost(stack);
                this.drawString(StatCollector.translateToLocal(ModLibs.BOOST) + " : " + (boost > 0 ? "+" + boost : boost), this.drawX + 10, this.drawY + 5 + padding, Color.BLACK.getRGB());
            } else
                mc.fontRenderer.drawStringWithShadow("§k MISSINGITEMISMISSING §r", drawX, drawY + padding, Color.RED.getRGB());
        }
    }

    private ItemStack getStackFromObj(Object obj) {
        ItemStack stack = null;
        if (obj != null) {
            if (obj instanceof Item)
                stack = new ItemStack((Item) obj);
            if (obj instanceof Block)
                stack = new ItemStack((Block) obj);
            if (obj instanceof ItemStack)
                stack = (ItemStack) obj;
            if (obj instanceof String) {
                String oreDicName = (String) obj;
                List<ItemStack> oreList = OreDictionary.getOres(oreDicName);
                if (oreList != null && !oreList.isEmpty())
                    stack = oreList.get(0);
            }
        }
        return stack;
    }
}

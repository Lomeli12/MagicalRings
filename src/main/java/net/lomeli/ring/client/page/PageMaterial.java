package net.lomeli.ring.client.page;

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

    private Object obj, obj1;
    private MaterialType mat1, mat2;
    private int tick, selected;

    public PageMaterial(GuiSpellBook guiSpellBook, Object obj, MaterialType mat1, Object obj1, MaterialType mat2) {
        super(guiSpellBook);
        this.obj = obj;
        this.mat1 = mat1;
        this.obj1 = obj1;
        this.mat2 = mat2;
    }

    @Override
    public PageMaterial setID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public void draw() {
        super.draw();
        if (obj != null) {
            ItemStack stack = getStackFromObj(obj);
            if (stack != null && stack.getItem() != null) {
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, drawX, drawY - 5);
                mc.fontRenderer.drawStringWithShadow(stack.getDisplayName(), drawX + 20, drawY, Color.CYAN.getRGB());
                int boost = 0;
                if (mat1 == MaterialType.BASIC)
                    boost = Rings.proxy.ringMaterials.getMaterialBoost(stack);
                else if (mat1 == MaterialType.GEM)
                    boost = Rings.proxy.ringMaterials.getGemBoost(stack);
                this.drawString(StatCollector.translateToLocal(ModLibs.BOOST) + " : " + (boost > 0 ? "+" + boost : boost), this.drawX + 10, this.drawY + 5, Color.BLACK.getRGB());
            } else
                mc.fontRenderer.drawStringWithShadow("§k MISSINGITEMISMISSING §r", drawX, drawY,Color.RED.getRGB());
        } else
            mc.fontRenderer.drawStringWithShadow("§k MISSINGITEMISMISSING §r", drawX, drawY,Color.RED.getRGB());
        if (obj1 != null) {
            ItemStack item = getStackFromObj(obj1);
            if (item != null && item.getItem() != null) {
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, item, drawX, drawY + 65);
                mc.fontRenderer.drawStringWithShadow(item.getDisplayName(), drawX + 20, drawY + 70, Color.CYAN.getRGB());
                int boost = 0;
                if (mat2 == MaterialType.BASIC)
                    boost = Rings.proxy.ringMaterials.getMaterialBoost(item);
                else if (mat2 == MaterialType.GEM)
                    boost = Rings.proxy.ringMaterials.getGemBoost(item);
                this.drawString(StatCollector.translateToLocal(ModLibs.BOOST) + " : " + (boost > 0 ? "+" + boost : boost), this.drawX + 10, this.drawY + 75, Color.BLACK.getRGB());
            } else
                mc.fontRenderer.drawStringWithShadow("§k MISSINGITEMISMISSING §r", drawX, drawY + 70, Color.RED.getRGB());
        } else
            mc.fontRenderer.drawStringWithShadow("§k MISSINGITEMISMISSING §r", drawX, drawY + 70, Color.RED.getRGB());
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
                if (oreList != null && !oreList.isEmpty()) {
                    stack = oreList.get(selected);
                    if (++tick >= 40)
                        selected++;
                    if (oreList.size() >= selected)
                        selected = 0;
                }
            }
        }
        return stack;
    }

    public static enum MaterialType {
        NULL, BASIC, GEM
    }
}

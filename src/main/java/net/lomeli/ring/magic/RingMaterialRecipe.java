package net.lomeli.ring.magic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.lomeli.ring.Rings;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class RingMaterialRecipe {
    private HashMap<Object, Integer> validMaterial = new HashMap<Object, Integer>();
    private HashMap<Object, Integer> materialBoost = new HashMap<Object, Integer>();
    private HashMap<Object, Integer> gemMaterial = new HashMap<Object, Integer>();
    private HashMap<Object, Integer> gemBoost = new HashMap<Object, Integer>();

    public RingMaterialRecipe() {
        this.registerItem(Items.iron_ingot, Color.LIGHT_GRAY.getRGB(), 0);
        this.registerItem(Items.gold_ingot, new Color(237, 237, 0).getRGB(), 1);
        this.registerItem(Blocks.obsidian, new Color(60, 0, 95).getRGB(), 2);
        this.registerItem(new ItemStack(ModItems.food, 1, 1), new Color(120, 85, 10).getRGB(), -2);
        this.registerItem("ingotCopper", new Color(190, 120, 0).getRGB(), 0);
        this.registerItem("ingotTin", new Color(220, 255, 245).getRGB(), 1);
        this.registerItem("ingotSteel", new Color(45, 45, 45).getRGB(), 2);
        this.registerItem("ingotSilver", new Color(165, 165, 165).getRGB(), 3);
        this.registerItem("ingotTungsten", new Color(59, 46, 74).getRGB(), 5);
        this.registerItem("ingotPlatinum", new Color(232, 232, 232).getRGB(), 4);

        this.registerMaterial("gemDiamond", new Color(100, 220, 255).getRGB(), 5);
        this.registerMaterial("gemEmerald", new Color(0, 210, 0).getRGB(), 6);
        this.registerMaterial("gemJade", new Color(0, 210, 80).getRGB(), 4);
        this.registerMaterial("gemAmethyst", new Color(140, 0, 210).getRGB(), 3);
        this.registerMaterial("gemAmber", new Color(240, 170, 0).getRGB(), 2);
        this.registerMaterial("gemPeridot", new Color(0, 160, 0).getRGB(), 1);
        this.registerMaterial("gemRuby", new Color(215, 0, 0).getRGB(), 1);
        this.registerMaterial("gemSapphire", new Color(0, 0, 130).getRGB(), 1);
        this.registerMaterial("ingotQuartz", new Color(230, 230, 230).getRGB(), 3);
    }

    public static ItemStack getNewRing(ItemStack material1, ItemStack material2, ItemStack gem, String customName) {
        ItemStack ring = null;
        
        if ((material1 != null && getMaterialList().doesMaterialMatch(material1)) && (material2 != null && getMaterialList().doesMaterialMatch(material2))) {
            if (material1.stackSize >= 10 && material2.stackSize >= 10) {
                ring = new ItemStack(ModItems.magicRing);
                ring.stackTagCompound = new NBTTagCompound();
                NBTTagCompound ringData = new NBTTagCompound();

                int color1 = getMaterialList().getMaterialColor(material1);
                int color2 = getMaterialList().getMaterialColor(material2);

                ringData.setInteger(ModLibs.L1RGB, color1);
                ringData.setInteger(ModLibs.L2RGB, color2);

                int boost1 = getMaterialList().getMaterialBoost(material1);
                int boost2 = getMaterialList().getMaterialBoost(material2);
                int gemBoost = 0;

                if (gem != null) {
                    if (getMaterialList().doesGemMatch(gem)) {
                        gemBoost = getMaterialList().getGemBoost(gem);
                        int gemColor = getMaterialList().getGemColor(gem);
                        ringData.setBoolean(ModLibs.HAS_GEM, true);
                        ringData.setInteger(ModLibs.GEM_RGB, gemColor);
                    }else
                        return null;
                }
                int boostTotal = getAverageBoost(boost1, boost2, gemBoost);
                ringData.setInteger(ModLibs.MATERIAL_BOOST, boostTotal);

                if (material1.getItem() == ModItems.onion || material2.getItem() == ModItems.onion)
                    ringData.setBoolean(ModLibs.EDIBLE, true);

                ring.getTagCompound().setTag(ModLibs.RING_TAG, ringData);

                if (customName != null && customName != "")
                    ring.setStackDisplayName(customName);
            }
        }
        return ring;
    }

    public static RingMaterialRecipe getMaterialList() {
        return Rings.proxy.ringMaterials;
    }

    public static int getAverageBoost(int par0, int par1, int par2) {
        return (int) Math.floor(Math.round(((double) (par0 + par1 + par2)) / 3d));
    }

    public void registerMaterial(ItemStack stack, int rgb, int boost) {
        if (!this.gemMaterial.containsKey(stack)) {
            this.gemMaterial.put(stack, rgb);
            this.gemBoost.put(stack, boost);
        }
    }

    public void registerMaterial(Item item, int rgb, int boost) {
        ItemStack stack = new ItemStack(item);
        this.registerMaterial(stack, rgb, boost);
    }

    public void registerMaterial(Block block, int rgb, int boost) {
        ItemStack stack = new ItemStack(block);
        this.registerMaterial(stack, rgb, boost);
    }

    public void registerMaterial(String oreDicName, int rgb, int boost) {
        if (!this.gemMaterial.containsKey(oreDicName)) {
            this.gemMaterial.put(oreDicName, rgb);
            this.gemBoost.put(oreDicName, boost);
        }
    }

    public void registerItem(ItemStack stack, int rgb, int boost) {
        if (!this.validMaterial.containsKey(stack)) {
            this.validMaterial.put(stack, rgb);
            this.materialBoost.put(stack, boost);
        }
    }

    public void registerItem(Item item, int rgb, int boost) {
        ItemStack stack = new ItemStack(item);
        this.registerItem(stack, rgb, boost);
    }

    public void registerItem(Block block, int rgb, int boost) {
        ItemStack stack = new ItemStack(block);
        this.registerItem(stack, rgb, boost);
    }

    public void registerItem(String oreDicName, int rgb, int boost) {
        if (!this.validMaterial.containsKey(oreDicName)) {
            this.validMaterial.put(oreDicName, rgb);
            this.materialBoost.put(oreDicName, boost);
        }
    }

    public int getMaterialColor(Object obj0) {
        if (obj0 instanceof ItemStack) {
            ItemStack stack = (ItemStack) obj0;
            for (Entry<Object, Integer> set : this.validMaterial.entrySet()) {
                Object obj = set.getKey();
                if (obj != null) {
                    if (obj instanceof ItemStack) {
                        ItemStack st = (ItemStack) obj;
                        if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage()))
                            return set.getValue();
                    }
                    if (obj instanceof String) {
                        for (ItemStack st : OreDictionary.getOres((String) obj)) {
                            if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage()))
                                return set.getValue();
                        }
                    }
                }
            }
        }
        return Color.WHITE.getRGB();
    }

    public int getGemColor(Object obj0) {
        if (obj0 instanceof ItemStack) {
            ItemStack stack = (ItemStack) obj0;
            for (Entry<Object, Integer> set : this.gemMaterial.entrySet()) {
                Object obj = set.getKey();
                if (obj != null) {
                    if (obj instanceof ItemStack) {
                        ItemStack st = (ItemStack) obj;
                        if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage()))
                            return set.getValue();
                    }
                    if (obj instanceof String) {
                        for (ItemStack st : OreDictionary.getOres((String) obj)) {
                            if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage()))
                                return set.getValue();
                        }
                    }
                }
            }
        }
        return Color.WHITE.getRGB();
    }

    public boolean doesGemMatch(ItemStack stack) {
        boolean match = false;
        for (Entry<Object, Integer> set : this.gemMaterial.entrySet()) {
            Object obj = set.getKey();
            if (obj != null) {
                if (obj instanceof ItemStack) {
                    ItemStack st = (ItemStack) obj;
                    match = (st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage());
                }
                if (obj instanceof String) {
                    for (ItemStack st : OreDictionary.getOres((String) obj)) {
                        if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage())) {
                            match = true;
                            break;
                        }
                    }
                }
                if (match)
                    break;
            }
        }
        return match;
    }

    public boolean doesMaterialMatch(ItemStack stack) {
        boolean match = false;
        materialLoop: for (Entry<Object, Integer> set : this.validMaterial.entrySet()) {
            Object obj = set.getKey();
            if (obj != null) {
                if (obj instanceof ItemStack) {
                    ItemStack st = (ItemStack) obj;
                    if (st.getItem() == stack.getItem() && st.getItemDamage() == stack.getItemDamage()) {
                        match = true;
                        break materialLoop;
                    }
                }
                if (obj instanceof String) {
                    for (ItemStack st : OreDictionary.getOres((String) obj)) {
                        if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage())) {
                            match = true;
                            break materialLoop;
                        }
                    }
                }
            }
        }
        return match;
    }

    public int getMaterialBoost(Object obj0) {
        if (obj0 instanceof ItemStack) {
            ItemStack stack = (ItemStack) obj0;
            for (Entry<Object, Integer> set : this.materialBoost.entrySet()) {
                Object obj = set.getKey();
                if (obj != null) {
                    if (obj instanceof ItemStack) {
                        ItemStack st = (ItemStack) obj;
                        if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage()))
                            return set.getValue();
                    }
                    if (obj instanceof String) {
                        for (ItemStack st : OreDictionary.getOres((String) obj)) {
                            if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage()))
                                return set.getValue();
                        }
                    }
                }
            }
        }
        return 0;
    }

    public int getGemBoost(Object obj0) {
        if (obj0 instanceof ItemStack) {
            ItemStack stack = (ItemStack) obj0;
            for (Entry<Object, Integer> set : this.gemBoost.entrySet()) {
                Object obj = set.getKey();
                if (obj != null) {
                    if (obj instanceof ItemStack) {
                        ItemStack st = (ItemStack) obj;
                        if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage()))
                            return set.getValue();
                    }
                    if (obj instanceof String) {
                        for (ItemStack st : OreDictionary.getOres((String) obj)) {
                            if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage()))
                                return set.getValue();
                        }
                    }
                }
            }
        }
        return 0;
    }
}

package net.lomeli.ring.magic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IMaterialRegistry;
import net.lomeli.ring.api.interfaces.recipe.IMaterial;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.ModLibs;

// TODO Fix this unholy mess!
public class RingMaterialRegistry implements IMaterialRegistry {
    public List<IMaterial> materialList;
    public List<IMaterial> gemList;

    public RingMaterialRegistry() {
        materialList = new ArrayList<IMaterial>();
        gemList = new ArrayList<IMaterial>();
        this.registerMaterial("ingotIron", Color.LIGHT_GRAY.getRGB(), 0);
        this.registerMaterial("ingotGold", new Color(0xeded00).getRGB(), 1);
        this.registerMaterial(Blocks.obsidian, new Color(0x3c005f).getRGB(), 2);
        this.registerMaterial(new ItemStack(ModItems.food, 1, 1), new Color(0x78550a).getRGB(), -2);
        this.registerMaterial("ingotCopper", new Color(0xbe7800).getRGB(), 0);
        this.registerMaterial("ingotTin", new Color(0xdcfff5).getRGB(), 1);
        this.registerMaterial("ingotSteel", new Color(0x2d2d2d).getRGB(), 2);
        this.registerMaterial("ingotSilver", new Color(0xa5a5a5).getRGB(), 3);
        this.registerMaterial("ingotTungsten", new Color(0x2d223a).getRGB(), 5);
        this.registerMaterial("ingotPlatinum", new Color(0x64CDED).getRGB(), 4);
        this.registerMaterial("ingotThaumium", new Color(0x51437D).getRGB(), 4);
        this.registerMaterial("ingotRedAlloy", new Color(0xD40404).getRGB(), 2);
        this.registerMaterial("ingotBlueAlloy", new Color(0x404d4).getRGB(), 2);
        this.registerMaterial("ingotManasteel", new Color(0x47E2D8).getRGB(), 2);
        this.registerMaterial("ingotElvenElementium", new Color(0xfb67ff).getRGB(), 4);
        this.registerMaterial("ingotTerrasteel", new Color(0x2fac2d).getRGB(), 5);

        this.registerGem("gemDiamond", new Color(0x64dcff).getRGB(), 5);
        this.registerGem("gemEmerald", new Color(0xd200).getRGB(), 6);
        this.registerGem("gemJade", new Color(0x87f0af).getRGB(), 4);
        this.registerGem("gemAmethyst", new Color(0x8c00d2).getRGB(), 3);
        this.registerGem("gemAmber", new Color(0xf0aa00).getRGB(), 2);
        this.registerGem("gemPeridot", new Color(0xa000).getRGB(), 1);
        this.registerGem("gemRuby", new Color(0xd70000).getRGB(), 1);
        this.registerGem("gemSapphire", new Color(0x82).getRGB(), 1);
        this.registerGem("ingotQuartz", new Color(0xe6e6e6).getRGB(), 3);
        this.registerGem("manaDiamond", new Color(0xa0f5ef).getRGB(), 6);
        this.registerGem("manaPearl", new Color(0x398e8).getRGB(), 4);
    }

    public static ItemStack getNewRing(ItemStack material1, ItemStack material2, ItemStack gem, String customName) {
        ItemStack ring = null;

        if ((material1 != null && getMaterialList().doesMaterialMatch(material1)) && (material2 != null && getMaterialList().doesMaterialMatch(material2))) {
            if (material1.stackSize >= 10 && material2.stackSize >= 10) {
                ring = new ItemStack(ModItems.magicRing);
                ring.stackTagCompound = new NBTTagCompound();
                NBTTagCompound ringData = new NBTTagCompound();

                boolean edible = false;
                if (material1.getItem() instanceof ItemFood || material2.getItem() instanceof ItemFood)
                    edible = true;

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
                        if (gem.getItem() instanceof ItemFood)
                            edible = true;
                    } else
                        return null;
                }
                if (edible)
                    ringData.setBoolean(ModLibs.EDIBLE, true);

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

    public static RingMaterialRegistry getMaterialList() {
        return Rings.proxy.ringMaterials;
    }

    public static int getAverageBoost(int par0, int par1, int par2) {
        int j = par0 + par1;
        if (j > 10)
            j = j / 2;
        return j + MathHelper.floor_float(par2 / 3);
    }

    public void registerGem(IMaterial gem) {
        this.gemList.add(gem);
    }

    @Override
    public void registerGem(ItemStack stack, int rgb, int boost) {
        registerGem(new RingMaterial(stack, rgb, boost));
    }

    @Override
    public void registerGem(Item item, int rgb, int boost) {
        ItemStack stack = new ItemStack(item);
        this.registerGem(stack, rgb, boost);
    }

    @Override
    public void registerGem(Block block, int rgb, int boost) {
        ItemStack stack = new ItemStack(block);
        this.registerGem(stack, rgb, boost);
    }

    @Override
    public void registerGem(String oreDicName, int rgb, int boost) {
        registerGem(new RingMaterial(oreDicName, rgb, boost));
    }

    public void registerMaterial(IMaterial ringMaterial) {
        this.materialList.add(ringMaterial);
    }

    @Override
    public void registerMaterial(ItemStack stack, int rgb, int boost) {
        registerMaterial(new RingMaterial(stack, rgb, boost));
    }

    @Override
    public void registerMaterial(Item item, int rgb, int boost) {
        ItemStack stack = new ItemStack(item);
        this.registerMaterial(stack, rgb, boost);
    }

    @Override
    public void registerMaterial(Block block, int rgb, int boost) {
        ItemStack stack = new ItemStack(block);
        this.registerMaterial(stack, rgb, boost);
    }

    @Override
    public void registerMaterial(String oreDicName, int rgb, int boost) {
        registerMaterial(new RingMaterial(oreDicName, rgb, boost));
    }

    public int getMaterialColor(Object obj0) {
        if (obj0 instanceof ItemStack) {
            IMaterial material = (IMaterial) getMaterialInfo((ItemStack) obj0, 0);
            if (material != null)
                return material.getColor();
        }
        return Color.WHITE.getRGB();
    }

    public int getGemColor(Object obj0) {
        if (obj0 instanceof ItemStack) {
            IMaterial material = (IMaterial) getMaterialInfo((ItemStack) obj0, 1);
            if (material != null)
                return material.getColor();
        }
        return Color.WHITE.getRGB();
    }

    public boolean doesGemMatch(ItemStack stack) {
        return getMaterialInfo(stack, 1) != null;
    }

    public boolean doesMaterialMatch(ItemStack stack) {
        return getMaterialInfo(stack, 0) != null;
    }

    public int getMaterialBoost(Object obj0) {
        if (obj0 instanceof ItemStack) {
            IMaterial material = (IMaterial) getMaterialInfo((ItemStack) obj0, 0);
            if (material != null)
                return material.getBoost();
        }
        return 0;
    }

    public int getGemBoost(Object obj0) {
        if (obj0 instanceof ItemStack) {
            IMaterial material = (IMaterial) getMaterialInfo((ItemStack) obj0, 1);
            if (material != null)
                return material.getBoost();
        }
        return 0;
    }

    public Object getMaterialInfo(ItemStack stack, int type) {
        if (type == 0) {
            for (int i = 0; i < this.materialList.size(); i++) {
                IMaterial material = this.materialList.get(i);
                if (material != null) {
                    Object obj = material.getMaterial();
                    if (obj instanceof ItemStack) {
                        ItemStack st = (ItemStack) obj;
                        if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage()))
                            return material;
                    }
                    if (obj instanceof String) {
                        if (SimpleUtil.isStackRegisteredAsOreDic(stack, (String) obj))
                            return material;
                    }
                }
            }
        } else {
            for (int i = 0; i < this.gemList.size(); i++) {
                IMaterial material = this.gemList.get(i);
                if (material != null) {
                    Object obj = material.getMaterial();
                    if (obj instanceof ItemStack) {
                        ItemStack st = (ItemStack) obj;
                        if ((st.getItem() == stack.getItem()) && (st.getItemDamage() == stack.getItemDamage()))
                            return material;
                    }
                    if (obj instanceof String) {
                        if (SimpleUtil.isStackRegisteredAsOreDic(stack, (String) obj)) {
                            return material;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static class RingMaterial implements IMaterial {
        private Object material;
        private int color, boost;

        public RingMaterial(Object material, int color, int boost) {
            this.material = material;
            this.color = color;
            this.boost = boost;
        }

        @Override
        public Object getMaterial() {
            return material;
        }

        @Override
        public int getColor() {
            return color;
        }

        @Override
        public int getBoost() {
            return boost;
        }
    }
}

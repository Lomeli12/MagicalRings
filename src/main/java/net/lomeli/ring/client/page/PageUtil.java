package net.lomeli.ring.client.page;

import java.util.*;

import net.minecraft.item.ItemStack;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IPageRegistry;
import net.lomeli.ring.api.Page;
import net.lomeli.ring.block.ModBlocks;
import net.lomeli.ring.client.gui.GuiSpellBook;
import net.lomeli.ring.item.ModItems;
import net.lomeli.ring.lib.BookText;
import net.lomeli.ring.lib.ModLibs;

public class PageUtil implements IPageRegistry {
    private static List<Page> addonPages = new ArrayList<Page>();
    public static int[][] stackorder = new int[][]{
            {0, 0},
            {1, 0},
            {0, 1},
            {1, 1},
            {0, 2},
            {1, 2},
            {2, 0},
            {2, 1},
            {2, 2}};

    public static void addPage(Page pg) {
        if (addonPages.contains(pg))
            return;
        addonPages.add(pg);
    }

    public static void loadBaseBook(GuiSpellBook screen) {
        screen.avaliablePages.clear();
        screen.avaliablePages.add(new PageTitle(screen, new ItemStack(ModItems.book).getDisplayName()));
        screen.avaliablePages.add(new PageText(screen, BookText.INTRO));
        screen.avaliablePages.add(new PageItem(screen, new ItemStack(ModItems.magicRing), BookText.RING).setID(ModLibs.MOD_ID.toLowerCase() + ".ring"));
        screen.avaliablePages.add(new PageItem(screen, new ItemStack(ModBlocks.ringForge), BookText.FORGE).setID(ModLibs.MOD_ID.toLowerCase() + ".ringForge"));
        screen.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModBlocks.ringForge), BookText.FORGE2));
        screen.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.ironHammer)).setID(ModLibs.MOD_ID.toLowerCase() + ".hammer"));
        screen.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.diamondHammer)));
        screen.avaliablePages.add(new PageText(screen, BookText.ALTAR_INTRO, BookText.ALTAR));
        screen.avaliablePages.add(new PageImage(screen, "", BookText.ALTAR_INTRO, BookText.IMAGES, 0, 107, 115, 86));
        screen.avaliablePages.add(new PageItem(screen, new ItemStack(ModBlocks.altar), BookText.ALTAR2).setID(ModLibs.MOD_ID.toLowerCase() + ".infusionAltar"));
        screen.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModBlocks.altar)));
        screen.avaliablePages.add(new PageItem(screen, new ItemStack(ModBlocks.altar, 1, 1), BookText.ITEM_ALTAR).setID(ModLibs.MOD_ID.toLowerCase() + ".altar"));
        screen.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModBlocks.altar, 1, 1)));
        screen.avaliablePages.add(new PageInfusionSetup(screen));
        screen.avaliablePages.add(new PageText(screen, BookText.ALTAR_INTRO, BookText.INFUSE_INFO));
        screen.avaliablePages.add(new PageItem(screen, new ItemStack(ModItems.spellParchment), BookText.SPELL).setID(ModLibs.MOD_ID.toLowerCase() + ".spellParchment"));
        screen.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.spellParchment), BookText.SPELL2));
        screen.avaliablePages.add(new PageText(screen, BookText.RING_USE));
        screen.avaliablePages.add(new PageText(screen, BookText.BAUBLE_INTRO, BookText.BAUBLE));
        screen.avaliablePages.add(new PageImage(screen, BookText.BAUBLE1, BookText.BAUBLE_INTRO, BookText.IMAGES, 0, 0, 107, 100));
        screen.avaliablePages.add(new PageText(screen, BookText.BAUBLE_INTRO, BookText.BAUBLE2));
        screen.avaliablePages.add(new PageText(screen, ModLibs.MANA, BookText.MANA));
        screen.avaliablePages.add(new PageRecipe(screen, new ItemStack(ModItems.food, 1, 2), BookText.MANA2));
        if (!addonPages.isEmpty()) {
            for (Page pg : addonPages) {
                if (pg.gui == null)
                    pg.setGui(screen);
                screen.avaliablePages.add(pg);
            }
        }
    }

    public static void loadMaterialBook(GuiSpellBook gui) {
        gui.avaliablePages.clear();
        gui.avaliablePages.add(new PageTitle(gui, new ItemStack(ModItems.book, 1, 1).getDisplayName()));
        gui.avaliablePages.add(new PageText(gui, BookText.BASIC_MATERIAL, BookText.MATERIAL_INTRO));
        gui.avaliablePages.add(new PageItem(gui, new ItemStack(ModItems.materials, 1, 0), BookText.BAT_WING));
        gui.avaliablePages.add(new PageRecipe(gui, new ItemStack(ModItems.materials, 1, 1), BookText.FIRE_STONE));
        gui.avaliablePages.add(new PageItem(gui, new ItemStack(ModItems.materials, 1, 2), BookText.ERUPTING_STONE));
        gui.avaliablePages.add(new PageRecipe(gui, new ItemStack(ModItems.materials, 1, 3), BookText.CHARGE_STONE));
        gui.avaliablePages.add(new PageItem(gui, new ItemStack(ModItems.materials, 1, 4), BookText.TENTACLE));
        gui.avaliablePages.add(new PageTitle(gui, BookText.RING_MATERIAL));
        for (int i = 0; i < Rings.proxy.ringMaterials.validMaterial.size(); i++) {
            Object obj1 = null, obj2 = null;
            if (i < Rings.proxy.ringMaterials.validMaterial.size())
                obj1 = getObjectByIndex(i, Rings.proxy.ringMaterials.validMaterial);
            i++;
            if (i < Rings.proxy.ringMaterials.validMaterial.size())
                obj2 = getObjectByIndex(i, Rings.proxy.ringMaterials.validMaterial);
            PageMaterial.MaterialType type1 = obj1 != null ? PageMaterial.MaterialType.BASIC : PageMaterial.MaterialType.NULL;
            PageMaterial.MaterialType type2 = obj2 != null ? PageMaterial.MaterialType.BASIC : PageMaterial.MaterialType.NULL;
            gui.avaliablePages.add(new PageMaterial(gui, obj1, type1, obj2, type2));
        }
        gui.avaliablePages.add(new PageTitle(gui, BookText.RING_GEM));
        for (int i = 0; i < Rings.proxy.ringMaterials.gemMaterial.size(); i++) {
            Object obj1 = null, obj2 = null;
            if (i < Rings.proxy.ringMaterials.gemMaterial.size())
                obj1 = getObjectByIndex(i, Rings.proxy.ringMaterials.gemMaterial);
            i++;
            if (i < Rings.proxy.ringMaterials.gemMaterial.size())
                obj2 = getObjectByIndex(i, Rings.proxy.ringMaterials.gemMaterial);
            PageMaterial.MaterialType type1 = obj1 != null ? PageMaterial.MaterialType.GEM : PageMaterial.MaterialType.NULL;
            PageMaterial.MaterialType type2 = obj2 != null ? PageMaterial.MaterialType.GEM : PageMaterial.MaterialType.NULL;
            gui.avaliablePages.add(new PageMaterial(gui, obj1, type1, obj2, type2));
        }
    }

    private static Object getObjectByIndex(int index, LinkedHashMap<Object, Integer> map) {
        if (map != null) {
            Iterator it = map.entrySet().iterator();
            int n = 0;
            while (it.hasNext()) {
                Map.Entry<Object, Integer> entry = (Map.Entry<Object, Integer>) it.next();
                if (n == index)
                    return entry.getKey();
                n++;
            }
        }
        return null;
    }

    @Override
    public void addTitlePage(String title, String id) {
        addPage(new PageTitle(null, title).setID(id));
    }

    @Override
    public void addTextPage(String title, String text, String id) {
        addPage(new PageText(null, title, text).setID(id));
    }

    @Override
    public void addImagePage(String title, String alttext, String resource, int u, int v, int width, int height, String id) {
        addPage(new PageImage(null, title, alttext, resource, u, v, width, height).setID(id));
    }

    @Override
    public void addItemPage(ItemStack item, String alttext, String id) {
        addPage(new PageItem(null, item, alttext).setID(id));
    }

    @Override
    public void addItemRecipePage(ItemStack item, String alttext, String id) {
        addPage(new PageRecipe(null, item, alttext).setID(id));
    }

    @Override
    public void addNewPage(Page page, String id) {
        if (page.pageID() == null || page.pageID() == "")
            page.setID(id);
        addNewPage(page);
    }

    @Override
    public void addNewPage(Page page) {
        addPage(page);
    }

    public static class StackPosition {
        Object itemObj;
        int rlyX, rlyY;

        public StackPosition(Object obj, int x, int y) {
            itemObj = obj;
            rlyX = x;
            rlyY = y;
        }
    }

    public static class ToolTipInfo {
        private int x, y;
        private String toolTipText;

        public ToolTipInfo(int x, int y, String text) {
            this.x = x;
            this.y = y;
            this.toolTipText = text;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getToolTipText() {
            return toolTipText;
        }
    }
}

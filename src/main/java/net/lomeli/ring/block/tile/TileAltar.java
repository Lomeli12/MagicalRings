package net.lomeli.ring.block.tile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.api.interfaces.recipe.IInfusionRecipe;
import net.lomeli.ring.core.helper.SimpleUtil;
import net.lomeli.ring.item.ItemMagicRing;
import net.lomeli.ring.lib.ModLibs;

public class TileAltar extends TileItemAltar {
    private int timer, effectTick;
    private String id;
    private boolean startRingInfusion, startItemInfusion, infoCollected;
    private List<TileItemAltar> tiles = new ArrayList<TileItemAltar>(), tilesToGetFrom = new ArrayList<TileItemAltar>();
    private List<ItemStack> tempInventory = new ArrayList<ItemStack>();
    private IInfusionRecipe infusionRecipe;

    public TileAltar() {
        super();
    }

    public boolean isInfusing() {
        return startItemInfusion || startRingInfusion;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.startRingInfusion)
            ringInfusion();
        else if (startItemInfusion)
            itemInfusion();
        if (isInfusing()) {
            if (++effectTick >= 15) {
                if (worldObj.isRemote) {
                    int rgb = Color.CYAN.getRGB();
                    float r = (rgb >> 16 & 255) / 255.0F;
                    float g = (rgb >> 8 & 255) / 255.0F;
                    float b = (rgb & 255) / 255.0F;
                    for (int i = 0; i < 16; ++i) {
                        EntityPortalFX effect = new EntityPortalFX(worldObj, xCoord + 0.5, yCoord + 0.1 + worldObj.rand.nextDouble() * 2.0D, zCoord + 0.5, worldObj.rand.nextGaussian(), 0.0D, worldObj.rand.nextGaussian());
                        effect.setRBGColorF(r, g, b);
                        Minecraft.getMinecraft().effectRenderer.addEffect(effect);
                    }
                }
                effectTick = 0;
            }
        }
        tiles.clear();
    }

    public void addPossibleTile(int x, int z) {
        TileEntity tile = worldObj.getTileEntity(xCoord + x, yCoord, zCoord + z);
        if (tile != null && tile instanceof TileItemAltar) {
            if (!tiles.contains(tile))
                tiles.add((TileItemAltar) tile);
        }
    }

    private boolean basicCheck(int i) {
        this.addPossibleTile(-2, -2);
        this.addPossibleTile(-2, 0);
        this.addPossibleTile(-2, 2);
        this.addPossibleTile(0, -2);
        this.addPossibleTile(0, 2);
        this.addPossibleTile(2, -2);
        this.addPossibleTile(2, 0);
        this.addPossibleTile(2, 2);

        if (getStackInSlot(0) == null || tiles.isEmpty()) {
            if (i == 0)
                reset();
            else
                resetItem();
            return false;
        }
        return true;
    }

    //<editor-fold desc="Item Infusion">
    // Item Infusion code
    private void itemInfusion() {
        if (!basicCheck(1))
            return;
        if (infusionRecipe != null) {
            if (!this.infoCollected)
                this.matchInfusionRecipe();
            else {
                if (++timer >= 30) {
                    if (!tilesToGetFrom.isEmpty()) {
                        if (tilesToGetFrom.get(0).getStackInSlot(0) == null) {
                            tilesToGetFrom.clear();
                            resetItem();
                            return;
                        }
                        tempInventory.add(tilesToGetFrom.get(0).getStackInSlot(0));
                        tilesToGetFrom.get(0).spawnEffects();
                        tilesToGetFrom.get(0).setInventorySlotContents(0, null);
                        tilesToGetFrom.get(0).markDirty();
                        tilesToGetFrom.remove(0);
                        timer = 0;
                    }
                }
                if (tilesToGetFrom.isEmpty()) {
                    if (++timer >= 30) {
                        ItemStack stack = getStackInSlot(0);
                        if (stack != null && stack.getItem() != null && SimpleUtil.areItemObjectsSame(stack, infusionRecipe.getBaseItem())) {
                            this.setInventorySlotContents(0, null);
                            this.markDirty();
                            this.setInventorySlotContents(0, infusionRecipe.getOutput().copy());
                            this.markDirty();
                        } else
                            resetItem();
                        EntityLightningBolt light = new EntityLightningBolt(worldObj, xCoord, yCoord, zCoord);
                        this.spawnEffects();
                        worldObj.spawnEntityInWorld(light);
                        worldObj.playSound(xCoord, yCoord, zCoord, "random.levelup", 0.5F, 0.4F / ((float) worldObj.rand.nextDouble() * 0.4F + 0.8F), false);
                        simpleItemReset();
                    }
                }
            }
        }
    }

    private void matchInfusionRecipe() {
        Object[] ingredients = infusionRecipe.getIngredients();
        if (ingredients == null) {
            resetItem();
            return;
        }
        List<Object> itemList = new ArrayList<Object>();
        for (Object obj : ingredients) {
            if (obj != null) {
                if (obj instanceof String) {
                    itemList.add(obj);
                } else {
                    ItemStack item = null;
                    if (obj instanceof ItemStack)
                        item = (ItemStack) obj;
                    else if (obj instanceof Item)
                        item = new ItemStack((Item) obj);
                    else if (obj instanceof Block)
                        item = new ItemStack((Block) obj);
                    if (item != null)
                        itemList.add(item);
                }
            }
        }

        if (itemList.isEmpty()) {
            resetItem();
            return;
        }

        for (int i = 0; i < tiles.size(); i++) {
            TileItemAltar tile = tiles.get(i);
            if (tile != null) {

                ItemStack tileItem = tile.getStackInSlot(0);
                if (tileItem != null) {
                    if (itemList.isEmpty())
                        break;
                    itemLoop:
                    for (int j = 0; j < itemList.size(); j++) {
                        Object obj = itemList.get(j);
                        if (obj != null) {
                            if (obj instanceof ItemStack) {
                                ItemStack ingredient = (ItemStack) obj;
                                if (tileItem.getItem() == ingredient.getItem() && tileItem.getItemDamage() == ingredient.getItemDamage()) {
                                    itemList.remove(j);
                                    tilesToGetFrom.add(tile);
                                    break itemLoop;
                                }
                            }
                            if (obj instanceof String) {
                                String oreName = (String) obj;
                                if (SimpleUtil.isStackRegisteredAsOreDic(tileItem, oreName)) {
                                    itemList.remove(j);
                                    tilesToGetFrom.add(tile);
                                    break itemLoop;
                                }
                            }
                        } else
                            itemList.remove(j);
                    }
                }
            }
        }

        if (itemList.isEmpty())
            this.infoCollected = true;
    }

    private void simpleItemReset() {
        this.infusionRecipe = null;
        this.timer = 0;
        this.tiles.clear();
        for (TileItemAltar tile : this.tilesToGetFrom) {
            tile.setInventorySlotContents(0, null);
        }
        this.tilesToGetFrom.clear();
        this.startItemInfusion = false;
        this.infoCollected = false;
        this.tempInventory.clear();
    }

    private void resetItem() {
        resetItem(true);
    }

    private void resetItem(boolean i) {
        this.infusionRecipe = null;
        this.timer = 0;
        this.tiles.clear();
        this.tilesToGetFrom.clear();
        this.startItemInfusion = false;
        if (!this.tempInventory.isEmpty()) {
            for (ItemStack stack : this.tempInventory) {
                if (stack != null) {
                    EntityItem ent = new EntityItem(worldObj, xCoord, yCoord, zCoord, stack);
                    if (!worldObj.isRemote)
                        worldObj.spawnEntityInWorld(ent);
                }
            }
        }
        this.tempInventory.clear();
        if (i)
            worldObj.playSound(xCoord, yCoord, zCoord, "random.fizz", 0.5F, 0.4F / ((float) worldObj.rand.nextDouble() * 0.4F + 0.8F), false);
    }
    //</editor-fold>

    //<editor-fold desc="Ring Infusion">
    // Ring Infusion
    private void ringInfusion() {
        if (!basicCheck(0))
            return;
        ISpell spell = Rings.proxy.spellRegistry.getSpell(id);
        if (spell != null) {

            if (!this.infoCollected)
                this.matchSpellRecipe();
            else {
                if (++timer >= 30) {
                    if (!tilesToGetFrom.isEmpty()) {
                        if (tilesToGetFrom.get(0).getStackInSlot(0) == null) {
                            reset();
                            tilesToGetFrom.clear();
                            return;
                        }
                        tempInventory.add(tilesToGetFrom.get(0).getStackInSlot(0));
                        tilesToGetFrom.get(0).spawnEffects();
                        tilesToGetFrom.get(0).setInventorySlotContents(0, null);
                        tilesToGetFrom.get(0).markDirty();
                        tilesToGetFrom.remove(0);
                        timer = 0;
                    }
                }
                if (tilesToGetFrom.isEmpty()) {
                    if (++timer >= 30) {
                        ItemStack stack = getStackInSlot(0);
                        if (stack != null && stack.getItem() instanceof ItemMagicRing) {
                            if (stack.getTagCompound() == null)
                                stack.stackTagCompound = new NBTTagCompound();
                            NBTTagCompound tag = stack.getTagCompound().hasKey(ModLibs.RING_TAG) ? stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG) : new NBTTagCompound();
                            tag.setString(ModLibs.SPELL_ID, id);
                            stack.getTagCompound().setTag(ModLibs.RING_TAG, tag);
                        } else
                            reset();
                        EntityLightningBolt light = new EntityLightningBolt(worldObj, xCoord, yCoord, zCoord);
                        this.spawnEffects();
                        worldObj.spawnEntityInWorld(light);
                        worldObj.playSound(xCoord, yCoord, zCoord, "random.levelup", 0.5F, 0.4F / ((float) worldObj.rand.nextDouble() * 0.4F + 0.8F), false);
                        simpleReset();
                    }
                }
            }
        }
    }

    public void simpleReset() {
        this.id = null;
        this.timer = 0;
        this.tiles.clear();
        for (TileItemAltar tile : this.tilesToGetFrom) {
            tile.setInventorySlotContents(0, null);
        }
        this.tilesToGetFrom.clear();
        this.startRingInfusion = false;
        this.infoCollected = false;
        this.tempInventory.clear();
    }

    public void reset() {
        reset(true);
    }

    public void reset(boolean i) {
        this.id = null;
        this.timer = 0;
        this.tiles.clear();
        this.tilesToGetFrom.clear();
        this.startRingInfusion = false;
        this.infoCollected = false;
        if (!this.tempInventory.isEmpty()) {
            for (ItemStack stack : this.tempInventory) {
                if (stack != null) {
                    EntityItem ent = new EntityItem(worldObj, xCoord, yCoord, zCoord, stack);
                    if (!worldObj.isRemote)
                        worldObj.spawnEntityInWorld(ent);
                }
            }
        }
        this.tempInventory.clear();
        if (i)
            worldObj.playSound(xCoord, yCoord, zCoord, "random.fizz", 0.5F, 0.4F / ((float) worldObj.rand.nextDouble() * 0.4F + 0.8F), false);
    }

    public void matchSpellRecipe() {
        Object[] ingredients = Rings.proxy.spellRegistry.getSpellRecipe(id);
        if (ingredients == null) {
            reset();
            return;
        }
        List<Object> itemList = new ArrayList<Object>();
        for (Object obj : ingredients) {
            if (obj != null) {
                if (obj instanceof String) {
                    itemList.add(obj);
                } else {
                    ItemStack item = null;
                    if (obj instanceof ItemStack)
                        item = (ItemStack) obj;
                    else if (obj instanceof Item)
                        item = new ItemStack((Item) obj);
                    else if (obj instanceof Block)
                        item = new ItemStack((Block) obj);
                    if (item != null)
                        itemList.add(item);
                }
            }
        }

        if (itemList.isEmpty()) {
            reset();
            return;
        }

        for (int i = 0; i < tiles.size(); i++) {
            TileItemAltar tile = tiles.get(i);
            if (tile != null) {
                ItemStack tileItem = tile.getStackInSlot(0);
                if (tileItem != null) {
                    if (itemList.isEmpty())
                        break;
                    itemLoop:
                    for (int j = 0; j < itemList.size(); j++) {
                        Object obj = itemList.get(j);
                        if (obj != null) {
                            if (obj instanceof ItemStack) {
                                ItemStack ingredient = (ItemStack) obj;
                                if (tileItem.getItem() == ingredient.getItem() && tileItem.getItemDamage() == ingredient.getItemDamage()) {
                                    itemList.remove(j);
                                    tilesToGetFrom.add(tile);
                                    break itemLoop;
                                }
                            }
                            if (obj instanceof String) {
                                String oreName = (String) obj;
                                if (SimpleUtil.isStackRegisteredAsOreDic(tileItem, oreName)) {
                                    itemList.remove(j);
                                    tilesToGetFrom.add(tile);
                                    break itemLoop;
                                }
                            }
                        } else
                            itemList.remove(j);
                    }
                }
            }
        }

        if (itemList.isEmpty())
            this.infoCollected = true;
    }

    public boolean hasBeenInfused(ItemStack stack) {
        if (stack != null && stack.getTagCompound() != null) {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
            return tag.hasKey(ModLibs.SPELL_ID);
        }
        return true;
    }
    //</editor-fold>

    public void startInfusion(EntityPlayer player, String spellId) {
        resetItem(false);
        reset(false);
        String noEXP = StatCollector.translateToLocal(ModLibs.NO_EXP);
        if (getStackInSlot(0) != null) {
            if (spellId == null) {
                basicCheck(1);
                ItemStack[] items = new ItemStack[tiles.size()];
                for (int i = 0; i < tiles.size(); i++) {
                    items[i] = tiles.get(i).getStackInSlot(0);
                }
                IInfusionRecipe recipe = Rings.proxy.infusionRegistry.getRecipeFromItems(items);
                tiles.clear();
                if (recipe != null && recipe.getOutput() != null && recipe.getOutput().getItem() != null) {
                    if (SimpleUtil.areItemObjectsSame(recipe.getBaseItem(), this.getStackInSlot(0))) {
                        int cost = recipe.getManaCost();
                        IPlayerSession session = Rings.proxy.manaHandler.getPlayerSession(player);
                        if (session != null && session.hasEnoughMana(cost)) {
                            if (!player.capabilities.isCreativeMode) {
                                session.useMana(cost, false);
                                player.getCurrentEquippedItem().damageItem(1, player);
                                if (!worldObj.isRemote)
                                    Rings.proxy.manaHandler.updatePlayerSession(session, worldObj.provider.dimensionId);
                            }
                            this.infusionRecipe = recipe;
                            this.startItemInfusion = true;
                        } else {
                            if (!worldObj.isRemote)
                                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(ModLibs.NO_MANA) + " " + cost));
                        }
                    }
                }

            } else {
                if (this.hasBeenInfused(getStackInSlot(0))) {
                    if (player.experienceLevel >= 35 || player.capabilities.isCreativeMode) {
                        if (!player.capabilities.isCreativeMode) {
                            player.addExperienceLevel(-35);
                            player.getCurrentEquippedItem().stackSize--;
                        }
                        this.id = spellId;
                        this.startRingInfusion = true;
                    } else {
                        if (!worldObj.isRemote)
                            player.addChatMessage(new ChatComponentText(noEXP + " " + StatCollector.translateToLocal(ModLibs.EXP_2)));
                    }
                } else {
                    if (player.experienceLevel >= 30 || player.capabilities.isCreativeMode) {
                        if (!player.capabilities.isCreativeMode) {
                            player.addExperienceLevel(-30);
                            player.getCurrentEquippedItem().stackSize--;
                        }
                        this.id = spellId;
                        this.startRingInfusion = true;
                    } else {
                        if (!worldObj.isRemote)
                            player.addChatMessage(new ChatComponentText(noEXP + " " + StatCollector.translateToLocal(ModLibs.EXP_1)));
                    }
                }
            }
        }
    }
}

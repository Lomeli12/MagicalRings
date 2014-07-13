package net.lomeli.ring.block.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.item.ItemMagicRing;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.MagicHandler;

public class TileAltar extends TileItemAltar {
    private int timer, spellID;
    private boolean startInfusion, infoCollected;
    private List<TileItemAltar> tiles = new ArrayList<TileItemAltar>(), tilesToGetFrom = new ArrayList<TileItemAltar>();
    private List<ItemStack> tempInventory = new ArrayList<ItemStack>();

    public TileAltar() {
        super();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.startInfusion) {
            this.addPossibleTile(-2, -2);
            this.addPossibleTile(-2, 0);
            this.addPossibleTile(-2, 2);
            this.addPossibleTile(0, -2);
            this.addPossibleTile(0, 2);
            this.addPossibleTile(2, -2);
            this.addPossibleTile(2, 0);
            this.addPossibleTile(2, 2);

            if (getStackInSlot(0) == null || tiles.isEmpty()) {
                reset();
                return;
            }
            ISpell spell = MagicHandler.getSpellLazy(spellID);
            if (spell != null) {

                if (!this.infoCollected)
                    this.matchRecipe();
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
                                tag.setInteger(ModLibs.SPELL_ID, spellID);
                                stack.getTagCompound().setTag(ModLibs.RING_TAG, tag);
                            }
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
        tiles.clear();
    }

    public void addPossibleTile(int x, int z) {
        TileEntity tile = worldObj.getTileEntity(xCoord + x, yCoord, zCoord + z);
        if (tile != null && tile instanceof TileItemAltar) {
            if (!tiles.contains(tile))
                tiles.add((TileItemAltar) tile);
        }
    }

    public void simpleReset() {
        this.spellID = -1;
        this.timer = 0;
        this.tiles.clear();
        for (TileItemAltar tile : this.tilesToGetFrom) {
            tile.setInventorySlotContents(0, null);
        }
        this.tilesToGetFrom.clear();
        this.startInfusion = false;
        this.infoCollected = false;
        this.tempInventory.clear();
    }

    public void reset() {
        this.spellID = -1;
        this.timer = 0;
        this.tiles.clear();
        this.tilesToGetFrom.clear();
        this.startInfusion = false;
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
        worldObj.playSound(xCoord, yCoord, zCoord, "random.fizz", 0.5F, 0.4F / ((float) worldObj.rand.nextDouble() * 0.4F + 0.8F), false);
    }

    public void matchRecipe() {
        Object[] ingredients = MagicHandler.getMagicHandler().getSpellRecipe(spellID);
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
                    for (int j = 0; j < itemList.size(); j++) {
                        Object obj = itemList.get(j);
                        if (obj != null) {
                            if (obj instanceof ItemStack) {
                                ItemStack ingredient = (ItemStack) obj;
                                if (tileItem.getItem() == ingredient.getItem() && tileItem.getItemDamage() == ingredient.getItemDamage()) {
                                    itemList.remove(j);
                                    tilesToGetFrom.add(tile);
                                }
                            }
                            if (obj instanceof String) {
                                String oreName = (String) obj;
                                List<ItemStack> oreList = OreDictionary.getOres(oreName);
                                if (oreList != null) {
                                    oreLoop:
                                    for (ItemStack ingredient : oreList) {
                                        if (tileItem.getItem() == ingredient.getItem() && tileItem.getItemDamage() == ingredient.getItemDamage()) {
                                            itemList.remove(j);
                                            tilesToGetFrom.add(tile);
                                            break oreLoop;
                                        }
                                    }
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

    public void startInfusion(EntityPlayer player, int spellId) {

        if (this.hasBeenInfused(getStackInSlot(0))) {
            if (player.experienceTotal >= 1205 || player.capabilities.isCreativeMode) {
                if (!player.capabilities.isCreativeMode) {
                    player.addExperienceLevel(-35);
                    player.getCurrentEquippedItem().stackSize--;
                }
                this.spellID = spellId;
                this.startInfusion = true;
            } else {
                if (!worldObj.isRemote)
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(ModLibs.NO_EXP_PLUS)));
            }
        } else {
            if (player.experienceTotal >= 825 || player.capabilities.isCreativeMode) {
                if (!player.capabilities.isCreativeMode) {
                    player.addExperienceLevel(-30);
                    player.getCurrentEquippedItem().stackSize--;
                }
                this.spellID = spellId;
                this.startInfusion = true;
            } else {
                if (!worldObj.isRemote)
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(ModLibs.NO_EXP)));
            }
        }
    }
}

package net.lomeli.ring.block.tile;

import java.util.ArrayList;
import java.util.List;

import net.lomeli.ring.item.ItemMagicRing;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.magic.ISpell;
import net.lomeli.ring.magic.MagicHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

public class TileAltar extends TileEntity implements IInventory {

    private ItemStack[] inventory;
    private int timer, spellID;
    private boolean startInfusion, infoCollected;
    private List<TileItemAltar> tiles = new ArrayList<TileItemAltar>(), tilesToGetFrom = new ArrayList<TileItemAltar>();
    private List<ItemStack> tempInventory = new ArrayList<ItemStack>();

    public TileAltar() {
        this.inventory = new ItemStack[1];
    }

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

            if (inventory[0] == null || tiles.isEmpty()) {
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
                            ItemStack stack = inventory[0];
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
                            simpleReset();
                        }
                    }
                }
            }
        }
        tiles.clear();
    }

    public void spawnEffects() {
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("flame", xCoord + 0.5, yCoord + 1.1, zCoord + 0.5, 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 1.1, zCoord + worldObj.rand.nextFloat(), 0, 0, 0);
        worldObj.spawnParticle("smoke", xCoord + 0.5, yCoord + 1.1, zCoord + 0.5, 0, 0, 0);
    }

    public void addPossibleTile(int x, int z) {
        TileEntity tile = worldObj.getTileEntity(xCoord + x, yCoord, zCoord + z);
        if (tile != null && tile instanceof TileItemAltar) {
            if (!tiles.contains((TileItemAltar) tile))
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
                    itemList.add((String) obj);
                }else {
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
                                    oreLoop: for (ItemStack ingredient : oreList) {
                                        if (tileItem.getItem() == ingredient.getItem() && tileItem.getItemDamage() == ingredient.getItemDamage()) {
                                            itemList.remove(j);
                                            tilesToGetFrom.add(tile);
                                            break oreLoop;
                                        }
                                    }
                                }
                            }
                        }else
                            itemList.remove(j);
                    }
                }
            }
        }

        if (itemList.isEmpty())
            this.infoCollected = true;
    }

    public boolean hasBeenInfused(ItemStack stack) {
        if (stack != null) {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
            return tag.hasKey(ModLibs.SPELL_ID);
        }
        return true;
    }

    public void startInfusion(EntityPlayer player, int spellId) {

        if (this.hasBeenInfused(inventory[0])) {
            if (player.experienceTotal >= 1205 || player.capabilities.isCreativeMode) {
                if (!player.capabilities.isCreativeMode) {
                    player.addExperienceLevel(-35);
                    player.getCurrentEquippedItem().stackSize--;
                }
                this.spellID = spellId;
                this.startInfusion = true;
            }else {
                if (!worldObj.isRemote)
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(ModLibs.NO_EXP_PLUS)));
            }
        }else {
            if (player.experienceTotal >= 825 || player.capabilities.isCreativeMode) {
                if (!player.capabilities.isCreativeMode) {
                    player.addExperienceLevel(-30);
                    player.getCurrentEquippedItem().stackSize--;
                }
                this.spellID = spellId;
                this.startInfusion = true;
            }else {
                if (!worldObj.isRemote)
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(ModLibs.NO_EXP)));
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return inventory[var1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        if (this.inventory[par1] != null) {
            ItemStack itemstack;

            if (this.inventory[par1].stackSize <= par2) {
                itemstack = this.inventory[par1];
                this.inventory[par1] = null;
                this.markDirty();
                return itemstack;
            }else {
                itemstack = this.inventory[par1].splitStack(par2);

                if (this.inventory[par1].stackSize == 0) {
                    this.inventory[par1] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        ItemStack stack = this.getStackInSlot(var1);
        inventory[var1] = null;
        this.markDirty();
        return stack;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        inventory[var1] = var2;
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return ModLibs.ALTAR;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return inventory[var1] == null;
    }

    public void read(NBTTagCompound nbt) {
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.length)
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        read(nbt);
    }

    public void write(NBTTagCompound nbt) {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag("Items", nbttaglist);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        write(nbt);
    }

    @Override
    public Packet getDescriptionPacket() {
        S35PacketUpdateTileEntity packet = (S35PacketUpdateTileEntity) super.getDescriptionPacket();
        NBTTagCompound dataTag = packet != null ? packet.func_148857_g() : new NBTTagCompound();
        write(dataTag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, dataTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt != null ? pkt.func_148857_g() : new NBTTagCompound();
        read(tag);
    }
}

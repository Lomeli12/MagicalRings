package net.lomeli.modjam4.item;

import java.awt.Color;

import net.lomeli.modjam4.lib.ModLibs;
import net.lomeli.modjam4.magic.ISpell;
import net.lomeli.modjam4.magic.MagicHandler;
import net.lomeli.modjam4.network.PacketHandler;
import net.lomeli.modjam4.network.PacketUpdatePlayerMP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMagicRing extends ItemRings {
    @SideOnly(Side.CLIENT)
    private IIcon rl1, rl2, gem;

    public ItemMagicRing(String texture) {
        super(texture);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    public int getLayer1RGB(NBTTagCompound nbt) {
        return nbt.getInteger(ModLibs.L1RGB);
    }

    public int getLayer2RGB(NBTTagCompound nbt) {
        return nbt.getInteger(ModLibs.L2RGB);
    }

    public boolean isEdible(NBTTagCompound nbt) {
        return nbt.getBoolean(ModLibs.EDIBLE);
    }

    public boolean hasGem(NBTTagCompound nbt) {
        return nbt.getBoolean(ModLibs.HAS_GEM);
    }

    public int getGemRGB(NBTTagCompound nbt) {
        return nbt.getInteger(ModLibs.GEM_RGB);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        rl1 = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":ring/ringEdge");
        rl2 = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":ring/ringInner");
        gem = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":ring/ringGem");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        this.useRing(stack, player, world, (int) player.posX, (int) player.posY, (int) player.posZ, 0, 0, 0, 0);
        if (stack.getTagCompound() == null) {
            stack.stackTagCompound = new NBTTagCompound();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger(ModLibs.L1RGB, Color.MAGENTA.getRGB());
            tag.setInteger(ModLibs.L2RGB, Color.DARK_GRAY.getRGB());
            tag.setBoolean(ModLibs.HAS_GEM, true);
            tag.setInteger(ModLibs.GEM_RGB, Color.YELLOW.getRGB());
            stack.getTagCompound().setTag(ModLibs.RING_TAG, tag);
        }
        if (!player.getEntityData().hasKey(ModLibs.PLAYER_DATA)) {
            PacketHandler.sendToServer(new PacketUpdatePlayerMP(player, 0, ModLibs.BASE_MP));
            PacketHandler.sendToAll(new PacketUpdatePlayerMP(player, 0, ModLibs.BASE_MP));
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return this.useRing(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    public boolean useRing(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (stack.getTagCompound() != null) {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
            if (tag != null) {
                int spellID = tag.getInteger(ModLibs.SPELL_ID);
                ISpell spell = MagicHandler.getSpellLazy(0);// MagicHandler.getSpellLazy(spellID);
                if (spell != null) {
                    if (MagicHandler.canUse(player, spell.cost())) {
                        MagicHandler.modifyPlayerMP(player, -spell.cost());
                        return spell.activateSpell(world, player, x, y, z, side, hitX, hitY, hitZ);
                    }
                }else {
                    if (this.isEdible(tag)) {
                        world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                        stack.stackSize--;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass) {
        if (renderPass == 1)
            return rl2;
        else if (renderPass == 2) {
            if (stack.getTagCompound() != null && this.hasGem(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG)))
                return gem;
            else
                return this.blankIcon;
        }else
            return rl1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (stack.getTagCompound() == null)
            return Color.LIGHT_GRAY.getRGB();
        else if (renderPass == 2) {
            if (this.hasGem(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG)))
                return this.getGemRGB(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG));
            else
                return Color.CYAN.getRGB();
        }else {
            if (renderPass == 1)
                return this.getLayer2RGB(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG));
            else
                return this.getLayer1RGB(stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getTagCompound() != null) {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(ModLibs.RING_TAG);
            if (tag != null) {
                int spellID = tag.getInteger(ModLibs.SPELL_ID);
                ISpell spell = MagicHandler.getSpellLazy(0);
                if (spell != null)
                    return StatCollector.translateToLocal(spell.getUnlocalizedName()) + " " + super.getItemStackDisplayName(stack);
            }
        }
        return super.getItemStackDisplayName(stack);
    }
}

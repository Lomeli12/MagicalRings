package net.lomeli.ring.magic.spells;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.ring.Rings;
import net.lomeli.ring.api.interfaces.IPlayerSession;
import net.lomeli.ring.api.interfaces.ISpell;
import net.lomeli.ring.client.entity.EntityManaFX;
import net.lomeli.ring.lib.ModLibs;
import net.lomeli.ring.network.PacketAllowFlying;

public class SwiftWind implements ISpell {
    private int tick;

    @Override
    public boolean useOnBlock(World world, EntityPlayer player, IPlayerSession session, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int boost, int cost) {
        return false;
    }

    @Override
    public void onUse(World world, EntityPlayer player, IPlayerSession session, ItemStack stack, int boost, int cost) {

    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {

    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity, IPlayerSession session) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!player.capabilities.isCreativeMode) {
                player.capabilities.isFlying = false;
                player.capabilities.allowFlying = false;
                Rings.pktHandler.sendToPlayerAndServer(new PacketAllowFlying(player, false), player);
            }
        }
    }

    @Override
    public void applyToMob(EntityPlayer player, IPlayerSession session, Entity target, int boost, int cost) {
    }

    @Override
    public void onUpdateTick(ItemStack stack, World world, Entity entity, IPlayerSession session, int par4, boolean par5, int boost, int cost, boolean enabled) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (session != null) {
                if (!player.capabilities.isCreativeMode) {
                    boolean canFlyNow = player.capabilities.allowFlying;
                    if (session.hasEnoughMana(cost())) {
                        if (player.capabilities.allowFlying == false)
                            player.capabilities.allowFlying = true;

                        if (++tick >= 11) {
                            if (player.capabilities.isFlying)
                                session.adjustMana(-cost(), false);
                            else if (player.fallDistance >= 4F)
                                session.adjustMana(-MathHelper.floor_float(player.fallDistance / 4), false);
                            tick = 0;
                        }
                    } else {
                        if (player.capabilities.allowFlying == true)
                            player.capabilities.allowFlying = false;
                        if (player.capabilities.isFlying == true)
                            player.capabilities.isFlying = false;
                    }

                    if (world.isRemote && player.capabilities.isFlying && (player.motionX != 0 || player.motionZ != 0 || player.motionY != 0))
                        spawnParticle(world, player, player.posX, player.posY, player.posZ);

                    if (canFlyNow != player.capabilities.allowFlying)
                        Rings.pktHandler.sendToServer(new PacketAllowFlying(player, player.capabilities.allowFlying));
                }
            }
        }
    }

    @Override
    public String getUnlocalizedName() {
        return ModLibs.WING;
    }

    @Override
    public int cost() {
        return 2;
    }

    @Override
    public String getSpellDescription() {
        return ModLibs.WING + "Desc";
    }

    public void spawnParticle(World world, EntityLivingBase entity, double x, double y, double z) {
        Minecraft mc = Minecraft.getMinecraft();
        int rgb = Color.CYAN.getRGB();
        float r = (rgb >> 16 & 255) / 255.0F;
        float g = (rgb >> 8 & 255) / 255.0F;
        float b = (rgb & 255) / 255.0F;
        boolean color = world.rand.nextBoolean();
        mc.effectRenderer.addEffect(new EntityManaFX(world, x - 0.5 + world.rand.nextFloat(), y - (entity.getEyeHeight()* 10), z - 0.5 + world.rand.nextFloat(), 0.325f, color ? r : 1f, color ? g : 1f, color ? b : 1f, 0.75f, 4));
    }
}

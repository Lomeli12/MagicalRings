package net.lomeli.ring.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

import net.lomeli.ring.lib.ModLibs;

public class ItemGhostSword extends ItemSword {

    public ItemGhostSword() {
        super(ToolMaterial.IRON);
        this.setMaxDamage(100);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(ModLibs.MOD_ID.toLowerCase() + ":ghostSword");
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
        if (world.rand.nextBoolean()) {
            if (entity instanceof EntityLivingBase && !world.isRemote) {
                if (stack.getItemDamage() != stack.getMaxDamage())
                    stack.damageItem(1, (EntityLivingBase) entity);
            }
        }
    }

    @Override
    public Item setUnlocalizedName(String name) {
        return super.setUnlocalizedName(ModLibs.MOD_ID.toLowerCase() + "." + name);
    }
}

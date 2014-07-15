package net.lomeli.ring.core.helper;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.ring.Rings;

public class IMCHelper {
    private static final String ADD_GEM = "addGem";
    private static final String ADD_MATERIAL = "addMaterial";

    public static void processMessages(List<FMLInterModComms.IMCMessage> messageList) {
        for (FMLInterModComms.IMCMessage message : messageList) {
            if (message.key.equalsIgnoreCase(ADD_GEM)) {
                LogHelper.info("Adding gem from mod " + message.getSender());
                if (message.isNBTMessage())
                    addGemFromNBT(message.getNBTValue());
            } else if (message.key.equalsIgnoreCase(ADD_MATERIAL)) {
                LogHelper.info("Adding ring material from mod " + message.getSender());
                if (message.isNBTMessage())
                    addMaterialFromNBT(message.getNBTValue());
            }
        }
    }

    private static void addGemFromNBT(NBTTagCompound tag) {
        if (tag != null) {
            int boost = tag.getInteger("Boost");
            int color = tag.getInteger("ColorRGB");
            ItemStack stack;
            if (tag.hasKey("OreDicName")) {
                String oreDictName = tag.getString("OreDicName");
                if (oreDictName != null)
                    Rings.proxy.ringMaterials.registerGem(oreDictName, color, boost);
                return;
            } else if (tag.hasKey("GameRegistryName")) {
                String gameRegName = tag.getString("GameRegistryName");
                String modID = tag.getString("modID");
                stack = GameRegistry.findItemStack(modID, gameRegName, 1);
            } else
                stack = getItemFromNBT(tag);
            if (stack != null)
                Rings.proxy.ringMaterials.registerGem(stack, color, boost);
        }
    }

    private static void addMaterialFromNBT(NBTTagCompound tag) {
        if (tag != null) {
            int boost = tag.getInteger("Boost");
            int color = tag.getInteger("ColorRGB");
            ItemStack stack;
            if (tag.hasKey("OreDicName")) {
                String oreDictName = tag.getString("OreDicName");
                if (oreDictName != null)
                    Rings.proxy.ringMaterials.registerMaterial(oreDictName, color, boost);
                return;
            } else if (tag.hasKey("GameRegistryName")) {
                String gameRegName = tag.getString("GameRegistryName");
                String modID = tag.getString("modID");
                stack = GameRegistry.findItemStack(modID, gameRegName, 1);
            } else
                stack = getItemFromNBT(tag);
            if (stack != null)
                Rings.proxy.ringMaterials.registerMaterial(stack, color, boost);
        }
    }

    private static ItemStack getItemFromNBT(NBTTagCompound tag) {
        if (tag != null) {
            ItemStack stack = null;
            int id = tag.getInteger("id");
            int count = tag.getInteger("Count");
            int damage = tag.getInteger("Damage");
            if (damage < 0)
                damage = 0;
            Item item = Item.getItemById(id);
            if (item != null) {
                stack = new ItemStack(item, count, damage);
                if (tag.hasKey("tag", 10))
                    stack.stackTagCompound = tag.getCompoundTag("tag");
            }
            return stack;
        }
        return null;
    }
}

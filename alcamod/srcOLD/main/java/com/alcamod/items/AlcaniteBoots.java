package com.alcamod.items;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class AlcaniteBoots extends ArmorItem {
    public AlcaniteBoots() {
        super(AlcaniteArmorMaterial.INSTANCE, EquipmentSlotType.FEET, new Properties().tab(ItemGroup.TAB_COMBAT));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        return "alcamod:textures/models/armor/alcanite_layer_1.png";
    }
}

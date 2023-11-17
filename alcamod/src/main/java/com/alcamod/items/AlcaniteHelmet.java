package com.alcamod.items;
        import net.minecraft.entity.Entity;
        import net.minecraft.inventory.EquipmentSlotType;
        import net.minecraft.item.ArmorItem;
        import net.minecraft.item.IArmorMaterial;
        import net.minecraft.item.ItemStack;
        import net.minecraft.item.ItemGroup;
public class AlcaniteHelmet extends ArmorItem {

    public AlcaniteHelmet() {
        super(AlcaniteArmorMaterial.INSTANCE, EquipmentSlotType.HEAD, new Properties().tab(ItemGroup.TAB_COMBAT));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        return "alcamod:textures/models/armor/alcanite_layer_1.png";
    }
}

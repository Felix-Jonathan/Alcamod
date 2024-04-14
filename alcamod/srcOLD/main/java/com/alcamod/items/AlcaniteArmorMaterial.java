package com.alcamod.items;

import com.alcamod.Alcamod;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum AlcaniteArmorMaterial implements IArmorMaterial {
    INSTANCE;

    private static final int[] MAX_DAMAGE_ARRAY = new int[]{5, 9, 12, 5};
    private static final int DURABILITY = 4942;
    private static final int[] PROTECTION_VALUES = new int[]{5, 9, 12, 5};

    public int getDurabilityForSlot(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * DURABILITY;
    }

    public int getDefenseForSlot(EquipmentSlotType slotIn) {
        return PROTECTION_VALUES[slotIn.getIndex()];
    }

    public int getEnchantmentValue() {
        return 15;
    }

    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_DIAMOND;
    }

    public Ingredient getRepairIngredient() {
        // Replace with Alcanite item
        return Ingredient.of(Alcamod.Alcanite.get());
    }

    @Override
    public String getName() {
        return "alcanite";
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 2;
    }



}

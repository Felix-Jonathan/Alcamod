package com.alcamod.material;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.function.Supplier;

public enum AlcaniteArmorMaterial implements ArmorMaterial {
    ALCANITE("alcanite", new int[]{13, 15, 16, 11}, new int[]{16, 40, 48, 16}, 50, SoundEvents.ARMOR_EQUIP_GENERIC, 4.0F, 2.0F, () -> Ingredient.of(Items.DIAMOND));

    private final int[] ARMOR_VALUES;
    private final String name;
    private final int[] durabilityForEachSlot;
    private final int[] defensePointsForEachSlot;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    AlcaniteArmorMaterial(String name, int[] durabilityForEachSlot, int[] defensePointsForEachSlot, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.ARMOR_VALUES=defensePointsForEachSlot;
        this.durabilityForEachSlot = durabilityForEachSlot;
        this.defensePointsForEachSlot = defensePointsForEachSlot;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        switch (type) {
            case HELMET: return 2048;
            case CHESTPLATE: return 4096;
            case LEGGINGS: return 4096;
            case BOOTS: return 2048;
            default: return 0;
        }
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        switch (type) {
            case HELMET: return this.ARMOR_VALUES[EquipmentSlot.HEAD.getIndex()];
            case CHESTPLATE: return this.ARMOR_VALUES[EquipmentSlot.CHEST.getIndex()];
            case LEGGINGS: return this.ARMOR_VALUES[EquipmentSlot.LEGS.getIndex()];
            case BOOTS: return this.ARMOR_VALUES[EquipmentSlot.FEET.getIndex()];
            default: return 0;
        }
    }
    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        // The name "alcanite" will be used to form the resource location for textures,
        // e.g., "alcamod:textures/models/armor/alcanite_layer_1.png" and "alcamod:textures/models/armor/alcanite_layer_2.png".
        return "alcamod:alcanite";
    }
    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}


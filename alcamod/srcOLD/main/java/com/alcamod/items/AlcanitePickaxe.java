package com.alcamod.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;

public class AlcanitePickaxe extends PickaxeItem {
    public AlcanitePickaxe(AlcaniteTier tier, int attackDamage, float attackSpeed, Item.Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }
}

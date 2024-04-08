package com.alcamod.entities;

import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import java.util.function.Supplier;
import net.minecraft.item.ItemGroup;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundNBT;

public class ModSpawnEggItem extends SpawnEggItem {
    private final Supplier<EntityType<?>> entityTypeSupplier;

    public ModSpawnEggItem(Supplier<EntityType<?>> entityTypeSupplier, int primaryColor, int secondaryColor) {
        super(null, primaryColor, secondaryColor, new Item.Properties().tab(ItemGroup.TAB_MISC));
        this.entityTypeSupplier = entityTypeSupplier;
    }

    @Override
    public EntityType<?> getType(@Nullable CompoundNBT nbt) {
        return entityTypeSupplier.get();
    }
}

package com.alcamod.entities.phantomknight;

import com.alcamod.Alcamod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Alcamod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PhantomKnightEntity extends ZombieEntity {

    public PhantomKnightEntity(EntityType<? extends ZombieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(Alcamod.PHANTOM_KNIGHT_ENTITY.get(), PhantomKnightEntity.createAttributes().build());
    }

}

package com.alcamod.entities.phantomknight;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.world.World;

public class PhantomKnight extends ZombieEntity {

    public PhantomKnight(EntityType<? extends ZombieEntity> type, World worldIn) {
        super(type, worldIn);
        // Ici, vous pouvez initialiser des éléments spécifiques à PhantomKnight
    }

    // Vous pouvez surcharger des méthodes ici pour personnaliser le comportement

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        // Ajoutez d'autres objectifs ici
    }


    // Ajoutez d'autres méthodes personnalisées ou surcharges si nécessaire
}

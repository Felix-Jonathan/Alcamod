package com.alcamod.client.renderer;

import com.alcamod.entities.phantomknight.PhantomKnightEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.util.ResourceLocation;

public class PhantomKnightRenderer extends MobRenderer<PhantomKnightEntity, ZombieModel<PhantomKnightEntity>> {

    // Ensure this path is correct for your texture file location
    private static final ResourceLocation TEXTURE = new ResourceLocation("alcamod",
            "textures/entity/phantom_knight.png");

    public PhantomKnightRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ZombieModel<PhantomKnightEntity>(0.0F, false), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(PhantomKnightEntity entity) {
        return TEXTURE;
    }
}

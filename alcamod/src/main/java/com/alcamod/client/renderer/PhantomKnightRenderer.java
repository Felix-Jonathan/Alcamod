package com.alcamod.client.renderer;

import com.alcamod.entities.phantomknight.PhantomKnightEntity;
import com.alcamod.entities.phantomknight.PhantomKnightModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class PhantomKnightRenderer extends MobRenderer<PhantomKnightEntity, PhantomKnightModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alcamod",
            "textures/entity/phantom_knight.png");

    public PhantomKnightRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PhantomKnightModel(), 0.5F); // Assuming PhantomKnightModel has a no-arg constructor
    }

    @Override
    public ResourceLocation getTextureLocation(PhantomKnightEntity entity) {
        return TEXTURE;
    }
}

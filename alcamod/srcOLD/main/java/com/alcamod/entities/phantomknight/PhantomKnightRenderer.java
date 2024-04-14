package com.alcamod.entities.phantomknight;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class PhantomKnightRenderer extends MobRenderer<PhantomKnight, PhantomKnightModel<PhantomKnight>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("alcamod", "textures/entity/phantom_knight.png");

    public PhantomKnightRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PhantomKnightModel<>(), 0.5f); // La taille de l'ombre, ajustez selon vos besoins
    }

    @Override
    public ResourceLocation getTextureLocation(PhantomKnight p_110775_1_) {

        return TEXTURE;
    }


}

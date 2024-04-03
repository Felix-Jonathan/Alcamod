package com.alcamod.entities.phantomknight;

// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import com.alcamod.entities.phantomknight.PhantomKnightEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public class PhantomKnightModel extends EntityModel<Entity> {
	private final ModelRenderer body;
	private final ModelRenderer upper;
	private final ModelRenderer head;
	private final ModelRenderer casque;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer casque2;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer arm0;
	private final ModelRenderer epee;
	private final ModelRenderer epee_r1;
	private final ModelRenderer arm1;
	private final ModelRenderer arm1_r1;
	private final ModelRenderer bouclier;
	private final ModelRenderer lower;
	private final ModelRenderer leg0;
	private final ModelRenderer leg1;

	public PhantomKnightModel() {
		texWidth = 128;
		texHeight = 128;

		body = new ModelRenderer(this);
		body.setPos(0.0F, -7.0F, 0.0F);

		upper = new ModelRenderer(this);
		upper.setPos(0.0F, -12.0F, -3.0F);
		body.addChild(upper);
		upper.texOffs(0, 0).addBox(-9.0F, 10.0F, -3.0F, 18.0F, 12.0F, 11.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, 12.0F, 1.0F);
		upper.addChild(head);
		head.texOffs(58, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, 0.0F, false);

		casque = new ModelRenderer(this);
		casque.setPos(0.0F, 31.0F, 2.0F);
		head.addChild(casque);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(0.0F, 0.0F, 0.0F);
		casque.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, 0.9599F);
		cube_r1.texOffs(47, 4).addBox(-35.2F, -33.0F, -4.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(0.0F, 0.0F, 0.0F);
		casque.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, -1.0908F);
		cube_r2.texOffs(75, 18).addBox(43.9F, -17.0F, -4.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setPos(0.0F, 0.0F, 0.0F);
		casque.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.0F, -0.48F);
		cube_r3.texOffs(20, 77).addBox(21.0F, -35.0F, -4.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

		casque2 = new ModelRenderer(this);
		casque2.setPos(0.0F, 31.0F, 2.0F);
		head.addChild(casque2);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setPos(0.0F, 0.0F, 0.0F);
		casque2.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.0F, -0.9599F);
		cube_r4.texOffs(47, 0).addBox(29.2F, -33.0F, -4.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r5 = new ModelRenderer(this);
		cube_r5.setPos(0.0F, 0.0F, 0.0F);
		casque2.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 1.0908F);
		cube_r5.texOffs(56, 62).addBox(-49.9F, -17.0F, -4.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r6 = new ModelRenderer(this);
		cube_r6.setPos(0.0F, 0.0F, 0.0F);
		casque2.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, 0.0F, 0.48F);
		cube_r6.texOffs(70, 74).addBox(-27.0F, -35.0F, -4.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

		arm0 = new ModelRenderer(this);
		arm0.setPos(-11.0F, 12.0F, 3.0F);
		upper.addChild(arm0);
		arm0.texOffs(38, 23).addBox(-2.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F, false);

		epee = new ModelRenderer(this);
		epee.setPos(11.0F, 31.0F, 0.0F);
		arm0.addChild(epee);

		epee_r1 = new ModelRenderer(this);
		epee_r1.setPos(0.0F, 0.0F, 0.0F);
		epee.addChild(epee_r1);
		setRotationAngle(epee_r1, 0.9599F, 0.0F, 0.0F);
		epee_r1.texOffs(0, 46).addBox(-11.0F, -34.5F, -1.0F, 0.0F, 25.0F, 10.0F, 0.0F, false);
		epee_r1.texOffs(62, 74).addBox(-12.0F, -9.5F, 2.0F, 2.0F, 5.0F, 4.0F, 0.0F, false);
		epee_r1.texOffs(62, 62).addBox(-13.0F, -11.5F, -1.0F, 4.0F, 2.0F, 10.0F, 0.0F, false);

		arm1 = new ModelRenderer(this);
		arm1.setPos(0.0F, 12.0F, 3.0F);
		upper.addChild(arm1);

		arm1_r1 = new ModelRenderer(this);
		arm1_r1.setPos(0.0F, 31.0F, 0.0F);
		arm1.addChild(arm1_r1);
		setRotationAngle(arm1_r1, -1.5708F, 0.0F, 0.0F);
		arm1_r1.texOffs(42, 62).addBox(9.0F, 0.5F, -33.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);

		bouclier = new ModelRenderer(this);
		bouclier.setPos(11.0F, 0.0F, -2.0F);
		arm1.addChild(bouclier);
		bouclier.texOffs(0, 23).addBox(-10.0F, -12.0F, -15.0F, 17.0F, 31.0F, 2.0F, 0.0F, false);

		lower = new ModelRenderer(this);
		lower.setPos(0.0F, 10.0F, 0.0F);
		body.addChild(lower);
		lower.texOffs(58, 39).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 5.0F, 6.0F, 0.5F, false);
		lower.texOffs(49, 50).addBox(-5.5F, 0.0F, -5.0F, 11.0F, 3.0F, 9.0F, 0.5F, false);

		leg0 = new ModelRenderer(this);
		leg0.setPos(-4.0F, 8.0F, 0.0F);
		lower.addChild(leg0);
		leg0.texOffs(58, 18).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, 0.0F, false);

		leg1 = new ModelRenderer(this);
		leg1.setPos(5.0F, 8.0F, 0.0F);
		lower.addChild(leg1);
		leg1.texOffs(20, 56).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		// previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

}
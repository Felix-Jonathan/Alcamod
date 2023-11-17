package com.alcamod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DailyGui extends ContainerScreen<DailyContainer> {

    private static final ResourceLocation GUI = new ResourceLocation("alcamod", "textures/gui/interfacejournalieresansbg.png");

    public DailyGui(DailyContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        // Vous devrez ajuster ces dimensions en fonction de la taille de votre image de fond et de la mise en page de votre GUI
        this.imageWidth = 250; // Largeur de l'image de fond
        this.imageHeight = 250; // Hauteur de l'image de fond
        // Ajuster la taille de l'inventaire si nécessaire
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(GUI);
        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;
        blit(matrixStack, guiLeft, guiTop, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        // Suppression des étiquettes pour une interface épurée
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    private void renderScaledItem(MatrixStack matrixStack, Slot slot, float scale) {
        matrixStack.pushPose();
        matrixStack.translate(slot.x, slot.y, 0);
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-8 * scale, -8 * scale, 0);
        this.itemRenderer.renderAndDecorateItem(slot.getItem(), 0, 0);
        matrixStack.popPose();
    }

    public static void openGUI() {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            PlayerInventory playerInventory = player.inventory;
            ITextComponent title = new TranslationTextComponent("container.dailygui");
            Minecraft.getInstance().setScreen(new DailyGui(new DailyContainer(0, playerInventory), playerInventory, title));
        }
    }
}

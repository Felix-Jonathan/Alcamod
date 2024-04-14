package com.alcamod.client.gui;

import com.alcamod.Alcamod;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RewardScreen extends Screen {
    
    private static final ResourceLocation IMAGE_LOCATION = new ResourceLocation(Alcamod.MODID, "textures/gui/dailyrewards.png");

    public RewardScreen() {
        super(Component.literal("Image Screen"));
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, IMAGE_LOCATION);
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int scaledWidth = screenWidth;
        int scaledHeight = (int) ((float) screenWidth * (1080 / (float) 1920));
        int yOffset = (screenHeight - scaledHeight) / 2;
        guiGraphics.blit(IMAGE_LOCATION, 0, yOffset, 0, 0, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // 256 est le code de la touche ÉCHAP
            Minecraft.getInstance().setScreen(null); // Ferme l'écran
            return true; // Consomme l'événement
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
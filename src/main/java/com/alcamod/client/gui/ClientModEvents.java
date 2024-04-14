package com.alcamod.client.gui;

import java.util.UUID;

import com.alcamod.Alcamod;
import com.alcamod.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Alcamod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientModEvents {

    private static boolean imageDisplayed = false; // Ajout d'un flag pour contrôler l'affichage de l'image

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        
        Player player = event.getEntity();
        try {
            Config.handlePlayerLogin(player.getUUID());
        } catch (Exception e) {
            e.printStackTrace(); // Pour le débogage
        }
        if (!imageDisplayed) {
            Minecraft.getInstance().setScreen(new RewardScreen());
            imageDisplayed = true; // Met à jour le flag après affichage
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        imageDisplayed = false; // Réinitialise le flag lors de la déconnexion du joueur
    }
}
package com.alcamod.client.gui;


import com.alcamod.Alcamod;
import com.alcamod.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Alcamod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientModEvents {

    private static boolean imageDisplayed = false; // Ajout d'un flag pour contrôler l'affichage de l'image

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        
        Player player = event.getEntity();
        //Do Some Stuff GLHF
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        imageDisplayed = false; // Réinitialise le flag lors de la déconnexion du joueur
    }
}
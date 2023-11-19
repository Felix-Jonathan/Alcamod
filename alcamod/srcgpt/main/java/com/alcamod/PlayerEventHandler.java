package com.alcamod;

import com.alcamod.gui.DailyContainer;
import com.alcamod.gui.DailyGui;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.util.concurrent.AbstractEventExecutor;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Mod.EventBusSubscriber
public class PlayerEventHandler {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static class ConfigData {


        private List<String> rewards;
        private List<String> topRewards;

        // Getters
        public List<String> getRewards() {
            return rewards;
        }

        public List<String> getTopRewards() {
            return topRewards;
        }
    }

    private static void createPlayerFile(UUID playerUUID) {
        try {
            Path playerDataPath = Paths.get("config/alcamod/dailyRewards/playerData");
            Files.createDirectories(playerDataPath);

            Path playerFile = playerDataPath.resolve(playerUUID.toString() + ".json");
            if (!Files.exists(playerFile)) {
                Files.createFile(playerFile);
                writePlayerRewards(playerFile);
            }
        } catch (Exception e) {
            // Gérer l'exception
        }
    }

    private static void writePlayerRewards(Path playerFile) {
        try {
            ConfigData configData = readConfigData();
            List<String> playerRewards = new ArrayList<>();
            Random random = new Random();

            // Ajouter 7 récompenses aléatoires
            for (int i = 0; i < 7; i++) {
                String reward = configData.getRewards().get(random.nextInt(configData.getRewards().size()));
                playerRewards.add(reward);
            }

            // Ajouter 1 top récompense aléatoire
            String topReward = configData.getTopRewards().get(random.nextInt(configData.getTopRewards().size()));
            playerRewards.add(topReward);

            // Ajouter 6 autres récompenses aléatoires
            for (int i = 0; i < 6; i++) {
                String reward = configData.getRewards().get(random.nextInt(configData.getRewards().size()));
                playerRewards.add(reward);
            }

            topReward = configData.getTopRewards().get(random.nextInt(configData.getTopRewards().size()));
            playerRewards.add(topReward);

            JsonObject playerDataJson = new JsonObject();
            playerDataJson.add("rewards", new Gson().toJsonTree(playerRewards));
            playerDataJson.addProperty("lastClickDate", ""); // Date vide lors de la première création

            // Écriture dans le fichier
            Gson gson = new Gson();
            Files.write(playerFile, Collections.singleton(gson.toJson(playerDataJson)));
        } catch (Exception e) {
            // Gérer l'exception
        }
    }


    private static ConfigData readConfigData() {
        try {
            Path configJson = Paths.get("config/alcamod/dailyRewards/config.json");
            Gson gson = new Gson();
            String json = new String(Files.readAllBytes(configJson));
            return gson.fromJson(json, ConfigData.class);
        } catch (Exception e) {
            // Gérer l'exception
            return new ConfigData(); // Retourne une instance vide pour éviter les NullPointerException
        }
    }


    @SubscribeEvent
    public static void onPlayerLogin(PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }

        UUID playerUUID = player.getUUID();
        createPlayerFile(playerUUID);

        LocalDate lastClickDate = readLastClickDate(playerUUID);
        LocalDate currentDate = LocalDate.now();

        // Vérifiez si le dernier clic n'a pas été effectué aujourd'hui
        if (!lastClickDate.equals(currentDate)) {
            // Ouvrir le GUI directement
            openDailyGui((ServerPlayerEntity) player);

        }
    }

    private static void openDailyGui(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new StringTextComponent("Daily Rewards");
            }

            @Nullable
            @Override
            public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
                return new DailyContainer(windowId, playerInventory);
            }
        }, (packetBuffer) -> {});
    }

    private static LocalDate readLastClickDate(UUID playerUUID) {
        try {
            Path playerFile = Paths.get("config/alcamod/dailyRewards/playerData", playerUUID.toString() + ".json");
            String json = new String(Files.readAllBytes(playerFile));
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            String lastClickDateString = jsonObject.get("lastClickDate").getAsString();
            return LocalDate.parse(lastClickDateString);
        } catch (Exception e) {
            e.printStackTrace();
            return LocalDate.MIN; // Retourne une date minimale en cas d'erreur
        }
    }
}
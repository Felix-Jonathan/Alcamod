package com.alcamod;

import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import com.google.gson.Gson;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


// ... Votre code existant ...

@Mod.EventBusSubscriber
public class PlayerEventHandler {

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

            // Ajouter encore 1 top récompense aléatoire
            topReward = configData.getTopRewards().get(random.nextInt(configData.getTopRewards().size()));
            playerRewards.add(topReward);

            Gson gson = new Gson();
            String json = gson.toJson(playerRewards);
            Files.write(playerFile, Collections.singleton(json));
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
        UUID playerUUID = event.getPlayer().getUUID();
        createPlayerFile(playerUUID);
    }

}
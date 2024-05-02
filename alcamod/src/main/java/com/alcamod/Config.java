package com.alcamod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.lang.reflect.Type;

@Mod.EventBusSubscriber
public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Paths.get("config/alcamod/dailyRewards/config.json");
    private static final Path PLAYER_DATA_PATH = Paths.get("config/alcamod/dailyRewards/playerData");
    private static List<String> rewards;
    private static List<String> topRewards;

    public static void init() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            if (!Files.exists(CONFIG_PATH)) {
                saveDefaultConfig();
            } else {
                loadConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadConfig() throws IOException {
        String json = Files.readString(CONFIG_PATH);
        Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
        Map<String, List<String>> configData = GSON.fromJson(json, type);
        rewards = configData.get("rewards");
        topRewards = configData.get("topRewards");
    }

    private static void saveDefaultConfig() throws IOException {
        Map<String, List<String>> defaultConfig = new HashMap<>();
        defaultConfig.put("rewards", Arrays.asList("minecraft:stone", "minecraft:dirt"));
        defaultConfig.put("topRewards", Arrays.asList("minecraft:totem_of_undying", "minecraft:heart_of_the_sea"));
        String json = GSON.toJson(defaultConfig);
        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH)) {
            writer.write(json);
        }
    }

    public static void handlePlayerLogin(UUID playerUUID) throws IOException {
        Path playerDataFile = PLAYER_DATA_PATH.resolve(playerUUID.toString() + ".json");
        Files.createDirectories(PLAYER_DATA_PATH);
        if (!Files.exists(playerDataFile)) {
            PlayerData playerData = createDefaultPlayerData(playerUUID);
            String json = GSON.toJson(playerData);
            try (BufferedWriter writer = Files.newBufferedWriter(playerDataFile)) {
                writer.write(json);
            }
        }
    }

    private static PlayerData createDefaultPlayerData(UUID playerUUID) {
        List<String> playerRewards = new ArrayList<>();
        Random random = new Random();
        IntStream.range(0, 15).forEach(i -> {
            if (i == 6 || i == 14) {
                playerRewards.add(topRewards.get(random.nextInt(topRewards.size())));
            } else {
                playerRewards.add(rewards.get(random.nextInt(rewards.size())));
            }
        });

        String lastClickDate = LocalDate.now().minusDays(1).toString();
        return new PlayerData(playerRewards, lastClickDate);
    }

    private static List<Item> convertToItems(List<String> itemNames) {
        return itemNames.stream()
                .map(ResourceLocation::new)
                .map(ForgeRegistries.ITEMS::getValue)
                .collect(Collectors.toList());
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        // This will be called when the server loads the config
        init();
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        // This will be called when the server reloads the config
        init();
    }

    private static class PlayerData {
        List<String> rewards;
        String lastClickDate;

        public PlayerData(List<String> rewards, String lastClickDate) {
            this.rewards = rewards;
            this.lastClickDate = lastClickDate;
        }
    }
}

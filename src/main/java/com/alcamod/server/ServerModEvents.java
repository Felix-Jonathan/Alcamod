package com.alcamod.server;

import com.alcamod.Alcamod;
import com.alcamod.network.RewardScreenPacketHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import java.io.Reader;
import java.lang.reflect.Type;

@Mod.EventBusSubscriber(modid = Alcamod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class ServerModEvents {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        Path filePath = Paths.get("config", "alcamod", "dailyRewards", "playerData", player.getUUID() + ".json");
        File file = filePath.toFile();

        // Crée le fichier s'il n'existe pas avec des données initiales
        if (!file.exists()) {
            createPlayerDataFile(player.getUUID(), filePath);
        }

        List<ItemStack> playerRewards = loadPlayerRewards(filePath);
        String lastClickDate = loadPlayerLastClickDate(filePath);
        RewardScreenPacketHandler.sendOpenRewardScreenPacket(player, playerRewards,lastClickDate);
    }

    private static void createPlayerDataFile(UUID playerUUID, Path path) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("lastClickDate", "2000-01-01"); // Date vide pour la dernière réclamation
            JsonArray defaultRewards = new JsonArray();
            Random random = new Random();
            String json = Files.readString(Paths.get("config/alcamod/dailyRewards/config.json"));
            Type type = new TypeToken<Map<String, List<String>>>() {
            }.getType();
            Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            Map<String, List<String>> configData = GSON.fromJson(json, type);
            List<String> rewards = configData.get("rewards");
            List<String> topRewards = configData.get("topRewards");
            IntStream.range(0, 15).forEach(i -> {
                if (i == 6 || i == 14) {
                    defaultRewards.add(topRewards.get(random.nextInt(topRewards.size())));
                } else {
                    defaultRewards.add(rewards.get(random.nextInt(rewards.size())));
                }
            });
            jsonObject.add("rewards", defaultRewards);

            // Crée les répertoires si nécessaires
            File file = path.toFile();
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(jsonObject, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<ItemStack> loadPlayerRewards(Path path) {
        List<ItemStack> rewards = new ArrayList<>();
        try {
            Gson gson = new Gson();
            try (Reader reader = new FileReader(path.toFile())) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonArray rewardsArray = json.getAsJsonArray("rewards");
                for (int i = 0; i < rewardsArray.size(); i++) {
                    String itemId = rewardsArray.get(i).getAsString();
                    ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)));
                    rewards.add(itemStack);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rewards;
    }

    
    public static String loadPlayerLastClickDate(Path path) {
        String lastClickDate = ""; // Initialiser avec une chaîne vide pour éviter null
        try {
            Gson gson = new Gson();
            try (Reader reader = new FileReader(path.toFile())) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonElement dateElement = json.get("lastClickDate"); // Récupérer le JsonElement
                if (dateElement != null && !dateElement.isJsonNull()) {
                    lastClickDate = dateElement.getAsString(); // Convertir le JsonElement en String si ce n'est pas null
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastClickDate; // Renvoie la date sous forme de chaîne de caractères
    }

}

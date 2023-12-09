package com.alcamod.network;

import com.alcamod.ConfigData;
import com.alcamod.PlayerEventHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketHandleRewardRequest {

    private final UUID playerUUID;

    public PacketHandleRewardRequest(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public static void encode(PacketHandleRewardRequest msg, PacketBuffer buf) {
        buf.writeUUID(msg.playerUUID);
    }

    public static PacketHandleRewardRequest decode(PacketBuffer buf) {
        return new PacketHandleRewardRequest(buf.readUUID());
    }

    public static void handle(PacketHandleRewardRequest msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity playerEntity = context.getSender();
            if (playerEntity != null) {
                List<String> rewards = readPlayerRewards(msg.playerUUID);
                boolean rewardGiven = false;

                for (int i = 0; i < rewards.size(); i++) {
                    String reward = rewards.get(i);
                    if (!"alcamod:green_mark".equals(reward)) {
                        Item rewardItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(reward.toLowerCase()));
                        if (rewardItem != null) {
                            ItemStack itemStack = new ItemStack(rewardItem);
                            if (playerEntity.inventory.add(itemStack)) {
                                rewards.set(i, "alcamod:green_mark");
                                rewardGiven = true;
                                break;
                            }
                        }
                    }
                }

                if (rewardGiven) {
                    updatePlayerRewardsFile(msg.playerUUID, rewards, LocalDate.now().toString());
                    // Vérifiez si toutes les récompenses sont alcamod:green_mark
                    if (rewards.stream().allMatch(reward -> reward.equals("alcamod:green_mark"))) {
                        resetPlayerRewards(msg.playerUUID);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
    private static void resetPlayerRewards(UUID playerUUID) {
        try {
            ConfigData configData = readConfigData(); // Assurez-vous que cette méthode est accessible
            List<String> newRewards = new ArrayList<>();
            Random random = new Random();

            // Ajouter des récompenses aléatoires basées sur la configuration
            for (int i = 0; i < 7; i++) {
                String reward = configData.getRewards().get(random.nextInt(configData.getRewards().size()));
                newRewards.add(reward);
            }

            // Ajouter une top récompense aléatoire
            String topReward = configData.getTopRewards().get(random.nextInt(configData.getTopRewards().size()));
            newRewards.add(topReward);

            // Ajouter d'autres récompenses aléatoires
            for (int i = 0; i < 6; i++) {
                String reward = configData.getRewards().get(random.nextInt(configData.getRewards().size()));
                newRewards.add(reward);
            }

            topReward = configData.getTopRewards().get(random.nextInt(configData.getTopRewards().size()));
            newRewards.add(topReward);

            // Mettre à jour le fichier de récompenses du joueur
            updatePlayerRewardsFile(playerUUID, newRewards, LocalDate.now().toString());
        } catch (Exception e) {
            // Gérer l'exception
            e.printStackTrace();
        }
    }

    // Assurez-vous que readConfigData est disponible et renvoie les données de configuration correctes
    private static ConfigData readConfigData() {
        try {
            Path configJson = Paths.get("config/alcamod/dailyRewards/config.json");
            Gson gson = new Gson();
            String json = new String(Files.readAllBytes(configJson));
            return gson.fromJson(json, ConfigData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ConfigData(); // Retourne une instance vide en cas d'erreur
        }
    }




    private static List<String> readPlayerRewards(UUID playerUUID) {
        try {
            Path playerFile = Paths.get("config/alcamod/dailyRewards/playerData", playerUUID.toString() + ".json");
            Gson gson = new Gson();
            String json = new String(Files.readAllBytes(playerFile));
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            JsonArray rewardsArray = jsonObject.getAsJsonArray("rewards");
            Type type = new TypeToken<List<String>>(){}.getType();
            return gson.fromJson(rewardsArray, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void updatePlayerRewardsFile(UUID playerUUID, List<String> rewards, String lastClickDate) {
        try {
            Path playerFile = Paths.get("config/alcamod/dailyRewards/playerData", playerUUID.toString() + ".json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = new JsonObject();
            JsonArray rewardsArray = gson.toJsonTree(rewards, new TypeToken<List<String>>(){}.getType()).getAsJsonArray();
            jsonObject.add("rewards", rewardsArray);
            jsonObject.addProperty("lastClickDate", lastClickDate); // Utiliser le paramètre lastClickDate ici
            Files.write(playerFile, gson.toJson(jsonObject).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

package com.alcamod.network;

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
import java.util.List;
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
        System.out.println("testPacket0");
        context.enqueueWork(() -> {
            ServerPlayerEntity playerEntity = context.getSender();
            if (playerEntity != null) {
                System.out.println("testPacket1");
                List<String> rewards = readPlayerRewards(msg.playerUUID);
                boolean rewardGiven = false;

                for (int i = 0; i < rewards.size(); i++) {
                    String reward = rewards.get(i);
                    if (!"alcamod:green_mark".equals(reward)) {
                        Item rewardItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(reward.toLowerCase()));
                        if (rewardItem != null) {
                            ItemStack itemStack = new ItemStack(rewardItem);
                            if (playerEntity.inventory.add(itemStack)) {
                                System.out.println("testPacket2");
                                rewards.set(i, "alcamod:green_mark");
                                rewardGiven = true;
                                break;
                            }
                        }
                    }
                }
                System.out.println("testPacket3");

                if (rewardGiven) {
                    System.out.println("testPacket4");
                    updatePlayerRewardsFile(msg.playerUUID, rewards);
                }
            }
        });
        context.setPacketHandled(true);
    }

    private static List<String> readPlayerRewards(UUID playerUUID) {
        try {
            System.out.println("testreadplayerreward");
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

    private static void updatePlayerRewardsFile(UUID playerUUID, List<String> rewards) {
        try {
            System.out.println("testupdateplayerreward");
            Path playerFile = Paths.get("config/alcamod/dailyRewards/playerData", playerUUID.toString() + ".json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = new JsonObject();
            JsonArray rewardsArray = gson.toJsonTree(rewards, new TypeToken<List<String>>(){}.getType()).getAsJsonArray();
            jsonObject.add("rewards", rewardsArray);
            jsonObject.addProperty("lastClickDate", LocalDate.now().toString());
            Files.write(playerFile, gson.toJson(jsonObject).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

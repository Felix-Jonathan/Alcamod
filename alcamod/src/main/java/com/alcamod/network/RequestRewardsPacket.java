package com.alcamod.network;


import com.alcamod.NetworkHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class RequestRewardsPacket {

    private final UUID playerUUID;

    public RequestRewardsPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public static void encode(RequestRewardsPacket msg, PacketBuffer buf) {
        buf.writeUUID(msg.playerUUID);
    }

    public static RequestRewardsPacket decode(PacketBuffer buf) {
        return new RequestRewardsPacket(buf.readUUID());
    }

    public static void handle(RequestRewardsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = ctx.get().getSender();
            if (playerEntity != null) {
                List<String> rewards = readPlayerRewards(msg.playerUUID);
                LocalDate lastClickDate = readLastClickDate(msg.playerUUID);
                RewardDataPacket rewardDataPacket = new RewardDataPacket(rewards, lastClickDate);
                NetworkHandler.INSTANCE.sendTo(rewardDataPacket, playerEntity.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        ctx.get().setPacketHandled(true);
    }




    private static List<String> readPlayerRewards(UUID playerUUID) {
        try {
            Path playerFile = Paths.get("config/alcamod/dailyRewards/playerData", playerUUID.toString() + ".json");
            String json = new String(Files.readAllBytes(playerFile));
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            JsonArray rewardsArray = jsonObject.getAsJsonArray("rewards");
            Type type = new TypeToken<List<String>>(){}.getType();
            return new Gson().fromJson(rewardsArray, type);
        } catch (Exception e) {
            e.printStackTrace();  // Imprime la trace de l'exception
            return Collections.emptyList();
        }
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

package com.alcamod.commands;

import com.alcamod.network.RewardDataPacket;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.alcamod.Alcamod;
import com.alcamod.NetworkHandler;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TodayCommand {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("aujourdhui").executes(TodayCommand::execute));
    }

    private static int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrException();
        UUID playerUUID = player.getUUID();
        LOGGER.info("Fetching rewards for player: {}", playerUUID);

        List<String> rewards = readPlayerRewards(playerUUID);

        LOGGER.info("Rewards fetched: {}", rewards);
        LocalDate lastClickDate = readLastClickDate(playerUUID);  // Ajoutez la méthode readLastClickDate si elle n'existe pas déjà
        RewardDataPacket packet = new RewardDataPacket(rewards, lastClickDate);
        NetworkHandler.INSTANCE.sendTo(packet, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);

        LOGGER.info("Rewards packet sent to player: {}", player.getName().getString());
        return 1;
    }

    private static List<String> readPlayerRewards(UUID playerUUID) {
        try {
            Path playerFile = Paths.get("config/alcamod/dailyRewards/playerData", playerUUID.toString() + ".json");
            LOGGER.info("Reading player rewards from file: {}", playerFile);
            String json = new String(Files.readAllBytes(playerFile));
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            JsonArray rewardsArray = jsonObject.getAsJsonArray("rewards");
            Type type = new TypeToken<List<String>>(){}.getType();
            return new Gson().fromJson(rewardsArray, type);
        } catch (Exception e) {
            LOGGER.error("Error reading player rewards", e);
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

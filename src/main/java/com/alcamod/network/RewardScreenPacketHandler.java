package com.alcamod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.joml.Random;

import java.nio.file.Path;
import java.io.Reader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

import java.time.LocalDate;
import com.alcamod.Alcamod;
import com.alcamod.client.gui.RewardScreen;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.stream.IntStream;
public class RewardScreenPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("alcamod", "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, OpenRewardScreenPacket.class, OpenRewardScreenPacket::encode,
                OpenRewardScreenPacket::decode, OpenRewardScreenPacket::handle);

        CHANNEL.registerMessage(id++, AddItemToInventoryPacket.class, AddItemToInventoryPacket::encode,
                AddItemToInventoryPacket::decode, AddItemToInventoryPacket::handle);
    }

    public static void sendOpenRewardScreenPacket(ServerPlayer player, List<ItemStack> items, String lastClickDate) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new OpenRewardScreenPacket(player.getUUID(), items, lastClickDate));
    }

    public static void sendOpenRewardScreenPacket(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new OpenRewardScreenPacket(player.getUUID()));
    }

    public static void sendAddItemToInventoryPacket(ServerPlayer player, ItemStack itemStack) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new AddItemToInventoryPacket(itemStack));
    }

    public static class OpenRewardScreenPacket {
        private final UUID playerUUID;
        private final List<ItemStack> items;
        private final String lastClickDate;

        public OpenRewardScreenPacket(UUID uuid) {
            this.playerUUID = uuid;
            this.items = null;
            this.lastClickDate = null;
        }

        public OpenRewardScreenPacket(UUID uuid, List<ItemStack> items, String lastClickDate) {
            this.playerUUID = uuid;
            this.items = items;
            this.lastClickDate = lastClickDate;
        }

        public static void encode(OpenRewardScreenPacket msg, FriendlyByteBuf buffer) {
            buffer.writeUUID(msg.playerUUID);
            buffer.writeInt(msg.items.size());
            for (ItemStack item : msg.items) {
                buffer.writeItem(item);
            }
            buffer.writeUtf(msg.lastClickDate);
        }

        public static OpenRewardScreenPacket decode(FriendlyByteBuf buffer) {
            UUID uuid = buffer.readUUID();
            int size = buffer.readInt();
            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                items.add(buffer.readItem());
            }
            String lastClickDate = buffer.readUtf();
            return new OpenRewardScreenPacket(uuid, items, lastClickDate);
        }

        public static void handle(OpenRewardScreenPacket msg, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(msg, context));
            });
            context.setPacketHandled(true);
        }

        @OnlyIn(Dist.CLIENT)
        private static void handleClient(OpenRewardScreenPacket msg, NetworkEvent.Context ctx) {
            Minecraft.getInstance().setScreen(new RewardScreen(msg.playerUUID, msg.items, msg.lastClickDate));
        }
    }

    public static class AddItemToInventoryPacket {
        private final ItemStack itemStack;
    
        public AddItemToInventoryPacket(ItemStack stack) {
            this.itemStack = stack;
        }
    
        public static void encode(AddItemToInventoryPacket msg, FriendlyByteBuf buffer) {
            //Alcamod.LOGGER.info("EncodeItemToServer");
            buffer.writeItem(msg.itemStack);
        }
    
        public static AddItemToInventoryPacket decode(FriendlyByteBuf buffer) {
            //Alcamod.LOGGER.info("DecodeSendItemToServer");
            return new AddItemToInventoryPacket(buffer.readItem());
        }
    
        public static void handle(AddItemToInventoryPacket msg, Supplier<NetworkEvent.Context> ctx) {
            //Alcamod.LOGGER.info("handleItemPacket");
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player != null) {
                    //Alcamod.LOGGER.info("AddItemToInventory");
                    player.getInventory().add(msg.itemStack);
                    addNextReward(player.getUUID());
                }
            });
            context.setPacketHandled(true);
        }
    
        public static void addNextReward(UUID uuid) {
            Path path = FMLPaths.CONFIGDIR.get().resolve("alcamod/dailyRewards/playerData/" + uuid.toString() + ".json");
            Gson gson = new Gson();
            try (Reader reader = new FileReader(path.toFile())) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray rewardsArray = jsonObject.getAsJsonArray("rewards");
                boolean allRewardsAreGreenMark = true;
                for (int i = 0; i < rewardsArray.size(); i++) {
                    if (!rewardsArray.get(i).getAsString().equals("alcamod:green_mark")) {
                        rewardsArray.set(i, gson.toJsonTree("alcamod:green_mark"));
                        allRewardsAreGreenMark = false;
                        break;
                    }
                }
    
                if (allRewardsAreGreenMark) {
                    rewardsArray = gson.toJsonTree(createDefaultRewards()).getAsJsonArray();
                    jsonObject.add("rewards", rewardsArray);
                }
    
                jsonObject.addProperty("lastClickDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    
                try (FileWriter writer = new FileWriter(path.toFile())) {
                    //Alcamod.LOGGER.info("JSON Writed !");
                    gson.toJson(jsonObject, writer);
                } catch (Exception e) {
                    Alcamod.LOGGER.error("Error writing to reward file", e);
                }
            } catch (Exception e) {
                Alcamod.LOGGER.error("Error reading reward file", e);
            }
        }
    
        public static List<String> createDefaultRewards() {
            List<String> defaultRewards = new ArrayList<>();
            Random random = new Random();
            try {
                String json = Files.readString(Paths.get("config/alcamod/dailyRewards/config.json"));
                Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
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
            } catch (IOException e) {
                Alcamod.LOGGER.error("Error loading default rewards", e);
            }
            return defaultRewards;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void sendAddItemToInventoryPacket(ItemStack itemStack) {
        // Check if there is an item to send and the player instance is not null
        if (!itemStack.isEmpty() && Minecraft.getInstance().player != null) {
            Alcamod.LOGGER.info("SendItemToServerPacket");
            // Directly send the packet to the server, no need to specify the player here
            CHANNEL.send(PacketDistributor.SERVER.noArg(), new AddItemToInventoryPacket(itemStack));
        }
    }
}



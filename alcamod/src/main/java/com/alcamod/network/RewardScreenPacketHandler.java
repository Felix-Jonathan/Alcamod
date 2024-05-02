package com.alcamod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;
import java.util.function.Supplier;

import com.alcamod.client.gui.RewardScreen;

public class RewardScreenPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("alcamod", "network"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, OpenRewardScreenPacket.class, OpenRewardScreenPacket::encode, OpenRewardScreenPacket::decode, OpenRewardScreenPacket::handle);
        CHANNEL.registerMessage(id++, AddItemToInventoryPacket.class, AddItemToInventoryPacket::encode, AddItemToInventoryPacket::decode, AddItemToInventoryPacket::handle);
    }

    public static void sendOpenRewardScreenPacket(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new OpenRewardScreenPacket(player.getUUID()));
    }

    public static void sendAddItemToInventoryPacket(ServerPlayer player, ItemStack itemStack) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new AddItemToInventoryPacket(itemStack));
    }

    public static class OpenRewardScreenPacket {
        private final UUID playerUUID;

        public OpenRewardScreenPacket(UUID uuid) {
            this.playerUUID = uuid;
        }

        public static void encode(OpenRewardScreenPacket msg, FriendlyByteBuf buffer) {
            buffer.writeUUID(msg.playerUUID);
        }

        public static OpenRewardScreenPacket decode(FriendlyByteBuf buffer) {
            return new OpenRewardScreenPacket(buffer.readUUID());
        }

        public static void handle(OpenRewardScreenPacket msg, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> {
                if (context.getDirection().getReceptionSide().isClient()) {
                    net.minecraft.client.Minecraft.getInstance().setScreen(new RewardScreen(msg.playerUUID));
                }
            });
            context.setPacketHandled(true);
        }
    }

    public static class AddItemToInventoryPacket {
        private final ItemStack itemStack;

        public AddItemToInventoryPacket(ItemStack stack) {
            this.itemStack = stack;
        }

        public static void encode(AddItemToInventoryPacket msg, FriendlyByteBuf buffer) {
            buffer.writeItem(msg.itemStack);
        }

        public static AddItemToInventoryPacket decode(FriendlyByteBuf buffer) {
            return new AddItemToInventoryPacket(buffer.readItem());
        }

        public static void handle(AddItemToInventoryPacket msg, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player != null) {
                    player.getInventory().add(msg.itemStack);
                }
            });
            context.setPacketHandled(true);
        }
    }

    public static void sendAddItemToInventoryPacket(ItemStack itemStack) {
        // Check if there is an item to send and the player instance is not null
        if (!itemStack.isEmpty() && Minecraft.getInstance().player != null) {
            // Directly send the packet to the server, no need to specify the player here
            CHANNEL.send(PacketDistributor.SERVER.noArg(), new AddItemToInventoryPacket(itemStack));
        }
    }
}

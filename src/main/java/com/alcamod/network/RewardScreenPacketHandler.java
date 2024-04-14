package com.alcamod.network;

import com.alcamod.client.gui.RewardScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class RewardScreenPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("alcamod", "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        CHANNEL.registerMessage(0, OpenRewardScreenPacket.class, OpenRewardScreenPacket::encode, OpenRewardScreenPacket::decode, OpenRewardScreenPacket::handle);
    }

    public static void sendOpenRewardScreenPacket(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new OpenRewardScreenPacket());
    }

    public static class OpenRewardScreenPacket {
        // Packet doesn't carry any data
        public OpenRewardScreenPacket() {}

        public static void encode(OpenRewardScreenPacket msg, FriendlyByteBuf buffer) {
            // Nothing to encode
        }

        public static OpenRewardScreenPacket decode(FriendlyByteBuf buffer) {
            // Nothing to decode
            return new OpenRewardScreenPacket();
        }

        public static void handle(OpenRewardScreenPacket msg, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> {
                // Ensure that the action is run on the client
                if (context.getDirection().getReceptionSide().isClient()) {
                    net.minecraft.client.Minecraft.getInstance().setScreen(new RewardScreen());
                }
            });
            context.setPacketHandled(true);
        }
    }
}
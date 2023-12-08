package com.alcamod;

import com.alcamod.network.RequestRewardsPacket;
import com.alcamod.network.RewardDataPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("alcamod", "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerMessages() {
        int id = 0;
        // Assurez-vous que PacketAddItemToInventory est correctement défini quelque part dans votre code
        logPacketRegistration("PacketAddItemToInventory", id);
        INSTANCE.registerMessage(id++, PacketAddItemToInventory.class, PacketAddItemToInventory::encode, PacketAddItemToInventory::decode, PacketAddItemToInventory::handle);

        logPacketRegistration("RequestRewardsPacket", id);
        INSTANCE.registerMessage(id++, RequestRewardsPacket.class, RequestRewardsPacket::encode, RequestRewardsPacket::decode, RequestRewardsPacket::handle);

        logPacketRegistration("RewardDataPacket", id);
        INSTANCE.registerMessage(id++, RewardDataPacket.class, RewardDataPacket::encode, RewardDataPacket::decode, RewardDataPacket::handle);

        logPacketRegistration("Finish", id);
    }

    private static void logPacketRegistration(String packetName, int id) {
        System.out.println("Registering packet: " + packetName + " with ID: " + id);
        // Ou utilisez un logger si vous en avez configuré un
    }
}

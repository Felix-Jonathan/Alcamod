package com.alcamod.network;

import com.alcamod.gui.DailyGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import com.alcamod.gui.DailyContainer;
public class RewardDataPacket {

    private final List<String> rewards;
    private static final Logger LOGGER = LogManager.getLogger();



    public RewardDataPacket(List<String> rewards) {
        this.rewards = rewards;
    }

    public static void encode(RewardDataPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.rewards.size());
        for (String reward : msg.rewards) {
            buf.writeUtf(reward);
        }
    }

    public static RewardDataPacket decode(PacketBuffer buf) {
        int size = buf.readInt();
        List<String> rewards = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            rewards.add(buf.readUtf(32767));
        }
        return new RewardDataPacket(rewards);
    }

    // Dans RewardDataPacket
    public static void handle(RewardDataPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            LOGGER.info("Handling RewardDataPacket on client side");
            if (context.getDirection().getReceptionSide().isClient()) {
                DailyGui.openWithRewards(msg.rewards);
            }
        });
        context.setPacketHandled(true);
    }


}


package com.alcamod.network;

import com.alcamod.gui.DailyGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import com.alcamod.gui.DailyContainer;
public class RewardDataPacket {

    private final List<String> rewards;
    private final LocalDate lastClickDate;
    private static final Logger LOGGER = LogManager.getLogger();



    public RewardDataPacket(List<String> rewards, LocalDate lastClickDate) {
        this.rewards = rewards;
        this.lastClickDate = lastClickDate;
    }

    public static void encode(RewardDataPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.rewards.size());
        for (String reward : msg.rewards) {
            buf.writeUtf(reward);
        }
        buf.writeUtf(msg.lastClickDate != null ? msg.lastClickDate.toString() : "");
    }

    public static RewardDataPacket decode(PacketBuffer buf) {
        int size = buf.readInt();
        List<String> rewards = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            rewards.add(buf.readUtf(32767));
        }

        String dateString = buf.readUtf(32767);
        LocalDate lastClickDate = dateString.isEmpty() ? LocalDate.MIN : LocalDate.parse(dateString);

        return new RewardDataPacket(rewards, lastClickDate);
    }




    // Dans RewardDataPacket
    public static void handle(RewardDataPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            LOGGER.info("Handling RewardDataPacket on client side");
            if (context.getDirection().getReceptionSide().isClient()) {
                // Passer également lastClickDate à la méthode openWithRewards
                DailyGui.openWithRewards(msg.rewards, msg.lastClickDate);
            }
        });
        context.setPacketHandled(true);
    }



}


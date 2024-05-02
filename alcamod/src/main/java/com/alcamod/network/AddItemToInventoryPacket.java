package com.alcamod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AddItemToInventoryPacket {
    private final ItemStack itemStack;

    public AddItemToInventoryPacket(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static void encode(AddItemToInventoryPacket msg, FriendlyByteBuf buf) {
        buf.writeItemStack(msg.itemStack, false); // Écrire l'itemStack dans le buffer
    }

    public static AddItemToInventoryPacket decode(FriendlyByteBuf buf) {
        return new AddItemToInventoryPacket(buf.readItem());
    }

    public static class Handler {
        public static void handle(final AddItemToInventoryPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    player.getInventory().add(message.itemStack.copy());
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}

package com.alcamod;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAddItemToInventory {

    private final ItemStack itemToAdd;

    public PacketAddItemToInventory(ItemStack item) {
        this.itemToAdd = item;
    }

    public static void encode(PacketAddItemToInventory msg, PacketBuffer buf) {
        buf.writeItem(msg.itemToAdd); // Utilisez writeItem au lieu de writeItemStack
    }


    public static PacketAddItemToInventory decode(PacketBuffer buf) {
        return new PacketAddItemToInventory(buf.readItem()); // Utilisez readItem au lieu de readItemStack
    }


    // Dans PacketAddItemToInventory.handle
    public static void handle(PacketAddItemToInventory msg, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (ctx.getSender() != null) {
                ctx.getSender().inventory.add(msg.itemToAdd);
                ctx.getSender().inventoryMenu.broadcastChanges(); // Assurez-vous que l'inventaire est synchronisé
            }
        });
        ctx.setPacketHandled(true);
    }

}

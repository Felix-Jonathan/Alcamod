package com.alcamod.gui;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IWorldPosCallable;
import com.google.gson.Gson;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.Collections;

public class DailyContainer extends Container {
    private static final int SLOT_SIZE = 36; // Taille standard d'un emplacement

    protected DailyContainer(int id, PlayerInventory playerInventory) {
        super(null, id); // Remplacer null par votre type de conteneur spécifique

        UUID playerUUID = playerInventory.player.getUUID();
        List<String> rewards = readPlayerRewards(playerUUID);

        int centerX = 125;
        int centerY = 28;

        int startX = centerX - (SLOT_SIZE * 2);
        int startY = centerY + SLOT_SIZE;

        for (int line = 0; line < 3; line++) {
            for (int i = 0; i < 5; i++) {
                int xPosition = startX + (i * (SLOT_SIZE)) - i;
                int yPosition = startY + (line * SLOT_SIZE);
                int slotIndex = i + (line * 5); // Calcul de l'index de l'emplacement correct

                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(rewards.get(slotIndex).toLowerCase()));
                ItemStack itemStack = item != null ? new ItemStack(item, 1) : new ItemStack(Items.AIR); // Utilisez AIR pour les emplacements vides

                Slot slot = new Slot(playerInventory, slotIndex, xPosition, yPosition) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false; // Les items ne devraient pas être déplaçables
                    }
                };

                slot.set(itemStack);
                this.addSlot(slot);
            }
        }

        // Ajoutez d'autres emplacements pour l'inventaire du joueur si nécessaire
    }


    private List<String> readPlayerRewards(UUID playerUUID) {
        try {
            Path playerFile = Paths.get("config/alcamod/dailyRewards/playerData", playerUUID.toString() + ".json");
            String json = new String(Files.readAllBytes(playerFile));
            return new Gson().fromJson(json, List.class);
        } catch (Exception e) {
            // Gérer l'exception
            return Collections.emptyList(); // Retourne une liste vide en cas d'erreur
        }
    }


    private int calculateXPosition(int index) {
        // Votre logique pour calculer la position X de l'emplacement
        return 0;
    }

    private int calculateYPosition(int index) {
        // Votre logique pour calculer la position Y de l'emplacement
        return 0;
    }


    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }
}

package com.alcamod.gui;

import com.alcamod.Alcamod;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.Collections;

public class DailyContainer extends Container {
    private static final int SLOT_SIZE = 36;
    private DisplayInventory displayInventory;

    public static List<String> rewards;
    public static UUID playerUUID;


    public DailyContainer(int windowId, PlayerInventory playerInventory) {
        super(Alcamod.DAILY_CONTAINER.get(), windowId);
        this.displayInventory = new DisplayInventory(15); // 15 emplacements pour l'affichage

        this.playerUUID = playerInventory.player.getUUID();
        this.rewards = readPlayerRewards(playerUUID);
        System.out.println(this.rewards);

        int centerX = 125;
        int centerY = 28;
        int startX = centerX - (SLOT_SIZE * 2);
        int startY = centerY + SLOT_SIZE;
        System.out.println("Placement");
        for (int line = 0; line < 3; line++) {
            for (int i = 0; i < 5; i++) {
                int xPosition = startX + (i * (SLOT_SIZE)) - i;
                int yPosition = startY + (line * SLOT_SIZE);
                int slotIndex = i + (line * 5);

                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.rewards.get(slotIndex).toLowerCase()));
                System.out.println(item);
                ItemStack itemStack = item != null ? new ItemStack(item, 1) : new ItemStack(net.minecraft.item.Items.AIR);

                this.displayInventory.setItem(slotIndex, itemStack);

                this.addSlot(new Slot(displayInventory, slotIndex, xPosition, yPosition) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }

                    @Override
                    public boolean mayPickup(PlayerEntity playerIn) {
                        return false;
                    }
                });
            }
        }
        System.out.println("PlacementFini");
    }

    private List<String> readPlayerRewards(UUID playerUUID) {
        try {
            Path playerFile = Paths.get("config/alcamod/dailyRewards/playerData", playerUUID.toString() + ".json");
            String json = new String(Files.readAllBytes(playerFile));
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            JsonArray rewardsArray = jsonObject.getAsJsonArray("rewards");
            Type type = new TypeToken<List<String>>(){}.getType();
            return new Gson().fromJson(rewardsArray, type);
        } catch (Exception e) {
            e.printStackTrace();  // Imprime la trace de l'exception
            return Collections.emptyList();
        }
    }


    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    public static List<String> getRewards() {
        return rewards;
    }
}

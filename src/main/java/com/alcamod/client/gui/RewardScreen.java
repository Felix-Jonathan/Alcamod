package com.alcamod.client.gui;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Type;
import java.io.Reader;

import com.alcamod.Alcamod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLPaths;

import com.google.gson.reflect.TypeToken;
import net.minecraftforge.registries.ForgeRegistries;

public class RewardScreen extends Screen {

    private static final ResourceLocation IMAGE_LOCATION = new ResourceLocation(Alcamod.MODID,
            "textures/gui/dailyrewards.png");
    private List<ItemStack> playerRewards; // Liste des récompenses du joueur
    private static final int ITEM_SIZE = 36;
    private static final int START_X = 140; // X coordinate where items start
    private static final int START_Y = 55; // Y coordinate where items start
    private static final int ITEMS_PER_ROW = 5;
    private static final int IMAGE_WIDTH = 1920;
    private static final int IMAGE_HEIGHT = 1080;

    private static final int CLICKABLE_ZONE_X = 185; // Position X de la zone clickable
    private static final int CLICKABLE_ZONE_Y = 175; // Position Y de la zone clickable
    private static final int CLICKABLE_ZONE_WIDTH = 100; // Largeur de la zone clickable
    private static final int CLICKABLE_ZONE_HEIGHT = 25; // Hauteur de la zone clickable
    private static final int CLICKABLE_ZONE_OPACITY = 100; // Opacité de la zone clickable (70%) 178
    private static final int CLICKABLE_ZONE_COLOR = 0xFFFF00; // Couleur jaune pour la zone clickable

    public RewardScreen(UUID playerUUID) {
        super(Component.literal("Image Screen"));
        this.playerRewards = loadPlayerRewards(playerUUID);
        System.out.println(this.playerRewards);
    }

    private List<ItemStack> loadPlayerRewards(UUID playerUUID) {
        Path path = FMLPaths.CONFIGDIR.get().resolve("alcamod/dailyRewards/playerData/" + playerUUID + ".json");
        Gson gson = new Gson();
        try (Reader reader = new FileReader(path.toFile())) {
            // Parse the JSON file to JsonObject
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            // Get the "rewards" JsonArray
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> rewardStrings = gson.fromJson(jsonObject.get("rewards"), type);
            List<ItemStack> rewards = new ArrayList<>();
            for (String item : rewardStrings) {
                Item itemObj = ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
                if (itemObj != null) {
                    rewards.add(new ItemStack(itemObj));
                } else {
                    // Handle the case where the item is not found in the registry
                    System.out.println("Item not found: " + item);
                }
            }
            return rewards;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Méthode pour écrire les données du joueur dans le fichier JSON
    private void writeNextPlayerData(String itemToReplace, ItemStack itemStack) {
        Path path = FMLPaths.CONFIGDIR.get()
                .resolve("alcamod/dailyRewards/playerData/" + Minecraft.getInstance().player.getUUID() + ".json");
        Gson gson = new Gson();
        try (Reader reader = new FileReader(path.toFile())) {
            // Parse the JSON file to JsonObject
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Get the "lastClickDate" field
            String lastClickDate = jsonObject.get("lastClickDate").getAsString();

            // Get today's date
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedCurrentDate = currentDate.format(formatter);

            // Check if lastClickDate is the same as today's date
            if (lastClickDate.equals(formattedCurrentDate)) {
                System.out.println("You have already clicked today.");
                return;
            }
            // Ajoute l'objet à l'inventaire du joueur
            System.out.println("GIVE");
            System.out.println(itemStack.getDisplayName());
            Minecraft.getInstance().player.getInventory().add(itemStack.copy());
            // Met à jour le fichier JSON du joueur

            // Update lastClickDate to today's date
            jsonObject.addProperty("lastClickDate", formattedCurrentDate);

            // Get the "rewards" JsonArray
            JsonArray rewardsArray = jsonObject.getAsJsonArray("rewards");
            // Find the first item that is not "alcamod:green_mark" and replace it
            boolean allRewardsAreGreenMark = false;
            for (int i = 0; i < rewardsArray.size(); i++) {
                String item = rewardsArray.get(i).getAsString();
                if (!item.equals("alcamod:green_mark")) {
                    rewardsArray.set(i, gson.toJsonTree("alcamod:green_mark"));
                    System.out.println(i);
                    if (i == rewardsArray.size() - 1){
                        System.out.println("reset");
                        allRewardsAreGreenMark = true;
                    }
                    break;
                }
            }

            // If all rewards are "alcamod:green_mark", reset the rewards array
            if (allRewardsAreGreenMark) {

                // Recreate the default rewards list
                List<String> defaultRewards = createDefaultRewards();
                // Convert it to a JSON array
                rewardsArray = gson.toJsonTree(defaultRewards).getAsJsonArray();
                // Update the "rewards" field in the JSON object
                jsonObject.add("rewards", rewardsArray);
            }

            // Write the updated JSON object back to the file
            try (FileWriter writer = new FileWriter(path.toFile())) {
                gson.toJson(jsonObject, writer);
            }
            this.playerRewards = loadPlayerRewards(Minecraft.getInstance().player.getUUID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> createDefaultRewards() throws IOException {
        List<String> defaultRewards = new ArrayList<>();
        Random random = new Random();
        String json = Files.readString(Paths.get("config/alcamod/dailyRewards/config.json"));
        Type type = new TypeToken<Map<String, List<String>>>() {
        }.getType();
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
        return defaultRewards;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Dessine l'image de fond
        renderBackground(guiGraphics);

        // Dessine la zone clickable
        renderClickableZone(guiGraphics);

        // Dessine les récompenses du joueur
        renderPlayerRewards(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    // Méthode pour dessiner l'image de fond
    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, IMAGE_LOCATION);

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int scaledWidth = screenWidth;
        int scaledHeight = (int) ((float) screenWidth * (1080 / (float) 1920));
        int yOffset = (screenHeight - scaledHeight) / 2;
        guiGraphics.blit(IMAGE_LOCATION, 0, yOffset, 0, 0, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
    }

    // Méthode pour dessiner la zone clickable
    private void renderClickableZone(GuiGraphics guiGraphics) {
        guiGraphics.fill(CLICKABLE_ZONE_X, CLICKABLE_ZONE_Y, CLICKABLE_ZONE_X + CLICKABLE_ZONE_WIDTH,
                CLICKABLE_ZONE_Y + CLICKABLE_ZONE_HEIGHT, CLICKABLE_ZONE_COLOR + (CLICKABLE_ZONE_OPACITY << 24));
    }

    // Méthode pour dessiner les récompenses du joueur
    private void renderPlayerRewards(GuiGraphics guiGraphics) {
        int x = START_X;
        int y = START_Y;
        for (int i = 0; i < playerRewards.size(); i++) {
            ItemStack itemStack = playerRewards.get(i);
            guiGraphics.renderItem(itemStack, x, y);
            x += ITEM_SIZE;
            if ((i + 1) % ITEMS_PER_ROW == 0) {
                x = START_X;
                y += ITEM_SIZE;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= CLICKABLE_ZONE_X && mouseX <= CLICKABLE_ZONE_X + CLICKABLE_ZONE_WIDTH &&
                mouseY >= CLICKABLE_ZONE_Y && mouseY <= CLICKABLE_ZONE_Y + CLICKABLE_ZONE_HEIGHT) {
            // Ajoute le premier objet de la liste playerRewards dans l'inventaire du joueur
            for (ItemStack itemStack : playerRewards) {
                ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
                if (!itemRegistryName.toString().equals("alcamod:green_mark")) {
                    writeNextPlayerData(itemRegistryName.toString(), itemStack);
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // 256 est le code de la touche ÉCHAP
            Minecraft.getInstance().setScreen(null); // Ferme l'écran
            return true; // Consomme l'événement
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
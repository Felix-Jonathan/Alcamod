package com.alcamod.client.gui;

import com.alcamod.Alcamod;
import com.alcamod.network.RewardScreenPacketHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RewardScreen extends Screen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Alcamod.MODID,
            "textures/gui/dailyrewards500.png");
    private static final int TEXTURE_WIDTH = 250;
    private static final int TEXTURE_HEIGHT = 250;
    private static final int ITEMS_PER_ROW = 5;
    private static final int ITEM_SPACING = 36;
    private static final int ITEM_START_X = 54;
    private static final int ITEM_START_Y = 63;
    private static final int BUTTON_X = 50;
    private static final int BUTTON_Y = 175;
    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 25;

    private List<ItemStack> playerRewards;
    private String lastClickDate = null;
    private UUID playerUUID = null;

    public RewardScreen(UUID playerUUID) {
        super(Component.literal("Daily Rewards"));
        System.out.println("Suis-jeUnServeur?2");
        this.playerRewards = loadPlayerRewards(playerUUID);
    }

    public RewardScreen(UUID playerUUID, List<ItemStack> playerRewards, String lastClickDate) {
        super(Component.literal("Daily Rewards"));
        System.out.println("Suis-jeUnServeur?2.2");
        this.playerUUID = playerUUID;
        this.playerRewards = playerRewards;
        this.lastClickDate = lastClickDate;
        //Alcamod.LOGGER.info(lastClickDate);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderTexture(guiGraphics);
        renderItems(guiGraphics);
        //renderButton(guiGraphics, mouseX, mouseY);
        renderCountdown(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderTexture(GuiGraphics guiGraphics) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, (this.width - TEXTURE_WIDTH) / 2, (this.height - TEXTURE_HEIGHT) / 2, 0, 0,
                TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    private void renderItems(GuiGraphics guiGraphics) {
        int x = (this.width - TEXTURE_WIDTH) / 2 + ITEM_START_X;
        int y = (this.height - TEXTURE_HEIGHT) / 2 + ITEM_START_Y;

        for (int i = 0; i < playerRewards.size(); i++) {
            ItemStack itemStack = playerRewards.get(i);
            guiGraphics.renderItem(itemStack, x, y);
            // System.out.println(i%3);
            if ((i % 5) == 0) {
                x += ITEM_SPACING - (i % 5) - 1;
            } else {
                x += ITEM_SPACING - (i % 5);
            }
            if ((i + 1) % ITEMS_PER_ROW == 0) {
                x = (this.width - TEXTURE_WIDTH) / 2 + ITEM_START_X;
                y += ITEM_SPACING;
            }
        }
    }

    private void renderButton(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int buttonX = (this.width - TEXTURE_WIDTH) / 2 + BUTTON_X;
        int buttonY = (this.height - TEXTURE_HEIGHT) / 2 + BUTTON_Y;

        boolean isHovered = mouseX >= buttonX && mouseX < buttonX + BUTTON_WIDTH && mouseY >= buttonY
                && mouseY < buttonY + BUTTON_HEIGHT;
        int textureY = isHovered ? BUTTON_HEIGHT : 0;

        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, buttonX, buttonY, 0, TEXTURE_HEIGHT + textureY, BUTTON_WIDTH, BUTTON_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT * 2);
    }

    private void renderCountdown(GuiGraphics guiGraphics) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        ZonedDateTime nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(ZoneId.of("Europe/Paris"));
        Duration duration = Duration.between(now, nextMidnight);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        String countdownText = String.format("Reset in: %02d:%02d:%02d", hours, minutes, seconds);
        int x = (this.width - font.width(countdownText)) / 2;
        int y = (this.height - TEXTURE_HEIGHT) / 2 + 20;

        guiGraphics.drawString(font, countdownText, x, y, 0xFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int buttonX = (this.width - TEXTURE_WIDTH) / 2 + BUTTON_X;
        int buttonY = (this.height - TEXTURE_HEIGHT) / 2 + BUTTON_Y;

        //Alcamod.LOGGER.info("TestClick");
        if (mouseX >= buttonX && mouseX < buttonX + BUTTON_WIDTH && mouseY >= buttonY
                && mouseY < buttonY + BUTTON_HEIGHT) {
            //Alcamod.LOGGER.info("TestClickValidé");
            for (ItemStack itemStack : playerRewards) {
                ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
                if (!itemRegistryName.toString().equals("alcamod:green_mark")) {
                    //Alcamod.LOGGER.info("located");
                    writeNextPlayerData(itemRegistryName.toString(), itemStack);
                    return true;
                }
            }
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    // Méthode pour écrire les données du joueur dans le fichier JSON
    public void writeNextPlayerData(String itemToReplace, ItemStack itemStack) {
        //Alcamod.LOGGER.info("STARTPROCESS");
        // Get the "lastClickDate" field
        String lastClickDate = "";
        if (this.lastClickDate != null) {
            lastClickDate = this.lastClickDate;

            //Si on est dans le même jour
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedCurrentDate = currentDate.format(formatter);
            //Alcamod.LOGGER.info("Current Date", formattedCurrentDate);
            // Check if lastClickDate is the same as today's date
            if (lastClickDate.equals(formattedCurrentDate)) {
                Alcamod.LOGGER.info("You have already clicked today.");
                Minecraft.getInstance().setScreen(null);
                return;
            }

            //Alcamod.LOGGER.info("GIVE");
            Alcamod.LOGGER.info("["+formattedCurrentDate.toString()+"]"+"Daily Give item to "+this.playerUUID.toString()+" :", itemStack.getDisplayName());
            // Envoyer l'item au serveur au lieu de l'ajouter directement
            sendItemToServer(itemStack);
            Minecraft.getInstance().setScreen(null); // Ferme l'écran
        } 
    }

    private void sendItemToServer(ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            //Alcamod.LOGGER.info("SendItemToServer");
            RewardScreenPacketHandler.sendAddItemToInventoryPacket(itemStack);
        }
        Minecraft.getInstance().setScreen(null);
    }

    private List<ItemStack> loadPlayerRewards(UUID playerUUID) {
        List<ItemStack> rewards = new ArrayList<>();

        try {
            Path path = Path.of("config", "alcamod", "dailyRewards", "playerData", playerUUID.toString() + ".json");
            Gson gson = new Gson();

            try (Reader reader = new FileReader(path.toFile())) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonArray rewardsArray = json.getAsJsonArray("rewards");

                for (int i = 0; i < rewardsArray.size(); i++) {
                    String itemId = rewardsArray.get(i).getAsString();
                    ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)));
                    rewards.add(itemStack);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rewards;
    }

    // private LocalDate loadLastClickDate(UUID playerUUID) {
    //     LocalDate lastClickDate = null;

    //     try {
    //         Path path = Path.of("config", "alcamod", "dailyRewards", "playerData", playerUUID.toString() + ".json");
    //         Gson gson = new Gson();

    //         try (Reader reader = new FileReader(path.toFile())) {
    //             JsonObject json = gson.fromJson(reader, JsonObject.class);
    //             String lastClickDateString = json.get("lastClickDate").getAsString();
    //             lastClickDate = LocalDate.parse(lastClickDateString, DateTimeFormatter.ISO_LOCAL_DATE);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return lastClickDate != null ? lastClickDate : LocalDate.MIN;
    // }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // 256 est le code de la touche ÉCHAP
            Minecraft.getInstance().setScreen(null); // Ferme l'écran
            return true; // Consomme l'événement
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
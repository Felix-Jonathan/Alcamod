package com.alcamod.gui;

import com.alcamod.network.PacketHandleRewardRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.ArrayList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.alcamod.NetworkHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;


public class DailyGui extends ContainerScreen<DailyContainer> {

    private static final ResourceLocation GUI = new ResourceLocation("alcamod", "textures/gui/dailyrewards.png");
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private int buttonX;
    private int buttonY;

    private static final int FIXED_BACKGROUND_WIDTH = 250;
    private static final int FIXED_BACKGROUND_HEIGHT = 250;
    private final UUID playerUUID = DailyContainer.playerUUID;
    // Dans vos méthodes, utilisez :
    private static final List<String> rewards = new ArrayList<>();

    private static final Logger LOGGER = LogManager.getLogger();

    private LocalDate lastClickDate;

    private int tickCount = 0;
    public DailyGui(DailyContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        // Vous devrez ajuster ces dimensions en fonction de la taille de votre image de fond et de la mise en page de votre GUI
        this.imageWidth = 250; // Largeur de l'image de fond
        this.imageHeight = 250; // Hauteur de l'image de fond
        // Ajuster la taille de l'inventaire si nécessaire
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // Assurez-vous que le chemin d'accès à la texture est correct.
        Minecraft.getInstance().getTextureManager().bind(GUI);

        // Centrez l'image de fond sur la fenêtre du jeu
        int guiLeft = (this.width - FIXED_BACKGROUND_WIDTH) / 2;
        int guiTop = (this.height - FIXED_BACKGROUND_HEIGHT) / 2;

        // Dessinez l'image de fond avec les dimensions fixes
        blit(matrixStack, guiLeft, guiTop, 0, 0, FIXED_BACKGROUND_WIDTH, FIXED_BACKGROUND_HEIGHT, FIXED_BACKGROUND_WIDTH, FIXED_BACKGROUND_HEIGHT);

        // Maintenant que l'arrière-plan est fixé, positionnez le rectangle gris par rapport à celui-ci
        this.buttonX = guiLeft + 95 ; // la valeur correcte pour aligner avec votre bouton "RÉCUPÉRER"
        this.buttonY = guiTop + 178; // la valeur correcte pour aligner avec votre bouton "RÉCUPÉRER"
    }


    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        // Suppression des étiquettes pour une interface épurée
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.renderBg(matrixStack, partialTicks, mouseX, mouseY); // Dessine l'arrière-plan fixe
        super.render(matrixStack, mouseX, mouseY, partialTicks); // Dessine l'interface utilisateur
        this.renderTooltip(matrixStack, mouseX, mouseY); // Dessine les infobulles

        renderCountdown(matrixStack);
        LocalDate currentDate = LocalDate.now();
        if (!lastClickDate.equals(currentDate)) {
            tickCount++;
            if (tickCount >= 20) { // Rafraîchit toutes les secondes (20 ticks)
                renderCountdown(matrixStack);
                tickCount = 0;
            }
        }


        //fill(matrixStack, buttonX, buttonY, buttonX + BUTTON_WIDTH, buttonY + BUTTON_HEIGHT, 0x80C0C0C0);
    }

    private void renderScaledItem(MatrixStack matrixStack, Slot slot, float scale) {
        matrixStack.pushPose();
        matrixStack.translate(slot.x, slot.y, 0);
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-8 * scale, -8 * scale, 0);
        this.itemRenderer.renderAndDecorateItem(slot.getItem(), 0, 0);
        matrixStack.popPose();
    }

    public static void openGUI() {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            PlayerInventory playerInventory = player.inventory;
            ITextComponent title = new TranslationTextComponent("container.dailygui");
            Minecraft.getInstance().setScreen(new DailyGui(new DailyContainer(0, playerInventory, rewards), playerInventory, title));
        }
    }

    private void updateDisplaySlots() {
        if (rewards != null && !rewards.isEmpty()) {
            for (int i = 0; i < this.menu.slots.size(); i++) {
                if (i >= rewards.size()) {
                    break; // Empêche l'accès à un index qui n'existe pas dans la liste
                }
                Slot slot = this.menu.getSlot(i);
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(rewards.get(i).toLowerCase()));
                ItemStack itemStack = item != null ? new ItemStack(item) : ItemStack.EMPTY;
                slot.set(itemStack);
            }
        }
    }




    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= buttonX && mouseX <= buttonX + BUTTON_WIDTH && mouseY >= buttonY && mouseY <= buttonY + BUTTON_HEIGHT) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                LocalDate currentDate = LocalDate.now();
                System.out.println(this.lastClickDate);
                if (!this.lastClickDate.equals(currentDate)) {
                    // Envoyer une requête au serveur pour gérer la récompense
                    PacketHandleRewardRequest packet = new PacketHandleRewardRequest(DailyContainer.playerUUID);
                    NetworkHandler.INSTANCE.sendToServer(packet);
                    // Fermer l'interface
                    this.onClose();
                }
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public static void savePlayerData(UUID playerUUID, List<String> rewards, String lastClickDate) {
        try {
            Path playerFile = Paths.get("config/alcamod/dailyRewards/playerData", playerUUID.toString() + ".json");

            JsonObject jsonObject = new JsonObject();
            JsonArray rewardsArray = new Gson().toJsonTree(rewards, new TypeToken<List<String>>(){}.getType()).getAsJsonArray();
            jsonObject.add("rewards", rewardsArray);
            jsonObject.addProperty("lastClickDate", lastClickDate);

            Files.write(playerFile, new Gson().toJson(jsonObject).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // Dans DailyGui
    public static void openWithRewards(List<String> rewards, LocalDate lastClickDate) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity player = minecraft.player;
        if (player != null) {
            PlayerInventory playerInventory = player.inventory;
            ITextComponent title = new TranslationTextComponent("container.dailygui");
            DailyGui gui = new DailyGui(new DailyContainer(0, playerInventory, rewards), playerInventory, title);
            gui.lastClickDate = lastClickDate;
            minecraft.setScreen(gui);
        }
    }

    private void renderCountdown(MatrixStack matrixStack) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        ZonedDateTime nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(ZoneId.of("Europe/Paris"));
        Duration duration = Duration.between(now, nextMidnight);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        String countdownText = "Reset dans : " + String.format("%02d:%02d:%02d", hours, minutes, seconds);
        int stringWidth = font.width(countdownText);
        int x = (this.width - stringWidth) / 2;
        int y = 20; // Ajustez cette valeur selon votre mise en page

        // Dessinez le texte avec un contour noir
        drawString(matrixStack, font, countdownText, x + 1, y, 0x000000); // Ombre
        drawString(matrixStack, font, countdownText, x, y - 1, 0xFFFFFF); // Texte blanc
    }



}

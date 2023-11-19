package com.alcamod.gui;

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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
public class DailyGui extends ContainerScreen<DailyContainer> {

    private static final ResourceLocation GUI = new ResourceLocation("alcamod", "textures/gui/interfacejournalieresansbg.png");
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private int buttonX;
    private int buttonY;

    private final UUID playerUUID = DailyContainer.playerUUID;
    private final List<String> rewards = readPlayerRewards(playerUUID);


    public DailyGui(DailyContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        // Vous devrez ajuster ces dimensions en fonction de la taille de votre image de fond et de la mise en page de votre GUI
        this.imageWidth = 250; // Largeur de l'image de fond
        this.imageHeight = 250; // Hauteur de l'image de fond
        // Ajuster la taille de l'inventaire si nécessaire
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        // Centrer le bouton sur l'écran
        if(this.width < 1500) {
            this.buttonX = (int) ((this.width - BUTTON_WIDTH) / 1.775);
        }else{
            this.buttonX = (int) ((this.width - BUTTON_WIDTH) / 1.85);
        }
        if(this.height < 500) {
            this.buttonY = (int) ((this.height - BUTTON_HEIGHT) / 1.275);
        }else{
            this.buttonY = (int) ((this.height - BUTTON_HEIGHT) / 2);
        }
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(GUI);
        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;
        blit(matrixStack, guiLeft, guiTop, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        // Suppression des étiquettes pour une interface épurée
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        fill(matrixStack, buttonX, buttonY, buttonX + BUTTON_WIDTH, buttonY + BUTTON_HEIGHT, 0xFFFFFF00); // Couleur jaune clair
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
            Minecraft.getInstance().setScreen(new DailyGui(new DailyContainer(0, playerInventory), playerInventory, title));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= buttonX && mouseX <= buttonX + BUTTON_WIDTH && mouseY >= buttonY && mouseY <= buttonY + BUTTON_HEIGHT) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                LocalDate lastClickDate = readLastClickDate(DailyContainer.playerUUID);
                LocalDate currentDate = LocalDate.now();
                if (!lastClickDate.equals(currentDate)) {
                    for (String reward : this.rewards) {
                        if (!"alcamod:green_mark".equals(reward)) {
                            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(reward.toLowerCase()));
                            if (item != null) {
                                ItemStack itemStack = new ItemStack(item);
                                // Donner l'item au joueur
                                boolean itemAdded = player.inventory.add(itemStack);
                                if (itemAdded) {
                                    System.out.println("Item ajouté au joueur: " + reward);
                                } else {
                                    System.out.println("Pas de place dans l'inventaire pour: " + reward);
                                }
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < rewards.size(); i++) {
                        if (!rewards.get(i).equals("alcamod:green_mark")) {
                            rewards.set(i, "alcamod:green_mark");
                            break;
                        }
                    }
                    savePlayerData(DailyContainer.playerUUID, rewards, currentDate.toString());

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

    private LocalDate readLastClickDate(UUID playerUUID) {
        try {
            Path playerFile = Paths.get("config/alcamod/dailyRewards/playerData", playerUUID.toString() + ".json");
            String json = new String(Files.readAllBytes(playerFile));
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            String lastClickDateString = jsonObject.get("lastClickDate").getAsString();
            return LocalDate.parse(lastClickDateString);
        } catch (Exception e) {
            e.printStackTrace();
            return LocalDate.MIN; // Retourne une date minimale en cas d'erreur
        }
    }



}

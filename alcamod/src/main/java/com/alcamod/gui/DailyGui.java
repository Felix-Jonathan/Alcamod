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

public class DailyGui extends ContainerScreen<DailyContainer> {

    private static final ResourceLocation GUI = new ResourceLocation("alcamod", "textures/gui/dailyrewards.png");
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private int buttonX;
    private int buttonY;

    private final UUID playerUUID = DailyContainer.playerUUID;
    // Dans vos méthodes, utilisez :
    private static final List<String> rewards = new ArrayList<>();

    private static final Logger LOGGER = LogManager.getLogger();

    private LocalDate lastClickDate;
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
        // Assurez-vous que le chemin d'accès à la texture est correct.
        Minecraft.getInstance().getTextureManager().bind(GUI);
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
        updateDisplaySlots();
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        //fill(matrixStack, buttonX, buttonY, buttonX + BUTTON_WIDTH, buttonY + BUTTON_HEIGHT, 0xFFFFFF00); // Couleur jaune clair
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




}

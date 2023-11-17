package com.alcamod;

import com.alcamod.commands.TodayCommand;
import com.alcamod.items.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.alcamod.blocks.AlcaniteBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Alcamod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod("alcamod")
public class Alcamod {

    public static final String MOD_ID = "alcamod";
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> Alcanite = ITEMS.register("alcanite", com.alcamod.items.Alcanite::new);
    public static final RegistryObject<Item> AlcaniteNugget = ITEMS.register("alcanite_nugget", com.alcamod.items.AlcaniteNugget::new);

    public static final RegistryObject<Item> AlcaniteHelmet = ITEMS.register("alcanite_helmet", com.alcamod.items.AlcaniteHelmet::new);

    public static final RegistryObject<Item> AlcaniteChestplate = ITEMS.register("alcanite_chestplate", com.alcamod.items.AlcaniteChestplate::new);

    public static final RegistryObject<Item> AlcaniteLeggings = ITEMS.register("alcanite_leggings", com.alcamod.items.AlcaniteLeggings::new);

    public static final RegistryObject<Item> AlcaniteBoots = ITEMS.register("alcanite_boots", com.alcamod.items.AlcaniteBoots::new);

    public static final RegistryObject<Item> AlcaniteSword = ITEMS.register("alcanite_sword",
            () -> new AlcaniteSword(AlcaniteTier.ALCANITE,6 , -1.5F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

    public static final RegistryObject<Item> ALCANITE_PICKAXE = ITEMS.register("alcanite_pickaxe", () ->
            new AlcanitePickaxe(AlcaniteTier.ALCANITE, 4, -2.5F, new Item.Properties().tab(ItemGroup.TAB_TOOLS))
    );

    public static final RegistryObject<Item> ALCANITE_AXE = ITEMS.register("alcanite_axe", () ->
            new AlcaniteAxe(AlcaniteTier.ALCANITE, 8, -3.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS))
    );

    public static final RegistryObject<Item> ALCANITE_SHOVEL = ITEMS.register("alcanite_shovel", () ->
            new AlcaniteShovel(AlcaniteTier.ALCANITE, 2, -3.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS))
    );

    public static final RegistryObject<Item> ALCANITE_HOE = ITEMS.register("alcanite_hoe", () ->
            new AlcaniteHoe(AlcaniteTier.ALCANITE, 1, -1.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS))
    );

    public static final RegistryObject<Block> ALCANITE_BLOCK = BLOCKS.register("alcanite_block", AlcaniteBlock::new);

    public static final RegistryObject<Item> ALCANITE_BLOCK_ITEM = ITEMS.register("alcanite_block",
            () -> new BlockItem(ALCANITE_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    private static final Logger LOGGER = LogManager.getLogger();

    private void setup(final FMLCommonSetupEvent event) {
        createModConfigDirectory();
    }

    private void createModConfigDirectory() {
        try {
            Path configPath = Paths.get("config/alcamod");
            Files.createDirectories(configPath);

            Path dailyRewardsPath = configPath.resolve("dailyRewards");
            Files.createDirectories(dailyRewardsPath);

            Path configJson = dailyRewardsPath.resolve("config.json");
            if (!Files.exists(configJson)) {
                Files.createFile(configJson);
                writeInitialConfigdailyRewards(configJson);
            }

            Path playerDataPath = dailyRewardsPath.resolve("playerData");
            Files.createDirectories(playerDataPath);

            LOGGER.info("Dossier de configuration Alcamod créé avec succès.");
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la création du dossier de configuration Alcamod.", e);
        }
    }

    private void writeInitialConfigdailyRewards(Path configFilePath) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            List<String> rewards = new ArrayList<>();
            // Ajouter des données initiales à la liste des récompenses
            rewards.add("Exemple de récompense 1");
            rewards.add("Exemple de récompense 2");

            List<String> topRewards = new ArrayList<>();
            // Ajouter des données initiales à la liste des top récompenses
            topRewards.add("Exemple de TopRécompense 1");
            topRewards.add("Exemple de TopRécompense 2");

            ConfigData configData = new ConfigData(rewards, topRewards);
            String json = gson.toJson(configData);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath.toFile()))) {
                writer.write(json);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'écriture dans le fichier config.json", e);
        }
    }

    private static class ConfigData {
        private final List<String> rewards;
        private final List<String> topRewards;

        public ConfigData(List<String> rewards, List<String> topRewards) {
            this.rewards = rewards;
            this.topRewards = topRewards;
        }
        // Getters et éventuellement setters si nécessaire
    }




    private void doClientStuff(final FMLClientSetupEvent event) {
        //test
    }

    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) {
        TodayCommand.register(event.getDispatcher());
    }

    public Alcamod() {
        LOGGER.debug("Logger : Initialize");
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
    }


}

package com.alcamod;

import com.alcamod.commands.TodayCommand;
import com.alcamod.gui.DailyContainer;
import com.alcamod.items.*;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.alcamod.blocks.AlcaniteBlock;
import com.alcamod.blocks.trophy.trophyCreate;
import com.alcamod.blocks.trophy.trophyAe2;
import com.alcamod.blocks.trophy.trophyBotania;
import com.alcamod.blocks.trophy.trophyCroptopia;
import com.alcamod.blocks.trophy.trophyMekanism;
import com.alcamod.blocks.trophy.trophyWoot;
import com.alcamod.client.renderer.PhantomKnightRenderer;
import com.alcamod.blocks.trophy.trophyDraconicEvolution;
import com.alcamod.blocks.trophy.trophyIndustrialForegoing;
import com.alcamod.blocks.trophy.trophyMysticalAgriculture;
import com.alcamod.blocks.trophy.trophySolarFlux;
import com.alcamod.blocks.trophy.trophyMasterSword;
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
import com.alcamod.gui.DailyGui;
import com.alcamod.NetworkHandler;
import com.alcamod.entities.ModSpawnEggItem;
import com.alcamod.entities.phantomknight.PhantomKnightEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraft.item.SpawnEggItem;

@Mod.EventBusSubscriber(modid = Alcamod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod("alcamod")
public class Alcamod {

    public static final String MOD_ID = "alcamod";
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
            .create(ForgeRegistries.CONTAINERS, MOD_ID);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    // Blocks
    public static final RegistryObject<Block> ALCANITE_BLOCK = BLOCKS.register("alcanite_block", AlcaniteBlock::new);
    public static final RegistryObject<Block> TROPHY_CREATE = BLOCKS.register("trophy_create", trophyCreate::new);

    public static final RegistryObject<Block> TROPHY_AE2 = BLOCKS.register("trophy_ae2", trophyAe2::new);
    public static final RegistryObject<Block> TROPHY_BOTANIA = BLOCKS.register("trophy_botania", trophyBotania::new);
    public static final RegistryObject<Block> TROPHY_CROPTOPIA = BLOCKS.register("trophy_croptopia",
            trophyCroptopia::new);
    public static final RegistryObject<Block> TROPHY_DRACONIC_EVOLUTION = BLOCKS.register("trophy_draconic_evolution",
            trophyDraconicEvolution::new);
    public static final RegistryObject<Block> TROPHY_MYSTICAL_AGRICULTURE = BLOCKS
            .register("trophy_mystical_agriculture", trophyMysticalAgriculture::new);
    public static final RegistryObject<Block> TROPHY_INDUSTRIAL_FOREGOING = BLOCKS
            .register("trophy_industrial_foregoing", trophyIndustrialForegoing::new);
    public static final RegistryObject<Block> TROPHY_WOOT = BLOCKS.register("trophy_woot", trophyWoot::new);
    public static final RegistryObject<Block> TROPHY_MEKANISM = BLOCKS.register("trophy_mekanism", trophyMekanism::new);
    public static final RegistryObject<Block> TROPHY_SOLAR_FLUX = BLOCKS.register("trophy_solar_flux",
            trophySolarFlux::new);
    public static final RegistryObject<Block> TROPHY_MASTER_SWORD = BLOCKS.register("trophy_master_sword",
            trophyMasterSword::new);

    // Items
    public static final RegistryObject<Item> TROPHY_CREATE_ITEM = ITEMS.register("trophy_create",
            () -> new BlockItem(TROPHY_CREATE.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> TROPHY_AE2_ITEM = ITEMS.register("trophy_ae2",
            () -> new BlockItem(TROPHY_AE2.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> TROPHY_BOTANIA_ITEM = ITEMS.register("trophy_botania",
            () -> new BlockItem(TROPHY_BOTANIA.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> TROPHY_CROPTOPIA_ITEM = ITEMS.register("trophy_croptopia",
            () -> new BlockItem(TROPHY_CROPTOPIA.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> TROPHY_DRACONIC_EVOLUTION_ITEM = ITEMS.register(
            "trophy_draconic_evolution",
            () -> new BlockItem(TROPHY_DRACONIC_EVOLUTION.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> TROPHY_MYSTICAL_AGRICULTURE_ITEM = ITEMS.register(
            "trophy_mystical_agriculture",
            () -> new BlockItem(TROPHY_MYSTICAL_AGRICULTURE.get(),
                    new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> TROPHY_INDUSTRIAL_FOREGOING_ITEM = ITEMS.register(
            "trophy_industrial_foregoing",
            () -> new BlockItem(TROPHY_INDUSTRIAL_FOREGOING.get(),
                    new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> TROPHY_WOOT_ITEM = ITEMS.register("trophy_woot",
            () -> new BlockItem(TROPHY_WOOT.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> TROPHY_MEKANISMITEM = ITEMS.register("trophy_mekanism",
            () -> new BlockItem(TROPHY_MEKANISM.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> TROPHY_SOLAR_FLUX_ITEM = ITEMS.register("trophy_solar_flux",
            () -> new BlockItem(TROPHY_SOLAR_FLUX.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> TROPHY_MASTER_SWORD_ITEM = ITEMS.register("trophy_master_sword",
            () -> new BlockItem(TROPHY_MASTER_SWORD.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> GreenMark = ITEMS.register("green_mark", com.alcamod.items.GreenMark::new);
    public static final RegistryObject<Item> Money = ITEMS.register("money", com.alcamod.items.GreenMark::new);
    public static final RegistryObject<Item> Key = ITEMS.register("key", com.alcamod.items.Key::new);

    // Alcanite

    public static final RegistryObject<Item> Alcanite = ITEMS.register("alcanite", com.alcamod.items.Alcanite::new);
    public static final RegistryObject<Item> AlcaniteNugget = ITEMS.register("alcanite_nugget",
            com.alcamod.items.AlcaniteNugget::new);

    public static final RegistryObject<Item> AlcaniteHelmet = ITEMS.register("alcanite_helmet",
            com.alcamod.items.AlcaniteHelmet::new);

    public static final RegistryObject<Item> AlcaniteChestplate = ITEMS.register("alcanite_chestplate",
            com.alcamod.items.AlcaniteChestplate::new);

    public static final RegistryObject<Item> AlcaniteLeggings = ITEMS.register("alcanite_leggings",
            com.alcamod.items.AlcaniteLeggings::new);

    public static final RegistryObject<Item> AlcaniteBoots = ITEMS.register("alcanite_boots",
            com.alcamod.items.AlcaniteBoots::new);

    public static final RegistryObject<Item> AlcaniteSword = ITEMS.register("alcanite_sword",
            () -> new AlcaniteSword(AlcaniteTier.ALCANITE, 6, -1.5F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

    public static final RegistryObject<Item> ALCANITE_PICKAXE = ITEMS.register("alcanite_pickaxe",
            () -> new AlcanitePickaxe(AlcaniteTier.ALCANITE, 4, -2.5F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

    public static final RegistryObject<Item> ALCANITE_AXE = ITEMS.register("alcanite_axe",
            () -> new AlcaniteAxe(AlcaniteTier.ALCANITE, 8, -3.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

    public static final RegistryObject<Item> ALCANITE_SHOVEL = ITEMS.register("alcanite_shovel",
            () -> new AlcaniteShovel(AlcaniteTier.ALCANITE, 2, -3.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

    public static final RegistryObject<Item> ALCANITE_HOE = ITEMS.register("alcanite_hoe",
            () -> new AlcaniteHoe(AlcaniteTier.ALCANITE, 1, -1.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

    public static final RegistryObject<Item> ALCANITE_BLOCK_ITEM = ITEMS.register("alcanite_block",
            () -> new BlockItem(ALCANITE_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<ContainerType<DailyContainer>> DAILY_CONTAINER = CONTAINERS.register(
            "daily_container",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
                return new DailyContainer(windowId, inv, new ArrayList<>());
            }));

    private static final Logger LOGGER = LogManager.getLogger();

    // Definition Entités
    // Enregistrement des entités
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES,
            MOD_ID);

    // Définir l'EntityType de PhantomKnight

    // Enregistrement de l'EntityType de PhantomKnight
    public static final RegistryObject<EntityType<PhantomKnightEntity>> PHANTOM_KNIGHT_ENTITY = ENTITY_TYPES.register(
            "phantom_knight",
            () -> EntityType.Builder.of(PhantomKnightEntity::new, EntityClassification.MONSTER)
                    .sized(1.0f, 1.0f) // Example size, adjust as needed
                    .build(new ResourceLocation("alcamod", "phantom_knight").toString()));

    // Enregistrement de l'œuf de spawn pour PhantomKnight
    public static final RegistryObject<Item> PHANTOM_KNIGHT_SPAWN_EGG = ITEMS.register("phantom_knight_spawn_egg",
            () -> new ModSpawnEggItem(PHANTOM_KNIGHT_ENTITY::get, 0x000000, 0xffffff));

    private static int readBossSize(String bossName) {
        try {
            Path bossConfigFile = Paths.get("config/alcamod/ultimateBattle/", bossName + "/config.json");
            String json = new String(Files.readAllBytes(bossConfigFile));
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            int sizeBoss = jsonObject.get("size").getAsInt();
            return sizeBoss;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Retourne une valeur d'erreur en cas d'erreur
        }

    }

    private void createModConfigDirectory() {
        try {
            Path configPath = Paths.get("config/alcamod");
            Files.createDirectories(configPath);
            // DailyRewards
            Path dailyRewardsPath = configPath.resolve("dailyRewards");
            Files.createDirectories(dailyRewardsPath);

            Path configJson = dailyRewardsPath.resolve("config.json");
            if (!Files.exists(configJson)) {
                Files.createFile(configJson);
                writeInitialConfigdailyRewards(configJson);
            }

            Path playerDataPath = dailyRewardsPath.resolve("playerData");
            Files.createDirectories(playerDataPath);

            // UltimateBattle
            Path ultimateBattlePath = configPath.resolve("ultimateBattle");
            Files.createDirectories(ultimateBattlePath);

            // Créer un dossier pour chaque boss
            // createBossConfigDirectory(ultimateBattlePath, "PhantomKnight");

            Path ultimateBattleConfigJson = ultimateBattlePath.resolve("config.json");
            if (!Files.exists(ultimateBattleConfigJson)) {
                Files.createFile(ultimateBattleConfigJson);
                writeInitialConfigUltimateBattle(ultimateBattleConfigJson);
            }

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
            rewards.add("DIAMOND");
            rewards.add("alcamod:green_mark");

            List<String> topRewards = new ArrayList<>();
            // Ajouter des données initiales à la liste des top récompenses
            topRewards.add("alcamod:green_mark");
            topRewards.add("alcamod:green_mark");

            ConfigData configData = new ConfigData(rewards, topRewards);
            String json = gson.toJson(configData);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath.toFile()))) {
                writer.write(json);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'écriture dans le fichier config.json", e);
        }
    }

    private void writeInitialConfigUltimateBattle(Path configFilePath) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Structure de données pour les arènes
            List<ArenaConfig> arenas = new ArrayList<>();
            arenas.add(new ArenaConfig("NomArène1", new int[] { 50000, 50000, 50000 }));
            arenas.add(new ArenaConfig("NomArène2", new int[] { 50000, 50000, 50000 }));

            String json = gson.toJson(arenas);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath.toFile()))) {
                writer.write(json);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'écriture dans le fichier config.json pour UltimateBattle", e);
        }
    }

    private static class ArenaConfig {
        private final String name;
        private final int[] coordinates;

        public ArenaConfig(String name, int[] coordinates) {
            this.name = name;
            this.coordinates = coordinates;
        }
        // Getters et éventuellement setters si nécessaire
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

    private void createBossConfigDirectory(Path parentPath, String bossName) {
        try {
            Path bossPath = parentPath.resolve(bossName);
            Files.createDirectories(bossPath);

            Path configJson = bossPath.resolve("config.json");
            if (!Files.exists(configJson)) {
                Files.createFile(configJson);
                writeInitialConfigBoss(configJson, bossName);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la création du dossier de configuration pour le boss " + bossName, e);
        }
    }

    private void writeInitialConfigBoss(Path configFilePath, String bossName) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            BossConfig bossConfig = new BossConfig(
                    1, // Tier
                    new int[] { 50, 100 }, // PV
                    new int[] { 5, 10 }, // Attaque
                    new int[] { 2, 5 }, // Défense
                    new int[] { 1, 3 }, // Vitesse
                    2, // Taille
                    new String[] { "item1", "item2" } // Loot table
            );

            String json = gson.toJson(bossConfig);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath.toFile()))) {
                writer.write(json);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'écriture dans le fichier config.json pour le boss " + bossName, e);
        }
    }

    private static class BossConfig {
        private final int tier;
        private final int[] pv;
        private final int[] attack;
        private final int[] defense;
        private final int[] speed;
        private final int size; // Taille
        private final String[] lootTable;

        public BossConfig(int tier, int[] pv, int[] attack, int[] defense, int[] speed, int size, String[] lootTable) {
            this.pv = pv;
            this.tier = tier;
            this.attack = attack;
            this.defense = defense;
            this.speed = speed;
            this.size = size;
            this.lootTable = lootTable;
        }
        // Getters et éventuellement setters si nécessaire
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // Enregistrement de l'écran (GUI) avec le conteneur
        ScreenManager.register(Alcamod.DAILY_CONTAINER.get(), DailyGui::new);

        // Enregistrement du rendu de l'entité
        RenderingRegistry.registerEntityRenderingHandler(PHANTOM_KNIGHT_ENTITY.get(), PhantomKnightRenderer::new);
    }

    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) {
        TodayCommand.register(event.getDispatcher());
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            // Configuration commune ici
        }
    }

    public Alcamod() {
        LOGGER.debug("Logger : Initialize");
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Enregistrez tous vos éléments, blocs, types d'entité, etc. ici
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        CONTAINERS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);

        // Enregistrez les gestionnaires de réseau et les écouteurs d'événements une
        // seule fois
        NetworkHandler.registerMessages();
        modEventBus.addListener(this::doClientStuff);

        createModConfigDirectory();
    }

}

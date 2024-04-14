package com.alcamod;

import com.alcamod.blocks.trophy.TrophyBlock;
import com.alcamod.material.AlcaniteArmorMaterial;
import com.alcamod.material.AlcaniteTier;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(Alcamod.MODID)
public class Alcamod {
    public static final String MODID = "alcamod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Enregistrement d'un nouveau bloc et son item correspondant
    //Trophés
    public static final RegistryObject<Block> TROPHY_AE2_BLOCK = BLOCKS.register("trophy_ae2", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_AE2_ITEM = ITEMS.register("trophy_ae2", () -> new BlockItem(TROPHY_AE2_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> TROPHY_BOTANIA_BLOCK = BLOCKS.register("trophy_botania", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_BOTANIA_ITEM = ITEMS.register("trophy_botania", () -> new BlockItem(TROPHY_BOTANIA_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> TROPHY_CREATE_BLOCK = BLOCKS.register("trophy_create", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_CREATE_ITEM = ITEMS.register("trophy_create", () -> new BlockItem(TROPHY_CREATE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> TROPHY_CROPTOPIA_BLOCK = BLOCKS.register("trophy_croptopia", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_CROPTOPIA_ITEM = ITEMS.register("trophy_croptopia", () -> new BlockItem(TROPHY_CROPTOPIA_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> TROPHY_DRACONIC_EVOLUTION_BLOCK = BLOCKS.register("trophy_draconic_evolution", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_DRACONIC_EVOLUTION_ITEM = ITEMS.register("trophy_draconic_evolution", () -> new BlockItem(TROPHY_DRACONIC_EVOLUTION_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> TROPHY_INDUSTRIAL_FOREGOING_BLOCK = BLOCKS.register("trophy_industrial_foregoing", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_INDUSTRIAL_FOREGOING_ITEM = ITEMS.register("trophy_industrial_foregoing", () -> new BlockItem(TROPHY_INDUSTRIAL_FOREGOING_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> TROPHY_MASTER_SWORD_BLOCK = BLOCKS.register("trophy_master_sword", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_MASTER_SWORD_ITEM = ITEMS.register("trophy_master_sword", () -> new BlockItem(TROPHY_MASTER_SWORD_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> TROPHY_MEKANISM_BLOCK = BLOCKS.register("trophy_mekanism", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_MEKANISM_ITEM = ITEMS.register("trophy_mekanism", () -> new BlockItem(TROPHY_MEKANISM_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> TROPHY_MYSTICAL_AGRICULTURE_BLOCK = BLOCKS.register("trophy_mystical_agriculture", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_MYSTICAL_AGRICULTURE_ITEM = ITEMS.register("trophy_mystical_agriculture", () -> new BlockItem(TROPHY_MYSTICAL_AGRICULTURE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> TROPHY_SOLAR_FLUX_BLOCK = BLOCKS.register("trophy_solar_flux", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_SOLAR_FLUX_ITEM = ITEMS.register("trophy_solar_flux", () -> new BlockItem(TROPHY_SOLAR_FLUX_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> TROPHY_WOOT_BLOCK = BLOCKS.register("trophy_woot", () -> new TrophyBlock());
    public static final RegistryObject<Item> TROPHY_WOOT_ITEM = ITEMS.register("trophy_woot", () -> new BlockItem(TROPHY_WOOT_BLOCK.get(), new Item.Properties()));

    //Items Alcanite
    public static final RegistryObject<Block> ALCANITE_BLOCK = BLOCKS.register("alcanite_block", () -> new TrophyBlock());
    public static final RegistryObject<Item> ALCANITE_BLOCK_ITEM = ITEMS.register("alcanite_block", () -> new BlockItem(ALCANITE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> ALCANITE_ITEM = ITEMS.register("alcanite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALCANITE_NUGGET_ITEM = ITEMS.register("alcanite_nugget", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ALCANITE_HELMET = ITEMS.register("alcanite_helmet", () -> new ArmorItem(AlcaniteArmorMaterial.ALCANITE, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ALCANITE_CHESTPLATE = ITEMS.register("alcanite_chestplate", () -> new ArmorItem(AlcaniteArmorMaterial.ALCANITE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ALCANITE_LEGGINGS = ITEMS.register("alcanite_leggings", () -> new ArmorItem(AlcaniteArmorMaterial.ALCANITE, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ALCANITE_BOOTS = ITEMS.register("alcanite_boots", () -> new ArmorItem(AlcaniteArmorMaterial.ALCANITE, ArmorItem.Type.BOOTS, new Item.Properties()));
    
    public static final RegistryObject<Item> ALCANITE_SWORD = ITEMS.register("alcanite_sword", () -> new SwordItem(AlcaniteTier.AlcaniteTier,20-21,3.5f-4.0f, new Item.Properties()));
    public static final RegistryObject<Item> ALCANITE_PICKAXE = ITEMS.register("alcanite_pickaxe", () -> new PickaxeItem(AlcaniteTier.AlcaniteTier,12-21,2.0f-4.0f, new Item.Properties()));
    public static final RegistryObject<Item> ALCANITE_AXE = ITEMS.register("alcanite_axe", () -> new AxeItem(AlcaniteTier.AlcaniteTier, 42-21, 0.5f-4.0f, new Item.Properties()));
    public static final RegistryObject<Item> ALCANITE_SHOVEL = ITEMS.register("alcanite_shovel", () -> new ShovelItem(AlcaniteTier.AlcaniteTier, 14-21, 0.8f-4.0f, new Item.Properties()));
    public static final RegistryObject<Item> ALCANITE_HOE = ITEMS.register("alcanite_hoe", () -> new HoeItem(AlcaniteTier.AlcaniteTier, 1-21, 0.2f-4.0f, new Item.Properties()));

    //Items Divers
    public static final RegistryObject<Item> MONEY_ITEM = ITEMS.register("money", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_MARK_ITEM = ITEMS.register("green_mark", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KEY_ITEM = ITEMS.register("key", () -> new Item(new Item.Properties()));



    public static final RegistryObject<CreativeModeTab> ALCAMOD_TAB = TABS.register("alcamod_tab", () -> CreativeModeTab.builder()
    .icon(() -> TROPHY_AE2_ITEM.get().getDefaultInstance()) // Définit l'icône de l'onglet, ici avec le premier trophée pour l'exemple
    .displayItems((parameters, output) -> {
        // Ajouter tous les items de trophée à l'onglet
        output.accept(TROPHY_AE2_ITEM.get());
        output.accept(TROPHY_BOTANIA_ITEM.get());
        output.accept(TROPHY_CREATE_ITEM.get());
        output.accept(TROPHY_CROPTOPIA_ITEM.get());
        output.accept(TROPHY_DRACONIC_EVOLUTION_ITEM.get());
        output.accept(TROPHY_INDUSTRIAL_FOREGOING_ITEM.get());
        output.accept(TROPHY_MASTER_SWORD_ITEM.get());
        output.accept(TROPHY_MEKANISM_ITEM.get());
        output.accept(TROPHY_MYSTICAL_AGRICULTURE_ITEM.get());
        output.accept(TROPHY_SOLAR_FLUX_ITEM.get());
        output.accept(TROPHY_WOOT_ITEM.get());
        output.accept(MONEY_ITEM.get());
        output.accept(GREEN_MARK_ITEM.get());
        output.accept(KEY_ITEM.get());
        output.accept(ALCANITE_BLOCK_ITEM.get());
        output.accept(ALCANITE_ITEM.get());
        output.accept(ALCANITE_NUGGET_ITEM.get());
        output.accept(ALCANITE_HELMET.get());
        output.accept(ALCANITE_CHESTPLATE.get());
        output.accept(ALCANITE_LEGGINGS.get());
        output.accept(ALCANITE_BOOTS.get());
        output.accept(ALCANITE_SWORD.get());
        output.accept(ALCANITE_PICKAXE.get());
        output.accept(ALCANITE_AXE.get());
        output.accept(ALCANITE_SHOVEL.get());
        output.accept(ALCANITE_HOE.get());
    }).build());


    public Alcamod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        TABS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

// Classe interne pour gérer les événements spécifiques au client (graphique, interactions utilisateurs...)
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public static class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Configuration client, comme la mise en place de rendus ou de contrôleurs
        LOGGER.info("HELLO FROM CLIENT SETUP");
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());  // Affiche le nom de l'utilisateur
    }
}
}

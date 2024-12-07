package net.pitan76.advancedreborn;

import net.minecraft.item.Item;
import net.pitan76.advancedreborn.armormaterials.BBArmorMaterial;
import net.pitan76.advancedreborn.armormaterials.NanoArmorMaterial;
import net.pitan76.advancedreborn.items.*;
import net.pitan76.mcpitanlib.api.item.ArmorEquipmentType;
import net.pitan76.mcpitanlib.api.item.CompatFoodComponent;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.v2.ItemSettingsBuilder;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.config.TechRebornConfig;

import static net.pitan76.advancedreborn.AdvancedReborn.*;

public class Items {
    public static ItemSettingsBuilder nothingSettings = new ItemSettingsBuilder();
    public static ItemSettingsBuilder betterBatpackSettings = new ItemSettingsBuilder().maxCount(1).enchantability(15);

    public static RegistryResult<Item> CHARGE_PAD_MK_1;
    public static RegistryResult<Item> CHARGE_PAD_MK_2;
    public static RegistryResult<Item> CHARGE_PAD_MK_3;
    public static RegistryResult<Item> CHARGE_PAD_MK_4;
    public static RegistryResult<Item> CHARGE_PAD_MK_FINAL;

    public static RegistryResult<Item> RAY_SOLAR_1;
    public static RegistryResult<Item> RAY_SOLAR_2;
    public static RegistryResult<Item> RAY_SOLAR_3;
    public static RegistryResult<Item> RAY_SOLAR_4;
    public static RegistryResult<Item> RAY_GENERATOR_1;
    public static RegistryResult<Item> RAY_GENERATOR_2;
    public static RegistryResult<Item> RAY_GENERATOR_3;
    public static RegistryResult<Item> RAY_GENERATOR_4;
    public static RegistryResult<Item> RAY_GENERATOR_5;
    public static RegistryResult<Item> RAY_GENERATOR_6;
    public static RegistryResult<Item> RAY_GENERATOR_7;
    public static RegistryResult<Item> RAY_GENERATOR_8;
    public static RegistryResult<Item> RAY_GENERATOR_9;
    public static RegistryResult<Item> RAY_GENERATOR_10;

    public static RegistryResult<Item> INDUCTION_FURNACE;
    public static RegistryResult<Item> ROTARY_GRINDER;
    public static RegistryResult<Item> CENTRIFUGAL_EXTRACTOR;
    public static RegistryResult<Item> SINGULARITY_COMPRESSOR;
    public static RegistryResult<Item> CANNING_MACHINE;
    public static RegistryResult<Item> RENAMING_MACHINE;
    public static RegistryResult<Item> TELEPORTER;
    public static RegistryResult<Item> FARMING_MACHINE;
    public static RegistryResult<Item> LOGGING_MACHINE;
    public static RegistryResult<Item> FERTILIZER_SPREADER;
    public static RegistryResult<Item> ENCHANTMENT_EXTRACTOR;

    public static RegistryResult<Item> FREQ_TRANS;
    public static RegistryResult<Item> CONFIG_WRENCH;
    //public static Item FORGE_HAMMER;
    //public static Item ADVANCED_FORGE_HAMMER;

    static {
        // 実装しないでおく
        //FORGE_HAMMER = new ForgeHammer(nothingSettings.recipeRemainder(FORGE_HAMMER), 80);
        //ADVANCED_FORGE_HAMMER = new ForgeHammer(nothingSettings.recipeRemainder(ADVANCED_FORGE_HAMMER), 360);
    }

    public static RegistryResult<Item> NANO_SUIT_HELMET;
    public static RegistryResult<Item> NANO_SUIT_BODY_ARMOR;
    public static RegistryResult<Item> NANO_SUIT_LEGGINGS;
    public static RegistryResult<Item> NANO_SUIT_BOOTS;

    // 強化バッテリー
    public static RegistryResult<Item> ADVANCED_BATTERY;
    public static RegistryResult<Item> ADVANCED_BATTERY_2;
    public static RegistryResult<Item> ADVANCED_BATTERY_3;
    public static RegistryResult<Item> ADVANCED_BATTERY_4;
    public static RegistryResult<Item> ADVANCED_BATTERY_5;

    // ダイナマイト (予定: 時限爆弾)
    public static RegistryResult<Item> DYNAMITE;
    public static RegistryResult<Item> STICKY_DYNAMITE;
    public static RegistryResult<Item> INDUSTRIAL_DYNAMITE;
    public static RegistryResult<Item> INDUSTRIAL_STICKY_DYNAMITE;
    public static RegistryResult<Item> INDUSTRIAL_TNT;

    // ライト、足場(鉄) 強化石材  ネーミングマシン
    public static RegistryResult<Item> LIGHT;

    public static RegistryResult<Item> CARDBOARD_BOX;
    public static RegistryResult<Item> CARDBOARD_BOX_MINETARO;
    public static RegistryResult<Item> CARDBOARD_BOX_MINEZON;
    public static RegistryResult<Item> CARDBOARD_BOX_NOTHING;

    // 缶
    public static RegistryResult<Item> EMPTY_CAN;
    public static RegistryResult<Item> FUEL_CAN;

    public static CompatFoodComponent CAN_FOOD_COMPONENT = new CompatFoodComponent().setSnack().setHunger(2).setSaturation(2);
    public static RegistryResult<Item> FOOD_CAN;

    // Better Batpack
    public static RegistryResult<Item> BATPACK_4;
    public static RegistryResult<Item> BATPACK_16;
    public static RegistryResult<Item> BATPACK_64;
    public static RegistryResult<Item> BATPACK_128;



    // 素材アイテム
    public static RegistryResult<Item> DUCT_TAPE;
    public static RegistryResult<Item> CARDBOARD_SHEET;

    //public static RegistryResult<Item> ADD_ITEMS;

    public static void init() {
        CHARGE_PAD_MK_1 = registry.registerItem(_id("charge_pad"), () -> ItemUtil.create(Blocks.CHARGE_PAD_MK_1.getOrNull(), CompatibleItemSettings.of(_id("charge_pad")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CHARGE_PAD_MK_2 = registry.registerItem(_id("charge_pad_2"), () -> ItemUtil.create(Blocks.CHARGE_PAD_MK_2.getOrNull(), CompatibleItemSettings.of(_id("charge_pad_2")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CHARGE_PAD_MK_3 = registry.registerItem(_id("charge_pad_3"), () -> ItemUtil.create(Blocks.CHARGE_PAD_MK_3.getOrNull(), CompatibleItemSettings.of(_id("charge_pad_3")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CHARGE_PAD_MK_4 = registry.registerItem(_id("charge_pad_4"), () -> ItemUtil.create(Blocks.CHARGE_PAD_MK_4.getOrNull(), CompatibleItemSettings.of(_id("charge_pad_4")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CHARGE_PAD_MK_FINAL = registry.registerItem(_id("charge_pad_final"), () -> ItemUtil.create(Blocks.CHARGE_PAD_MK_FINAL.getOrNull(), CompatibleItemSettings.of(_id("charge_pad_final")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_SOLAR_1 = registry.registerItem(_id("ray_solar_panel"), () -> ItemUtil.create(Blocks.RAY_SOLAR_1.getOrNull(), CompatibleItemSettings.of(_id("ray_solar_panel")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_SOLAR_2 = registry.registerItem(_id("ray_solar_panel_2"), () -> ItemUtil.create(Blocks.RAY_SOLAR_2.getOrNull(), CompatibleItemSettings.of(_id("ray_solar_panel_2")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_SOLAR_3 = registry.registerItem(_id("ray_solar_panel_3"), () -> ItemUtil.create(Blocks.RAY_SOLAR_3.getOrNull(), CompatibleItemSettings.of(_id("ray_solar_panel_3")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_SOLAR_4 = registry.registerItem(_id("ray_solar_panel_4"), () -> ItemUtil.create(Blocks.RAY_SOLAR_4.getOrNull(), CompatibleItemSettings.of(_id("ray_solar_panel_4")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_GENERATOR_1 = registry.registerItem(_id("ray_generator"), () -> ItemUtil.create(Blocks.RAY_GENERATOR_1.getOrNull(), CompatibleItemSettings.of(_id("ray_generator")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_GENERATOR_2 = registry.registerItem(_id("ray_generator_2"), () -> ItemUtil.create(Blocks.RAY_GENERATOR_2.getOrNull(), CompatibleItemSettings.of(_id("ray_generator_2")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_GENERATOR_3 = registry.registerItem(_id("ray_generator_3"), () -> ItemUtil.create(Blocks.RAY_GENERATOR_3.getOrNull(), CompatibleItemSettings.of(_id("ray_generator_3")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_GENERATOR_4 = registry.registerItem(_id("ray_generator_4"), () -> ItemUtil.create(Blocks.RAY_GENERATOR_4.getOrNull(), CompatibleItemSettings.of(_id("ray_generator_4")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_GENERATOR_5 = registry.registerItem(_id("ray_generator_5"), () -> ItemUtil.create(Blocks.RAY_GENERATOR_5.getOrNull(), CompatibleItemSettings.of(_id("ray_generator_5")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_GENERATOR_6 = registry.registerItem(_id("ray_generator_6"), () -> ItemUtil.create(Blocks.RAY_GENERATOR_6.getOrNull(), CompatibleItemSettings.of(_id("ray_generator_6")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_GENERATOR_7 = registry.registerItem(_id("ray_generator_7"), () -> ItemUtil.create(Blocks.RAY_GENERATOR_7.getOrNull(), CompatibleItemSettings.of(_id("ray_generator_7")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_GENERATOR_8 = registry.registerItem(_id("ray_generator_8"), () -> ItemUtil.create(Blocks.RAY_GENERATOR_8.getOrNull(), CompatibleItemSettings.of(_id("ray_generator_8")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_GENERATOR_9 = registry.registerItem(_id("ray_generator_9"), () -> ItemUtil.create(Blocks.RAY_GENERATOR_9.getOrNull(), CompatibleItemSettings.of(_id("ray_generator_9")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RAY_GENERATOR_10 = registry.registerItem(_id("ray_generator_10"), () -> ItemUtil.create(Blocks.RAY_GENERATOR_10.getOrNull(), CompatibleItemSettings.of(_id("ray_generator_10")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        INDUCTION_FURNACE = registry.registerItem(_id("induction_furnace"), () -> ItemUtil.create(Blocks.INDUCTION_FURNACE.getOrNull(), CompatibleItemSettings.of(_id("induction_furnace")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        ROTARY_GRINDER = registry.registerItem(_id("rotary_grinder"), () -> ItemUtil.create(Blocks.ROTARY_GRINDER.getOrNull(), CompatibleItemSettings.of(_id("rotary_grinder")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CENTRIFUGAL_EXTRACTOR = registry.registerItem(_id("centrifugal_extractor"), () -> ItemUtil.create(Blocks.CENTRIFUGAL_EXTRACTOR.getOrNull(), CompatibleItemSettings.of(_id("centrifugal_extractor")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        SINGULARITY_COMPRESSOR = registry.registerItem(_id("singularity_compressor"), () -> ItemUtil.create(Blocks.SINGULARITY_COMPRESSOR.getOrNull(), CompatibleItemSettings.of(_id("singularity_compressor")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CANNING_MACHINE = registry.registerItem(_id("canning_machine"), () -> ItemUtil.create(Blocks.CANNING_MACHINE.getOrNull(), CompatibleItemSettings.of(_id("canning_machine")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        RENAMING_MACHINE = registry.registerItem(_id("renaming_machine"), () -> ItemUtil.create(Blocks.RENAMING_MACHINE.getOrNull(), CompatibleItemSettings.of(_id("renaming_machine")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        TELEPORTER = registry.registerItem(_id("teleporter"), () -> ItemUtil.create(Blocks.TELEPORTER.getOrNull(), CompatibleItemSettings.of(_id("teleporter")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        FARMING_MACHINE = registry.registerItem(_id("farming_machine"), () -> ItemUtil.create(Blocks.FARMING_MACHINE.getOrNull(), CompatibleItemSettings.of(_id("farming_machine")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        LOGGING_MACHINE = registry.registerItem(_id("logging_machine"), () -> ItemUtil.create(Blocks.LOGGING_MACHINE.getOrNull(), CompatibleItemSettings.of(_id("logging_machine")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        FERTILIZER_SPREADER = registry.registerItem(_id("fertilizer_spreader"), () -> ItemUtil.create(Blocks.FERTILIZER_SPREADER.getOrNull(), CompatibleItemSettings.of(_id("fertilizer_spreader")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        ENCHANTMENT_EXTRACTOR = registry.registerItem(_id("enchantment_extractor"), () -> ItemUtil.create(Blocks.ENCHANTMENT_EXTRACTOR.getOrNull(), CompatibleItemSettings.of(_id("enchantment_extractor")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        FREQ_TRANS = registry.registerItem(_id("freq_trans"), () -> new FreqTrans(CompatibleItemSettings.of(_id("freq_trans")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1)));
        CONFIG_WRENCH = registry.registerItem(_id("config_wrench"), () -> new ConfigWrench(CompatibleItemSettings.of(_id("config_wrench")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1)));

        NANO_SUIT_HELMET = registry.registerItem(_id("nano_helmet"), () -> new NanoSuitItem(NanoArmorMaterial.NANO, ArmorEquipmentType.HEAD, CompatibleItemSettings.of(_id("nano_suit_helmet")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1)));
        NANO_SUIT_BODY_ARMOR = registry.registerItem(_id("nano_chestplate"), () -> new NanoSuitItem(NanoArmorMaterial.NANO, ArmorEquipmentType.CHEST, CompatibleItemSettings.of(_id("nano_suit_body_armor")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1)));
        NANO_SUIT_LEGGINGS = registry.registerItem(_id("nano_leggings"), () -> new NanoSuitItem(NanoArmorMaterial.NANO, ArmorEquipmentType.LEGS, CompatibleItemSettings.of(_id("nano_suit_leggings")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1)));
        NANO_SUIT_BOOTS = registry.registerItem(_id("nano_boots"), () -> new NanoSuitItem(NanoArmorMaterial.NANO, ArmorEquipmentType.FEET, CompatibleItemSettings.of(_id("nano_suit_boots")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1)));

        ADVANCED_BATTERY = registry.registerItem(_id("advanced_battery"), () -> new AdvancedBattery(CompatibleItemSettings.of(_id("advanced_battery")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1), 8 * TechRebornConfig.redCellBatteryMaxCharge, RcEnergyTier.HIGH));
        ADVANCED_BATTERY_2 = registry.registerItem(_id("advanced_battery_2"), () -> new AdvancedBattery(CompatibleItemSettings.of(_id("advanced_battery_2")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1), 64 * TechRebornConfig.redCellBatteryMaxCharge, RcEnergyTier.HIGH));
        ADVANCED_BATTERY_3 = registry.registerItem(_id("advanced_battery_3"), () -> new AdvancedBattery(CompatibleItemSettings.of(_id("advanced_battery_3")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1), 512 * TechRebornConfig.redCellBatteryMaxCharge, RcEnergyTier.HIGH));
        ADVANCED_BATTERY_4 = registry.registerItem(_id("advanced_battery_4"), () -> new AdvancedBattery(CompatibleItemSettings.of(_id("advanced_battery_4")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1), 4096 * TechRebornConfig.redCellBatteryMaxCharge, RcEnergyTier.HIGH));
        ADVANCED_BATTERY_5 = registry.registerItem(_id("advanced_battery_5"), () -> new AdvancedBattery(CompatibleItemSettings.of(_id("advanced_battery_5")).addGroup(AdvancedReborn.AR_GROUP).maxCount(1).maxDamage(-1), 32768 * TechRebornConfig.redCellBatteryMaxCharge, RcEnergyTier.EXTREME));
        DYNAMITE = registry.registerItem(_id("dynamite"), () -> new Dynamite(CompatibleItemSettings.of(_id("dynamite")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        STICKY_DYNAMITE = registry.registerItem(_id("sticky_dynamite"), () -> new Dynamite(CompatibleItemSettings.of(_id("sticky_dynamite")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64),true));
        INDUSTRIAL_DYNAMITE = registry.registerItem(_id("industrial_dynamite"), () -> new Dynamite(CompatibleItemSettings.of(_id("industrial_dynamite")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64),false, true));
        INDUSTRIAL_STICKY_DYNAMITE = registry.registerItem(_id("industrial_sticky_dynamite"), () -> new Dynamite(CompatibleItemSettings.of(_id("industrial_sticky_dynamite")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64),true, true));
        INDUSTRIAL_TNT = registry.registerItem(_id("industrial_tnt"), () -> ItemUtil.create(Blocks.INDUSTRIAL_TNT.getOrNull(), CompatibleItemSettings.of(_id("industrial_tnt")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        LIGHT = registry.registerItem(_id("light"), () -> ItemUtil.create(Blocks.LIGHT.getOrNull(), CompatibleItemSettings.of(_id("light")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CARDBOARD_BOX = registry.registerItem(_id("cardboard_box"), () -> ItemUtil.create(Blocks.CARDBOARD_BOX.getOrNull(), CompatibleItemSettings.of(_id("cardboard_box")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CARDBOARD_BOX_MINETARO = registry.registerItem(_id("cardboard_box_minetaro"), () -> ItemUtil.create(Blocks.CARDBOARD_BOX_MINETARO.getOrNull(), CompatibleItemSettings.of(_id("cardboard_box_minetaro")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CARDBOARD_BOX_MINEZON = registry.registerItem(_id("cardboard_box_minezon"), () -> ItemUtil.create(Blocks.CARDBOARD_BOX_MINEZON.getOrNull(), CompatibleItemSettings.of(_id("cardboard_box_minezon")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CARDBOARD_BOX_NOTHING = registry.registerItem(_id("cardboard_box_nothing_logo"), () -> ItemUtil.create(Blocks.CARDBOARD_BOX_NOTHING.getOrNull(), CompatibleItemSettings.of(_id("cardboard_box_nothing_logo")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        EMPTY_CAN = registry.registerItem(_id("empty_can"), () -> new CompatItem(CompatibleItemSettings.of(_id("empty_can")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        FUEL_CAN = registry.registerItem(_id("fuel_can"), () -> new FuelCanItem(CompatibleItemSettings.of(_id("fuel_can")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64).recipeRemainder(EMPTY_CAN.getOrNull())));
        FOOD_CAN = registry.registerItem(_id("food_can"), () -> new FoodCanItem(CompatibleItemSettings.of(_id("food_can")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64).recipeRemainder(EMPTY_CAN.getOrNull()).food(CAN_FOOD_COMPONENT)));
        BATPACK_4 = registry.registerItem(CompatIdentifier.of("better_batpack", "batpack4"), () -> new BetterBatpackItem(betterBatpackSettings.build(CompatIdentifier.of("better_batpack", "batpack4")), TechRebornConfig.lithiumBatpackCharge * 4, new BBArmorMaterial("batpack4"), RcEnergyTier.MEDIUM));
        BATPACK_16 = registry.registerItem(CompatIdentifier.of("better_batpack", "batpack16"), () -> new BetterBatpackItem(betterBatpackSettings.build(CompatIdentifier.of("better_batpack", "batpack16")), TechRebornConfig.lithiumBatpackCharge * 16, new BBArmorMaterial("batpack16"), RcEnergyTier.HIGH));
        BATPACK_64 = registry.registerItem(CompatIdentifier.of("better_batpack", "batpack64"), () -> new BetterBatpackItem(betterBatpackSettings.build(CompatIdentifier.of("better_batpack", "batpack64")), TechRebornConfig.lithiumBatpackCharge * 64, new BBArmorMaterial("batpack64"), RcEnergyTier.EXTREME));
        BATPACK_128 = registry.registerItem(CompatIdentifier.of("better_batpack", "batpack128"), () -> new BetterBatpackItem(betterBatpackSettings.build(CompatIdentifier.of("better_batpack", "batpack128")), TechRebornConfig.lithiumBatpackCharge * 128, new BBArmorMaterial("batpack128"), RcEnergyTier.INSANE));
        DUCT_TAPE = registry.registerItem(_id("duct_tape"), () -> new CompatItem(CompatibleItemSettings.of(_id("duct_tape")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
        CARDBOARD_SHEET = registry.registerItem(_id("cardboard_sheet"), () -> new CompatItem(CompatibleItemSettings.of(_id("cardboard_sheet")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));

        registry.registerFuel(() -> FUEL_CAN.getOrNull(), 500);
        //ADD_ITEMS = registry.registerItem(_id("add_items"), () -> new AddItems(CompatibleItemSettings.of(_id("add_items")).addGroup(AdvancedReborn.AR_GROUP).maxCount(64)));
    }
}

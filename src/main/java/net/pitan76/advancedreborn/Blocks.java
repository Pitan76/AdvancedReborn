package net.pitan76.advancedreborn;

import net.minecraft.block.Block;
import net.pitan76.advancedreborn.blocks.*;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.sound.CompatBlockSoundGroup;

import static net.pitan76.advancedreborn.AdvancedReborn.*;

public class Blocks {

    public static BlockSettingsBuilder baseSettings = new BlockSettingsBuilder().material(CompatibleMaterial.METAL).requiresTool().strength(2, 8);

    public static RegistryResult<Block> CHARGE_PAD_MK_1;
    public static RegistryResult<Block> CHARGE_PAD_MK_2;
    public static RegistryResult<Block> CHARGE_PAD_MK_3;
    public static RegistryResult<Block> CHARGE_PAD_MK_4;
    public static RegistryResult<Block> CHARGE_PAD_MK_FINAL;

    public static RegistryResult<Block> RAY_SOLAR_1;
    public static RegistryResult<Block> RAY_SOLAR_2;
    public static RegistryResult<Block> RAY_SOLAR_3;
    public static RegistryResult<Block> RAY_SOLAR_4;

    public static RegistryResult<Block> RAY_GENERATOR_1;
    public static RegistryResult<Block> RAY_GENERATOR_2;
    public static RegistryResult<Block> RAY_GENERATOR_3;
    public static RegistryResult<Block> RAY_GENERATOR_4;
    public static RegistryResult<Block> RAY_GENERATOR_5;
    public static RegistryResult<Block> RAY_GENERATOR_6;
    public static RegistryResult<Block> RAY_GENERATOR_7;
    public static RegistryResult<Block> RAY_GENERATOR_8;
    public static RegistryResult<Block> RAY_GENERATOR_9;
    public static RegistryResult<Block> RAY_GENERATOR_10;


    public static RegistryResult<Block> INDUCTION_FURNACE;
    public static RegistryResult<Block> ROTARY_GRINDER;
    public static RegistryResult<Block> CENTRIFUGAL_EXTRACTOR;
    public static RegistryResult<Block> SINGULARITY_COMPRESSOR;
    public static RegistryResult<Block> CANNING_MACHINE;
    public static RegistryResult<Block> RENAMING_MACHINE;
    public static RegistryResult<Block> TELEPORTER;
    public static RegistryResult<Block> FARMING_MACHINE;
    public static RegistryResult<Block> LOGGING_MACHINE;
    public static RegistryResult<Block> FERTILIZER_SPREADER;
    public static RegistryResult<Block> ENCHANTMENT_EXTRACTOR;


    //breakByHand
    public static RegistryResult<Block> CARDBOARD_BOX;
    public static RegistryResult<Block> CARDBOARD_BOX_MINEZON;
    public static RegistryResult<Block> CARDBOARD_BOX_MINETARO;
    public static RegistryResult<Block> CARDBOARD_BOX_NOTHING;

    public static RegistryResult<Block> LIGHT;
    // breakByHand

    public static RegistryResult<Block> INDUSTRIAL_TNT;

    public static void init() {
        CHARGE_PAD_MK_1 = registry.registerBlock(_id("charge_pad"), () -> new ChargePad(baseSettings.build(_id("charge_pad")), 4));
        CHARGE_PAD_MK_2 = registry.registerBlock(_id("charge_pad_2"), () -> new ChargePad(baseSettings.build(_id("charge_pad_2")), 16));
        CHARGE_PAD_MK_3 = registry.registerBlock(_id("charge_pad_3"), () -> new ChargePad(baseSettings.build(_id("charge_pad_3")), 64));
        CHARGE_PAD_MK_4 = registry.registerBlock(_id("charge_pad_4"), () -> new ChargePad(baseSettings.build(_id("charge_pad_4")), 128));
        CHARGE_PAD_MK_FINAL = registry.registerBlock(_id("charge_pad_final"), () -> new ChargePadFinal(baseSettings.build(_id("charge_pad_final")), 256));
        RAY_SOLAR_1 = registry.registerBlock(_id("ray_solar_panel"), () -> new RaySolar(baseSettings.build(_id("ray_solar_panel")), 1, false));
        RAY_SOLAR_2 = registry.registerBlock(_id("ray_solar_panel_2"), () -> new RaySolar(baseSettings.build(_id("ray_solar_panel_2")), 8, false));
        RAY_SOLAR_3 = registry.registerBlock(_id("ray_solar_panel_3"), () -> new RaySolar(baseSettings.build(_id("ray_solar_panel_3")), 64, false));
        RAY_SOLAR_4 = registry.registerBlock(_id("ray_solar_panel_4"), () -> new RaySolar(baseSettings.build(_id("ray_solar_panel_4")), 512, false));
        RAY_GENERATOR_1 = registry.registerBlock(_id("ray_generator"), () -> new RaySolar(baseSettings.build(_id("ray_generator")), 2, true));
        RAY_GENERATOR_2 = registry.registerBlock(_id("ray_generator_2"), () -> new RaySolar(baseSettings.build(_id("ray_generator_2")), 8, true));
        RAY_GENERATOR_3 = registry.registerBlock(_id("ray_generator_3"), () -> new RaySolar(baseSettings.build(_id("ray_generator_3")), 32, true));
        RAY_GENERATOR_4 = registry.registerBlock(_id("ray_generator_4"), () -> new RaySolar(baseSettings.build(_id("ray_generator_4")), 128, true));
        RAY_GENERATOR_5 = registry.registerBlock(_id("ray_generator_5"), () -> new RaySolar(baseSettings.build(_id("ray_generator_5")), 512, true));
        RAY_GENERATOR_6 = registry.registerBlock(_id("ray_generator_6"), () -> new RaySolar(baseSettings.build(_id("ray_generator_6")), 2048, true));
        RAY_GENERATOR_7 = registry.registerBlock(_id("ray_generator_7"), () -> new RaySolar(baseSettings.build(_id("ray_generator_7")), 8192, true));
        RAY_GENERATOR_8 = registry.registerBlock(_id("ray_generator_8"), () -> new RaySolar(baseSettings.build(_id("ray_generator_8")), 32768, true));
        RAY_GENERATOR_9 = registry.registerBlock(_id("ray_generator_9"), () -> new RaySolar(baseSettings.build(_id("ray_generator_9")), 131072, true));
        RAY_GENERATOR_10 = registry.registerBlock(_id("ray_generator_10"), () -> new RaySolar(baseSettings.build(_id("ray_generator_10")), 532480, true));
        INDUCTION_FURNACE = registry.registerBlock(_id("induction_furnace"), () -> new InductionFurnace(baseSettings.build(_id("induction_furnace"))));
        ROTARY_GRINDER = registry.registerBlock(_id("rotary_grinder"), () -> new RotaryGrinder(baseSettings.build(_id("rotary_grinder"))));
        CENTRIFUGAL_EXTRACTOR = registry.registerBlock(_id("centrifugal_extractor"), () -> new CentrifugalExtractor(baseSettings.build(_id("centrifugal_extractor"))));
        SINGULARITY_COMPRESSOR = registry.registerBlock(_id("singularity_compressor"), () -> new SingularityCompressor(baseSettings.build(_id("singularity_compressor"))));
        CANNING_MACHINE = registry.registerBlock(_id("canning_machine"), () -> new CanningMachine(baseSettings.build(_id("canning_machine"))));
        RENAMING_MACHINE = registry.registerBlock(_id("renaming_machine"), () -> new RenamingMachine(baseSettings.build(_id("renaming_machine"))));
        TELEPORTER = registry.registerBlock(_id("teleporter"), () -> new Teleporter(baseSettings.build(_id("teleporter"))));
        FARMING_MACHINE = registry.registerBlock(_id("farming_machine"), () -> new FarmingMachine(baseSettings.build(_id("farming_machine"))));
        LOGGING_MACHINE = registry.registerBlock(_id("logging_machine"), () -> new LoggingMachine(baseSettings.build(_id("logging_machine"))));
        FERTILIZER_SPREADER = registry.registerBlock(_id("fertilizer_spreader"), () -> new FertilizerSpreader(baseSettings.build(_id("fertilizer_spreader"))));
        ENCHANTMENT_EXTRACTOR = registry.registerBlock(_id("enchantment_extractor"), () -> new EnchantmentExtractor(baseSettings.build(_id("enchantment_extractor"))));
        CARDBOARD_BOX = registry.registerBlock(_id("cardboard_box"), () -> new CardboardBox(CompatibleBlockSettings.of(_id("cardboard_box"), CompatibleMaterial.WOOD).sounds(CompatBlockSoundGroup.WOOD).strength(1, 3)));
        CARDBOARD_BOX_MINEZON = registry.registerBlock(_id("cardboard_box_minezon"), () -> new CardboardBox(CompatibleBlockSettings.of(_id("cardboard_box_minezon"), CompatibleMaterial.WOOD).sounds(CompatBlockSoundGroup.WOOD).strength(1, 3)));
        CARDBOARD_BOX_MINETARO = registry.registerBlock(_id("cardboard_box_minetaro"), () -> new CardboardBox(CompatibleBlockSettings.of(_id("cardboard_box_minetaro"), CompatibleMaterial.WOOD).sounds(CompatBlockSoundGroup.WOOD).strength(1, 3)));
        CARDBOARD_BOX_NOTHING = registry.registerBlock(_id("cardboard_box_nothing_logo"), () -> new CardboardBox(CompatibleBlockSettings.of(_id("cardboard_box_nothing_logo"), CompatibleMaterial.WOOD).sounds(CompatBlockSoundGroup.WOOD).strength(1, 3)));
        LIGHT = registry.registerBlock(_id("light"), () -> new CompatBlock(CompatibleBlockSettings.of(_id("light"), CompatibleMaterial.METAL).strength(1.5F, 4).luminance((state) -> 15)));
        INDUSTRIAL_TNT = registry.registerBlock(_id("industrial_tnt"), () -> new IndustrialTNT(CompatibleBlockSettings.copy(_id("industrial_tnt"), net.minecraft.block.Blocks.TNT)));
    }
}
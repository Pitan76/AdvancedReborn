package net.pitan76.advancedreborn;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.pitan76.advancedreborn.blocks.RaySolar;
import net.pitan76.advancedreborn.tile.*;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.tile.BlockEntityTypeBuilder;

import static net.pitan76.advancedreborn.AdvancedReborn.INSTANCE;
import static net.pitan76.advancedreborn.AdvancedReborn.registry;

public class Tiles {
    public static RegistryResult<BlockEntityType<?>> INDUCTION_FURNACE_TILE;
    public static RegistryResult<BlockEntityType<?>> ROTARY_GRINDER_TILE;
    public static RegistryResult<BlockEntityType<?>> CENTRIFUGAL_EXTRACTOR_TILE;
    public static RegistryResult<BlockEntityType<?>> SINGULARITY_COMPRESSOR_TILE;
    public static RegistryResult<BlockEntityType<?>> RAY_SOLAR_TILE;
    public static RegistryResult<BlockEntityType<?>> CARDBOARD_BOX_TILE;
    public static RegistryResult<BlockEntityType<?>> CANNING_MACHINE_TILE;
    public static RegistryResult<BlockEntityType<?>> RENAMING_MACHINE_TILE;
    public static RegistryResult<BlockEntityType<?>> TELEPORTER_TILE;
    public static RegistryResult<BlockEntityType<?>> FARMING_MACHINE_TILE;
    public static RegistryResult<BlockEntityType<?>> LOGGING_MACHINE_TILE;
    public static RegistryResult<BlockEntityType<?>> FERTILIZER_SPREADER_TILE;
    public static RegistryResult<BlockEntityType<?>> ENCHANTMENT_EXTRACTOR_TILE;

    public static void init() {
        INDUCTION_FURNACE_TILE = registry.registerBlockEntityType(INSTANCE.compatId("induction_furnace"), () -> create(InductionFurnaceTile::new, Blocks.INDUCTION_FURNACE.getOrNull()));
        ROTARY_GRINDER_TILE = registry.registerBlockEntityType(INSTANCE.compatId("rotary_grinder"), () -> create(RotaryGrinderTile::new, Blocks.ROTARY_GRINDER.getOrNull()));
        CENTRIFUGAL_EXTRACTOR_TILE = registry.registerBlockEntityType(INSTANCE.compatId("centrifugal_extractor"), () -> create(CentrifugalExtractorTile::new, Blocks.CENTRIFUGAL_EXTRACTOR.getOrNull()));
        SINGULARITY_COMPRESSOR_TILE = registry.registerBlockEntityType(INSTANCE.compatId("singularity_compressor"), () -> create(SingularityCompressorTile::new, Blocks.SINGULARITY_COMPRESSOR.getOrNull()));
        RAY_SOLAR_TILE = registry.registerBlockEntityType(INSTANCE.compatId("ray_solar"), () -> create(RaySolarTile::new, AdvancedReborn.solars.toArray(new RaySolar[0])));
        CARDBOARD_BOX_TILE = registry.registerBlockEntityType(INSTANCE.compatId("cardboard_box"), () -> create(CardboardBoxTile::new, Blocks.CARDBOARD_BOX.getOrNull()));
        CANNING_MACHINE_TILE = registry.registerBlockEntityType(INSTANCE.compatId("canning_machine"), () -> create(CanningMachineTile::new, Blocks.CANNING_MACHINE.getOrNull()));
        RENAMING_MACHINE_TILE = registry.registerBlockEntityType(INSTANCE.compatId("renaming_machine"), () -> create(RenamingMachineTile::new, Blocks.RENAMING_MACHINE.getOrNull()));
        TELEPORTER_TILE = registry.registerBlockEntityType(INSTANCE.compatId("teleporter"), () -> create(TeleporterTile::new, Blocks.TELEPORTER.getOrNull()));
        FARMING_MACHINE_TILE = registry.registerBlockEntityType(INSTANCE.compatId("farming_machine"), () -> create(FarmingMachineTile::new, Blocks.FARMING_MACHINE.getOrNull()));
        LOGGING_MACHINE_TILE = registry.registerBlockEntityType(INSTANCE.compatId("logging_machine"), () -> create(LoggingMachineTile::new, Blocks.LOGGING_MACHINE.getOrNull()));
        FERTILIZER_SPREADER_TILE = registry.registerBlockEntityType(INSTANCE.compatId("fertilizer_spreader"), () -> create(FertilizerSpreaderTile::new, Blocks.FERTILIZER_SPREADER.getOrNull()));
        ENCHANTMENT_EXTRACTOR_TILE = registry.registerBlockEntityType(INSTANCE.compatId("enchantment_extractor"), () -> create(EnchantmentExtractorTile::new, Blocks.ENCHANTMENT_EXTRACTOR.getOrNull()));
    }

    public static <T extends BlockEntity> BlockEntityType<T> create(BlockEntityTypeBuilder.Factory<T> supplier, Block... blocks) {
        return BlockEntityTypeBuilder.create(supplier, blocks).build();
    }
}

package net.pitan76.advancedreborn;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import net.pitan76.advancedreborn.blocks.RaySolar;
import net.pitan76.advancedreborn.tile.*;

public class Tiles {
    public static BlockEntityType<InductionFurnaceTile> INDUCTION_FURNACE_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("induction_furnace"), create(InductionFurnaceTile::new, Blocks.INDUCTION_FURNACE.getOrNull()));
    public static BlockEntityType<RotaryGrinderTile> ROTARY_GRINDER_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("rotary_grinder"), create(RotaryGrinderTile::new, Blocks.ROTARY_GRINDER.getOrNull()));
    public static BlockEntityType<CentrifugalExtractorTile> CENTRIFUGAL_EXTRACTOR_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("centrifugal_extractor"), create(CentrifugalExtractorTile::new, Blocks.CENTRIFUGAL_EXTRACTOR.getOrNull()));
    public static BlockEntityType<SingularityCompressorTile> SINGULARITY_COMPRESSOR_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("singularity_compressor"), create(SingularityCompressorTile::new, Blocks.SINGULARITY_COMPRESSOR.getOrNull()));
    public static BlockEntityType<RaySolarTile> RAY_SOLAR_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("ray_solar"), create(RaySolarTile::new, AdvancedReborn.solars.toArray(new RaySolar[AdvancedReborn.solars.size()])));
    public static BlockEntityType<CardboardBoxTile> CARDBOARD_BOX_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("cardboard_box"), create(CardboardBoxTile::new, Blocks.CARDBOARD_BOX.getOrNull()));
    public static BlockEntityType<CanningMachineTile> CANNING_MACHINE_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("canning_machine"), create(CanningMachineTile::new, Blocks.CANNING_MACHINE.getOrNull()));
    public static BlockEntityType<RenamingMachineTile> RENAMING_MACHINE_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("renaming_machine"), create(RenamingMachineTile::new, Blocks.RENAMING_MACHINE.getOrNull()));
    public static BlockEntityType<TeleporterTile> TELEPORTER_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("teleporter"), create(TeleporterTile::new, Blocks.TELEPORTER.getOrNull()));
    public static BlockEntityType<FarmingMachineTile> FARMING_MACHINE_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("farming_machine"), create(FarmingMachineTile::new, Blocks.FARMING_MACHINE.getOrNull()));
    public static BlockEntityType<LoggingMachineTile> LOGGING_MACHINE_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("logging_machine"), create(LoggingMachineTile::new, Blocks.LOGGING_MACHINE.getOrNull()));
    public static BlockEntityType<FertilizerSpreaderTile> FERTILIZER_SPREADER_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("fertilizer_spreader"), create(FertilizerSpreaderTile::new, Blocks.FERTILIZER_SPREADER.getOrNull()));
    public static BlockEntityType<EnchantmentExtractorTile> ENCHANTMENT_EXTRACTOR_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, AdvancedReborn.INSTANCE.id("enchantment_extractor"), create(EnchantmentExtractorTile::new, Blocks.ENCHANTMENT_EXTRACTOR.getOrNull()));

    public static void init() {

    }

    public static <T extends BlockEntity> BlockEntityType<T> create(FabricBlockEntityTypeBuilder.Factory<T> supplier, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(supplier, blocks).build(null);
    }
}

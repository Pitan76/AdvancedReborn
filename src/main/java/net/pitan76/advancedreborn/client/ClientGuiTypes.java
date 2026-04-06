package net.pitan76.advancedreborn.client;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.pitan76.advancedreborn.GuiTypes;
import net.pitan76.advancedreborn.gui.*;
import net.pitan76.advancedreborn.tile.*;
import techreborn.blockentity.GuiType;
import techreborn.client.ClientGuiType;
import techreborn.client.GuiFactory;

public class ClientGuiTypes {
    public static ClientGuiType<CanningMachineTile> CANNING_MACHINE = register(GuiTypes.CANNING_MACHINE, GuiCanningMachine::new);
    public static ClientGuiType<RenamingMachineTile> RENAMING_MACHINE = register(GuiTypes.RENAMING_MACHINE, GuiRenamingMachine::new);
    public static ClientGuiType<InductionFurnaceTile> INDUCTION_FURNACE = register(GuiTypes.INDUCTION_FURNACE, GuiInductionFurnace::new);
    public static ClientGuiType<RotaryGrinderTile> ROTARY_GRINDER = register(GuiTypes.ROTARY_GRINDER, GuiRotaryGrinder::new);
    public static ClientGuiType<CentrifugalExtractorTile> CENTRIFUGAL_EXTRACTOR = register(GuiTypes.CENTRIFUGAL_EXTRACTOR, GuiCentrifugalExtractor::new);
    public static ClientGuiType<SingularityCompressorTile> SINGULARITY_COMPRESSOR = register(GuiTypes.SINGULARITY_COMPRESSOR, GuiSingularityCompressor::new);
    public static ClientGuiType<FarmingMachineTile> FARMING_MACHINE = register(GuiTypes.FARMING_MACHINE, GuiFarmingMachine::new);
    public static ClientGuiType<LoggingMachineTile> LOGGING_MACHINE = register(GuiTypes.LOGGING_MACHINE, GuiLoggingMachine::new);
    public static ClientGuiType<FertilizerSpreaderTile> FERTILIZER_SPREADER = register(GuiTypes.FERTILIZER_SPREADER, GuiFertilizerSpreader::new);
    public static ClientGuiType<EnchantmentExtractorTile> ENCHANTMENT_EXTRACTOR = register(GuiTypes.ENCHANTMENT_EXTRACTOR, GuiEnchantmentExtractor::new);

    public static <T extends BlockEntity> ClientGuiType<T> register(GuiType<T> type, GuiFactory<T> factory) {
        return new ClientGuiType<>(type, factory);
    }

    public static void init() {
    }
}

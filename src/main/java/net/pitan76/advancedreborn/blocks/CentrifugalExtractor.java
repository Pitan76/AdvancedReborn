package net.pitan76.advancedreborn.blocks;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.advancedreborn.GuiTypes;
import net.pitan76.advancedreborn.tile.CentrifugalExtractorTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import reborncore.api.blockentity.IMachineGuiHandler;

public class CentrifugalExtractor extends AdvancedMachineBlock {

    public CentrifugalExtractor(CompatibleBlockSettings settings) {
        super(settings);
    }

    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new CentrifugalExtractorTile(event);
    }

    public IMachineGuiHandler getGui() {
        return GuiTypes.CENTRIFUGAL_EXTRACTOR;
    }
}

package net.pitan76.advancedreborn.blocks;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.pitan76.advancedreborn.GuiTypes;
import net.pitan76.advancedreborn.tile.FertilizerSpreaderTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import reborncore.api.blockentity.IMachineGuiHandler;

public class FertilizerSpreader extends AdvancedMachineBlock {
    public FertilizerSpreader(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent e) {
        return new FertilizerSpreaderTile(e);
    }

    @Override
    public IMachineGuiHandler getGui() {
        return GuiTypes.FERTILIZER_SPREADER;
    }
}

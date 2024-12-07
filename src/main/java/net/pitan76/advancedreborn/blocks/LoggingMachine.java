package net.pitan76.advancedreborn.blocks;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.advancedreborn.GuiTypes;
import net.pitan76.advancedreborn.tile.LoggingMachineTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import reborncore.api.blockentity.IMachineGuiHandler;

public class LoggingMachine extends AdvancedMachineBlock {
    public LoggingMachine(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new LoggingMachineTile(event);
    }

    @Override
    public IMachineGuiHandler getGui() {
        return GuiTypes.LOGGING_MACHINE;
    }
}

package net.pitan76.advancedreborn.blocks;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.advancedreborn.GuiTypes;
import net.pitan76.advancedreborn.tile.FarmingMachineTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import reborncore.api.blockentity.IMachineGuiHandler;

public class FarmingMachine extends AdvancedMachineBlock {
    public FarmingMachine(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new FarmingMachineTile(event);
    }

    @Override
    public IMachineGuiHandler getGui() {
        return GuiTypes.FARMING_MACHINE;
    }
}

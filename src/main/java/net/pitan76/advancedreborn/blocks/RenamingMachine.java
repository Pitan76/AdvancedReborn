package net.pitan76.advancedreborn.blocks;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.pitan76.advancedreborn.GuiTypes;
import net.pitan76.advancedreborn.tile.RenamingMachineTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import reborncore.api.blockentity.IMachineGuiHandler;

public class RenamingMachine extends AdvancedMachineBlock {

    public RenamingMachine(CompatibleBlockSettings settings) {
        super(settings);
    }

    public BlockEntity createBlockEntity(TileCreateEvent e) {
        return new RenamingMachineTile(e);
    }

    public IMachineGuiHandler getGui() {
        return GuiTypes.RENAMING_MACHINE;
    }
}

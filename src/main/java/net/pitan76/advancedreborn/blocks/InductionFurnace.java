package net.pitan76.advancedreborn.blocks;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.advancedreborn.GuiTypes;
import net.pitan76.advancedreborn.tile.InductionFurnaceTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import reborncore.api.blockentity.IMachineGuiHandler;

public class InductionFurnace extends AdvancedMachineBlock {

    public InductionFurnace(CompatibleBlockSettings settings) {
        super(settings);
    }

    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new InductionFurnaceTile(event);
    }

    public IMachineGuiHandler getGui() {
        return GuiTypes.INDUCTION_FURNACE;
    }
}

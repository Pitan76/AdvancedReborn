package net.pitan76.advancedreborn.blocks;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.advancedreborn.GuiTypes;
import net.pitan76.advancedreborn.tile.RotaryGrinderTile;
import net.pitan76.mcpitanlib.api.block.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import reborncore.api.blockentity.IMachineGuiHandler;

public class RotaryGrinder extends AdvancedMachineBlock {

    public RotaryGrinder(CompatibleBlockSettings settings) {
        super(settings);
    }

    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new RotaryGrinderTile(event);
    }

    public IMachineGuiHandler getGui() {
        return GuiTypes.ROTARY_GRINDER;
    }
}

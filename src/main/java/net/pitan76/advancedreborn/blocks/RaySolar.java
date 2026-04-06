package net.pitan76.advancedreborn.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.tile.RaySolarTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class RaySolar extends AdvancedMachineBlock {

    public boolean isRayGenerator = false;
    public int energy = 1;

    public RaySolar(CompatibleBlockSettings settings, int energy, boolean allowNight) {
        super(settings);
        if (!AdvancedReborn.solars.contains(this)) AdvancedReborn.solars.add(this);
        this.isRayGenerator = allowNight;
        this.energy = energy;
    }

    public BlockEntity createBlockEntity(TileCreateEvent e) {
        return new RaySolarTile(e, this);
    }

    public BlockEntity createBlockEntity(BlockGetter world) {
        return createBlockEntity(new TileCreateEvent(world));
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return createBlockEntity(new TileCreateEvent(pos, state));
    }
}

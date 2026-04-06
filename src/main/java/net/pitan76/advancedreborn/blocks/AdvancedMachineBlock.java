package net.pitan76.advancedreborn.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;

public abstract class AdvancedMachineBlock extends BlockMachineBase {

    public AdvancedMachineBlock(CompatibleBlockSettings settings) {
        super(settings.build());
    }

    // 1.17.1へのポート用
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return createBlockEntity(new TileCreateEvent(pos, state));
    }

    public BlockEntity createBlockEntity(BlockGetter world) {
        return createBlockEntity(new TileCreateEvent(world));
    }

    public abstract BlockEntity createBlockEntity(TileCreateEvent event);

    public IMachineGuiHandler getGui() {
        return null;
    }
}

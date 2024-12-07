package net.pitan76.advancedreborn.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
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

    public BlockEntity createBlockEntity(BlockView world) {
        return createBlockEntity(new TileCreateEvent(world));
    }

    public abstract BlockEntity createBlockEntity(TileCreateEvent event);

    public IMachineGuiHandler getGui() {
        return null;
    }
}

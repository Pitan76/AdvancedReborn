package net.pitan76.advancedreborn.blocks;

import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.pitan76.advancedreborn.tile.TeleporterTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class Teleporter extends AdvancedMachineBlock {


    public Teleporter(CompatibleBlockSettings settings) {
        super(settings);
    }

    public RenderShape getRenderType(BlockState state) {
        return RenderShape.MODEL;
    }

    // 1.17.1へのポート用
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return createBlockEntity(new TileCreateEvent(pos, state));
    }

    public BlockEntity createBlockEntity(BlockGetter world) {
        return createBlockEntity(new TileCreateEvent(world));
    }

    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new TeleporterTile(event);
    }

}

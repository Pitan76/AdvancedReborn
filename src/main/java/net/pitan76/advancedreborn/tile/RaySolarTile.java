package net.pitan76.advancedreborn.tile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.blocks.RaySolar;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;

public class RaySolarTile extends PowerAcceptorBlockEntity implements IToolDrop {

    public RaySolar solar = (RaySolar) Blocks.RAY_SOLAR_1.getOrNull();
    public long energy = 1;

    public RaySolarTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.solar = (RaySolar) state.getBlock();
        this.energy = solar.energy;
    }

    public RaySolarTile(TileCreateEvent event, RaySolar solar) {
        this(event);
        this.solar = solar;
        this.energy = solar.energy;
    }

    public RaySolarTile(TileCreateEvent event) {
        this(Tiles.RAY_SOLAR_TILE.getOrNull(), event.getBlockPos(), event.getBlockState());
    }

    // 1.17
    public RaySolarTile(BlockPos pos, BlockState state) {
        this(new TileCreateEvent(pos, state));
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        if (WorldUtil.isClient(world)) {
            return;
        }
        if ((!WorldUtil.isRaining(world) && !WorldUtil.isThundering(world) && WorldUtil.isDay(world) && WorldUtil.isSkyVisible(world, pos.above())) || solar.isRayGenerator) {
            addEnergy(getEuPerTick(energy));
        }
    }

    @Override
    protected boolean canProvideEnergy(@Nullable Direction side) {
        return true;
    }

    @Override
    public long getBaseMaxPower() {
        return energy * 8;
    }

    @Override
    public long getBaseMaxOutput() {
        return energy * 4;
    }

    @Override
    public long getBaseMaxInput() {
        return energy * 4;
    }

    @Override
    public ItemStack getToolDrop(Player p0) {
        return ItemStackUtil.create(solar.asItem());
    }
}

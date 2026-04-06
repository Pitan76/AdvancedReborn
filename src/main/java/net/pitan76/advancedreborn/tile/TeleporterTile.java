package net.pitan76.advancedreborn.tile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.Level;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import org.jetbrains.annotations.Nullable;
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeleporterTile extends BlockEntity implements BlockEntityTicker<TeleporterTile> {

    private static final VoxelShape SHAPE_RANGE = Shapes.cuboid(-2, -2, -2, 3, 3, 3);
    private BlockPos teleportPos = null;

    public TeleporterTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public TeleporterTile(BlockPos pos, BlockState state) {
        this(Tiles.TELEPORTER_TILE.getOrNull(), pos, state);
    }

    public TeleporterTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public void tick(Level world, BlockPos pos, BlockState state, TeleporterTile tile) {
        if (!AutoConfigAddon.getConfig().teleporterEnabled) return;
        if (world == null) return;
        if (WorldUtil.isClient(world)) return;
        if (getTeleportPos() == null) return;
        List<BlockEntity> entities = getEntities();
        if (entities.isEmpty()) return;
        if (!world.isReceivingRedstonePower(getPos())) return;
        if (use()) {
            for (BlockEntity entity : entities) {
                entity.requestTeleport(getTeleportPos().getX() - 0.5D, getTeleportPos().getY() - 0.5D, getTeleportPos().getZ() - 0.5D);
                return;
            }
        }
    }

    public boolean useTile(BlockEntity blockEntity) {
        if (blockEntity instanceof EnergyStorageBlockEntity) {
            EnergyStorageBlockEntity energyStorage = (EnergyStorageBlockEntity) blockEntity;
            if (energyStorage.getEnergy() >= AutoConfigAddon.getConfig().teleporterUseEnergy) {
                energyStorage.useEnergy(AutoConfigAddon.getConfig().teleporterUseEnergy);
                return true;
            }
        }
        return false;
    }

    public boolean use() {
        if (world == null) return false;
        BlockEntity up = WorldUtil.getBlockEntity(world, pos.above());
        BlockEntity down =  WorldUtil.getBlockEntity(world, pos.below());
        BlockEntity north = WorldUtil.getBlockEntity(world, pos.north());
        BlockEntity south = WorldUtil.getBlockEntity(world, pos.south());
        BlockEntity east =  WorldUtil.getBlockEntity(world, pos.east());
        BlockEntity west =  WorldUtil.getBlockEntity(world, pos.west());

        if (useTile(up))
            return true;
        if (useTile(down))
            return true;
        if (useTile(north))
            return true;
        if (useTile(south))
            return true;
        if (useTile(east))
            return true;
        if (useTile(west))
            return true;
        return false;
    }

    @Nullable
    public BlockPos getTeleportPos() {
        return teleportPos;
    }

    @Nullable
    public void setTeleportPos(BlockPos teleportPos) {
        //System.out.println(teleportPos);
        this.teleportPos = teleportPos;
    }

    public List<BlockEntity> getEntities() {
        try {
            return SHAPE_RANGE.getBoundingBoxes().stream().flatMap((box) -> WorldUtil.getEntitiesByClass(getWorld(), BlockEntity.class, box.offset(getX(), getY(), getZ()), EntitySelector.VALID_ENTITY).stream()).collect(Collectors.toList());
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    public double getX() {
        return getPos().getX();
    }

    public double getY() {
        return getPos().getY();
    }

    public double getZ() {
        return getPos().getZ();
    }

    @Override
    public void saveAdditional(ValueOutput view) {
        if (getTeleportPos() != null) {
            view.putDouble("tpX", getTeleportPos().getX());
            view.putDouble("tpY", getTeleportPos().getY());
            view.putDouble("tpZ", getTeleportPos().getZ());
        }
        super.saveAdditional(view);
    }

    @Override
    public void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        double tpX = view.getDouble("tpX", 0);
        double tpY = view.getDouble("tpY", 0);
        double tpZ = view.getDouble("tpZ", 0);
        teleportPos = PosUtil.flooredBlockPos(tpX, tpY, tpZ);
    }
}

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

    private static final VoxelShape SHAPE_RANGE = Shapes.create(-2, -2, -2, 3, 3, 3);
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
        if (WorldUtil.isClient(world)) return;
        if (getTeleportPos() == null) return;
        List<Entity> entities = getEntities();
        if (entities.isEmpty()) return;
        if (!WorldUtil.isReceivingRedstonePower(world, getBlockPos())) return;
        if (use()) {
            for (Entity entity : entities) {
                entity.teleportTo(getTeleportPos().getX() - 0.5D, getTeleportPos().getY() - 0.5D, getTeleportPos().getZ() - 0.5D);
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
        if (level == null) return false;
        BlockPos pos = getBlockPos();

        BlockEntity up = WorldUtil.getBlockEntity(level, pos.above());
        BlockEntity down =  WorldUtil.getBlockEntity(level, pos.below());
        BlockEntity north = WorldUtil.getBlockEntity(level, pos.north());
        BlockEntity south = WorldUtil.getBlockEntity(level, pos.south());
        BlockEntity east =  WorldUtil.getBlockEntity(level, pos.east());
        BlockEntity west =  WorldUtil.getBlockEntity(level, pos.west());

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

    public List<Entity> getEntities() {
        try {
            return SHAPE_RANGE.toAabbs().stream().flatMap((box) -> WorldUtil.getEntitiesByClass(level, Entity.class, box.inflate(getX(), getY(), getZ()), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    public double getX() {
        return getBlockPos().getX();
    }

    public double getY() {
        return getBlockPos().getY();
    }

    public double getZ() {
        return getBlockPos().getZ();
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
        double tpX = view.getDoubleOr("tpX", 0);
        double tpY = view.getDoubleOr("tpY", 0);
        double tpZ = view.getDoubleOr("tpZ", 0);
        teleportPos = PosUtil.flooredBlockPos(tpX, tpY, tpZ);
    }
}

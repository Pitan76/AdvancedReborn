package net.pitan76.advancedreborn.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import org.jetbrains.annotations.Nullable;
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeleporterTile extends BlockEntity implements BlockEntityTicker<TeleporterTile> {

    private static final VoxelShape SHAPE_RANGE = VoxelShapes.cuboid(-2, -2, -2, 3, 3, 3);
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

    public void tick(World world, BlockPos pos, BlockState state, TeleporterTile tile) {
        if (!AutoConfigAddon.getConfig().teleporterEnabled) return;
        if (world == null) return;
        if (WorldUtil.isClient(world)) return;
        if (getTeleportPos() == null) return;
        List<Entity> entities = getEntities();
        if (entities.isEmpty()) return;
        if (!world.isReceivingRedstonePower(getPos())) return;
        if (use()) {
            for (Entity entity : entities) {
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
        BlockEntity up = WorldUtil.getBlockEntity(world, pos.up());
        BlockEntity down =  WorldUtil.getBlockEntity(world, pos.down());
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

    public List<Entity> getEntities() {
        try {
            return SHAPE_RANGE.getBoundingBoxes().stream().flatMap((box) -> WorldUtil.getEntitiesByClass(getWorld(), Entity.class, box.offset(getX(), getY(), getZ()), EntityPredicates.VALID_ENTITY).stream()).collect(Collectors.toList());
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

    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (getTeleportPos() != null) {
            nbt.putDouble("tpX", getTeleportPos().getX());
            nbt.putDouble("tpY", getTeleportPos().getY());
            nbt.putDouble("tpZ", getTeleportPos().getZ());
        }
        super.writeNbt(nbt, registryLookup);
    }

    public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(tag, registryLookup);
        if (tag.contains("tpX") && tag.contains("tpY") && tag.contains("tpZ")) teleportPos = PosUtil.flooredBlockPos(NbtUtil.getDouble(tag, "tpX"), NbtUtil.getDouble(tag, "tpY"), NbtUtil.getDouble(tag, "tpZ"));
    }
}

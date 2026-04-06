package net.pitan76.advancedreborn.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.Level;
import net.pitan76.advancedreborn.Particles;
import net.pitan76.advancedreborn.api.Energy;
import net.pitan76.mcpitanlib.api.block.args.v2.OutlineShapeEvent;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.*;
import net.pitan76.mcpitanlib.api.state.property.BooleanProperty;
import net.pitan76.mcpitanlib.api.state.property.CompatProperties;
import net.pitan76.mcpitanlib.api.state.property.DirectionProperty;
import net.pitan76.mcpitanlib.api.util.VoxelShapeUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.random.CompatRandom;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity;

public class ChargePad extends CompatBlock {

    public int multiple = 4;

    public static final VoxelShape SHAPE = VoxelShapeUtil.blockCuboid(0.0D, 0.0D, 0.0D, 16.0D, 1.5D, 16.0D);

    public static BooleanProperty USING = BooleanProperty.of("using");
    public static DirectionProperty FACING = CompatProperties.HORIZONTAL_FACING;

    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public int getComparatorOutput(BlockState state, Level world, BlockPos pos) {
        return USING.get(state) ? 15 : 0;
    }

    public ChargePad(CompatibleBlockSettings settings, int multiple) {
        super(settings);
        setNewDefaultState(getNewDefaultState().setValue(FACING.getProperty(), Direction.NORTH).setValue(USING.getProperty(), false));
        this.multiple = multiple;
    }

    public void setFacing(Direction facing, Level world, BlockPos pos) {
        WorldUtil.setBlockState(world, pos, WorldUtil.getBlockState(world, pos).setValue(FACING.getProperty(), facing));
    }

    public Direction getFacing(BlockState state) {
        return FACING.get(state);
    }

    public void onPlaced(BlockPlacedEvent e) {
        super.onPlaced(e);
        Level world = e.getWorld();
        BlockPos pos = e.getPos();
        LivingEntity placer = e.getPlacer();

        if (placer != null)
            setFacing(placer.getDirection().getOpposite(), world, pos);

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof MachineBaseBlockEntity) {
            ((MachineBaseBlockEntity) blockEntity).onPlace(world, pos, e.state, placer, e.stack);
        }
    }

    public void appendProperties(AppendPropertiesArgs args) {
        args.addProperty(FACING, USING);
        super.appendProperties(args);
    }

    CompatRandom random = CompatRandom.of(256);

    @Override
    public void onEntityCollision(EntityCollisionEvent e) {
        super.onEntityCollision(e);
        if (e.isClient()) return;
        if (!(e.getPlayerEntity().isPresent())) return;

        Level world = e.getWorld();
        BlockPos pos = e.getBlockPos();
        BlockState state = e.getState();
        Player player = new Player(e.getPlayerEntity().get());

        if (WorldUtil.getBlockEntity(world, pos.below()) instanceof EnergyStorageBlockEntity) {
            EnergyStorageBlockEntity tile = (EnergyStorageBlockEntity) WorldUtil.getBlockEntity(world, pos.below());
            if (tile == null) return;
            long eu = (long) tile.getEnergy();
            if (eu <= 5) return;
            //if (!tile.canProvideEnergy(EnergySide.UP)) return;
            long outputEU = 0;
            if (tile.getEuPerTick(eu) > tile.getEuPerTick(tile.getBaseMaxOutput() * multiple)) {
                outputEU = (long) tile.getBaseMaxOutput() * multiple;
            } else {
                outputEU = eu;
            }
            //System.out.println("EU: " + eu + ", OutputEU: " + outputEU);
            long storageEU = outputEU;
            for (int i = 0; i < player.getInvSize(); i++) {
                if (storageEU <= 0) break;

                ItemStack invStack = player.getInv().getItem(i);

                if (invStack.isEmpty()) continue;

                if (Energy.isHolder(invStack)) {
                    long energy = Energy.of(invStack).getStoredEnergy(invStack);
                    if (energy >= Energy.of(invStack).getEnergyCapacity(invStack)) continue;
                    Energy.of(invStack).setStoredEnergy(invStack, energy + storageEU);
                    storageEU -= Energy.of(invStack).getStoredEnergy(invStack) - energy;
                }
            }
            tile.setEnergy(eu - outputEU);
            double rX = random.nextInt(9) * 0.1;
            double rZ = random.nextInt(9) * 0.1;

            WorldUtil.spawnParticles(world, (SimpleParticleType) Particles.ENERGY.getOrNull(), pos.getX() + 0.1 + rX, pos.getY() + 0.25, pos.getZ() + 0.1 + rZ, 1, 0, 0.3, 0, 0);
            WorldUtil.setBlockState(world, pos, state.setValue(USING.getProperty(), true));
            WorldUtil.scheduleBlockTick(world, pos, this, 5);
            WorldUtil.updateComparators(world, pos, this);
        }
    }

    @Override
    public void scheduledTick(BlockScheduledTickEvent e) {
        super.scheduledTick(e);
        Level world = e.getWorld();
        BlockPos pos = e.getPos();

        WorldUtil.setBlockState(world, pos, e.state.setValue(USING.getProperty(), false));
        WorldUtil.updateComparators(world, pos, this);
    }

    @Override
    public VoxelShape getOutlineShape(OutlineShapeEvent e) {
        return SHAPE;
    }
}

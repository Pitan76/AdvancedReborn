package net.pitan76.advancedreborn.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.Particles;
import net.pitan76.advancedreborn.api.Energy;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.event.block.AppendPropertiesArgs;
import net.pitan76.mcpitanlib.api.event.block.BlockPlacedEvent;
import net.pitan76.mcpitanlib.api.event.block.BlockScheduledTickEvent;
import net.pitan76.mcpitanlib.api.block.args.v2.OutlineShapeEvent;
import net.pitan76.mcpitanlib.api.util.VoxelShapeUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.random.CompatRandom;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity;

public class ChargePad extends CompatBlock {

    public int multiple = 4;

    public static final VoxelShape SHAPE = VoxelShapeUtil.blockCuboid(0.0D, 0.0D, 0.0D, 16.0D, 1.5D, 16.0D);

    public static BooleanProperty USING = BooleanProperty.of("using");
    public static DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    @SuppressWarnings("deprecation")
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(USING) ? 15 : 0;
    }

    public ChargePad(CompatibleBlockSettings settings, int multiple) {
        super(settings);
        setNewDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(USING, false));
        this.multiple = multiple;
    }

    public void setFacing(Direction facing, World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
    }

    public Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    public void onPlaced(BlockPlacedEvent e) {
        super.onPlaced(e);
        World world = e.getWorld();
        BlockPos pos = e.getPos();
        LivingEntity placer = e.getPlacer();

        if(placer != null)
            setFacing(placer.getHorizontalFacing().getOpposite(), world, pos);

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

    @SuppressWarnings("deprecation")
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (world.isClient()) return;
        if (!(entity instanceof PlayerEntity)) return;
        if (world.getBlockEntity(pos.down()) instanceof EnergyStorageBlockEntity) {
            EnergyStorageBlockEntity tile = (EnergyStorageBlockEntity) world.getBlockEntity(pos.down());
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
            PlayerEntity player = (PlayerEntity) entity;
            for (int i = 0; i < player.getInventory().size(); i++) {
                if (storageEU <= 0) {
                    break;
                }
                ItemStack invStack = player.getInventory().getStack(i);

                if (invStack.isEmpty()) {
                    continue;
                }

                if (Energy.isHolder(invStack)) {
                    long energy = Energy.of(invStack).getStoredEnergy(invStack);
                    if (energy >= Energy.of(invStack).getEnergyCapacity()) continue;
                    Energy.of(invStack).setStoredEnergy(invStack, energy + storageEU);
                    storageEU -= Energy.of(invStack).getStoredEnergy(invStack) - energy;
                }
            }
            tile.setEnergy(eu - outputEU);
            double rX = random.nextInt(9) * 0.1;
            double rZ = random.nextInt(9) * 0.1;
            WorldUtil.spawnParticles(world, Particles.ENERGY, pos.getX() + 0.1 + rX, pos.getY() + 0.25, pos.getZ() + 0.1 + rZ, 1, 0, 0.3, 0, 0);
            WorldUtil.setBlockState(world, pos, state.with(USING, true));
            //world.createAndScheduleBlockTick(pos, this, 5);
            WorldUtil.scheduleBlockTick(world, pos, this, 5);
            WorldUtil.updateComparators(world, pos, this);
        }
    }

    @Override
    public void scheduledTick(BlockScheduledTickEvent e) {
        super.scheduledTick(e);
        World world = e.getWorld();
        BlockPos pos = e.getPos();

        WorldUtil.setBlockState(world, pos, e.state.with(USING, false));
        WorldUtil.updateComparators(world, pos, this);
    }

    public VoxelShape getOutlineShape(OutlineShapeEvent e) {
        return SHAPE;
    }
}

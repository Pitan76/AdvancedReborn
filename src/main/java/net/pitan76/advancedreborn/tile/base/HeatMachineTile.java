package net.pitan76.advancedreborn.tile.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.tile.InductionFurnaceTile;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;

public abstract class HeatMachineTile extends PowerAcceptorBlockEntity {
    public int heat = 0;
    public int heatMultiple = 100; // 上昇する速さ(低いほど早い)

    public HeatMachineTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public int getHeat() {
        return heat;
    }

    public int getHeatPer() {
        if (world == null) return 0;
        BlockEntity be = world.getBlockEntity(getPos());
        if (!(be instanceof HeatMachineTile)) return 0;
        HeatMachineTile tile = (HeatMachineTile) be;
        float heat = tile.getHeat();
        float heatMultiple = tile.getHeatMultiple();
        return Math.round(heat / heatMultiple);
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getHeatMultiple() {
        return heatMultiple;
    }

    public void setHeatMultiple(int heatMultiple) {
        this.heatMultiple = heatMultiple;
    }

    public void addHeat(int amount) {
        setHeat(getHeat() + amount);
    }

    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        if (world == null) return;
        if (getWorld().isClient()) {
            if (getWorld().isReceivingRedstonePower(getPos())) {
                if (getHeat() <= 100 * getHeatMultiple()) addHeat(1); //+0.1%
            } else if (getHeat() > 0) addHeat(-1);
            return;
        }
        if (getWorld().isReceivingRedstonePower(getPos())) {
            if (getHeat() <= 100 * getHeatMultiple()) addHeat(1); //+0.1%
            useEnergy(1);
        } else if (getHeat() > 0) addHeat(-1);
        if (getHeat() != 0) for (int i = 0;i <= getHeat() / (5 * getHeatMultiple());i++) {
            if (i >= getHeat() / (5 * getHeatMultiple())) break;
            super.tick(world, pos, state, blockEntity2);
            if (this instanceof InductionFurnaceTile) {
                InductionFurnaceTile tile = (InductionFurnaceTile) this;
                tile.setCookTime(tile.getCookingTime() + 1);
            }
        }
    }

    @Override
    public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(tag, registryLookup);
        setHeat(tag.getInt("heat"));
    }

    @Override
    public void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("heat", getHeat());
        super.writeNbt(tag, registryLookup);
    }
}

package net.pitan76.advancedreborn.tile.base;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.pitan76.advancedreborn.tile.InductionFurnaceTile;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
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
        if (level == null) return 0;
        BlockEntity be = WorldUtil.getBlockEntity(level, getBlockPos());
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

    public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        if (WorldUtil.isClient(world)) {
            if (WorldUtil.isReceivingRedstonePower(world, pos)) {
                if (getHeat() <= 100 * getHeatMultiple()) addHeat(1); //+0.1%
            } else if (getHeat() > 0) addHeat(-1);
            return;
        }
        if (WorldUtil.isReceivingRedstonePower(world, pos)) {
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
    public void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        setHeat(view.getIntOr("heat", 0));
    }

    @Override
    public void saveAdditional(ValueOutput view) {
        view.putInt("heat", getHeat());
        super.saveAdditional(view);
    }
}

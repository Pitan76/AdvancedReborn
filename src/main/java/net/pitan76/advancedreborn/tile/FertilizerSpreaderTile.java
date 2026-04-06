package net.pitan76.advancedreborn.tile;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;

public class FertilizerSpreaderTile extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

    public Block toolDrop;
    public int energySlot;

    public RebornInventory<?> inventory;

    public int[] slotIndex = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public int coolDownDefault = 5;
    public int coolDown = coolDownDefault;

    public FertilizerSpreaderTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        toolDrop = Blocks.FERTILIZER_SPREADER.getOrNull();
        energySlot = 10;
        inventory = new RebornInventory<>(11, "FertilizerSpreaderTile", 64, this);
        checkTier();
    }

    public FertilizerSpreaderTile(BlockPos pos, BlockState state) {
        this(Tiles.FERTILIZER_SPREADER_TILE.getOrNull(), pos, state);
    }

    public FertilizerSpreaderTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__FERTILIZER_SPREADER").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this)
                .slot(0, 55, 32).slot(1, 73, 32).slot(2, 91, 32).slot(3, 109, 32).slot(4, 127, 32)
                .slot(5, 55, 50).slot(6, 73, 50).slot(7, 91, 50).slot(8, 109, 50).slot(9, 127, 50)
                .energySlot(10, 8, 72).syncEnergyValue()
                .addInventory().create(this, syncID);
    }

    public long getBaseMaxPower() {
        return AutoConfigAddon.getConfig().fertilizerSpreaderMaxEnergy;
    }

    public long getBaseMaxOutput() {
        return 0;
    }

    public long getBaseMaxInput() {
        return AutoConfigAddon.getConfig().fertilizerSpreaderMaxInput;
    }

    public boolean canProvideEnergy(Direction side) {
        return false;
    }

    public ItemStack getToolDrop(Player p0) {
        return ItemStackUtil.create(toolDrop.asItem(), 1);
    }

    public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        if (world == null || WorldUtil.isClient(world)) {
            return;
        }
        charge(energySlot);
        BlockMachineBase block = (BlockMachineBase) state.getBlock();

        block.setActive(getEnergy() > 0, world, getPos());
        if (coolDown <= 0) coolDown = coolDownDefault;
        else {
            coolDown--;
            return;
        }

        if (isActive()) {

            // ここから!isEmpty↓
            if (getInventory().isEmpty()) return;

            long useEnergy = getEuPerTick(AutoConfigAddon.config.fertilizerSpreaderUseEnergy);

            if (getEnergy() > useEnergy) {
                ItemStack stack =  getFertilizerStack();
                if (trySpread(world, pos, AutoConfigAddon.config.fertilizerSpreaderRange, stack)) {
                    stack.shrink(1);
                    useEnergy(useEnergy);
                }
            }
        }
    }

    public ItemStack getFertilizerStack() {
        for (int i : slotIndex) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                if (!(stack.getItem() instanceof BoneMealItem)) continue;
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean trySpread(Level world, BlockPos pos, int range, ItemStack stack) {
        if (!(stack.getItem() instanceof BoneMealItem)) return false;

        for (int x = -range; x < range + 1; x++) {
            for (int z = -range; z < range + 1; z++) {
                for (int y = -range; y < range + 1; y++) {
                    BlockPos executePos = PosUtil.flooredBlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState state = WorldUtil.getBlockState(world, executePos);
                    if (state.getBlock() instanceof CropBlock || state.getBlock() instanceof SaplingBlock || state.isIn(BlockTags.SAPLINGS) || state.isIn(BlockTags.CROPS)) {
                        if (state.getBlock() instanceof CropBlock) {
                            CropBlock block = (CropBlock) state.getBlock();
                            if (block.isMature(state)) continue;
                        }
                        BoneMealItem.useOnFertilizable(ItemStack.EMPTY, world, executePos);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Container getInventory() {
        return inventory;
    }
}

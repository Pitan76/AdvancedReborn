package net.pitan76.advancedreborn.tile;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.BlockEntityUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import net.pitan76.mcpitanlib.midohra.block.MCBlocks;
import org.apache.commons.lang3.ArrayUtils;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;

import java.util.ArrayList;
import java.util.List;

public class FarmingMachineTile extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

    public Block toolDrop;
    public int energySlot;

    public RebornInventory<?> inventory;

    public int[] harvestItemSlotIndex = new int[] {4, 5, 6, 7, 8};
    public int[] plantItemSlotIndex = new int[] {0, 1, 2, 3};

    public int coolDownDefault = 5;
    public int coolDown = coolDownDefault;

    public FarmingMachineTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        toolDrop = Blocks.FARMING_MACHINE.getOrNull();
        energySlot = 9;
        inventory = new RebornInventory<>(10, "FarmingMachineTile", 64, this);
        checkTier();
    }

    public FarmingMachineTile(BlockPos pos, BlockState state) {
        this(Tiles.FARMING_MACHINE_TILE.getOrNull(), pos, state);
    }

    public FarmingMachineTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__FARMING_MACHINE").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this)
                .slot(0, 55, 32).slot(1, 73, 32).slot(2, 55, 50).slot(3, 73, 50)
                .slot(4, 55, 72).slot(5, 73, 72).slot(6, 91, 72).slot(7, 109, 72).slot(8, 127, 72)
                .energySlot(9, 8, 72).syncEnergyValue()
                .addInventory().create(this, syncID);
    }

    public long getBaseMaxPower() {
        return AutoConfigAddon.getConfig().farmingMachineMaxEnergy;
    }

    public long getBaseMaxOutput() {
        return 0;
    }

    public long getBaseMaxInput() {
        return AutoConfigAddon.getConfig().farmingMachineMaxInput;
    }

    public boolean canProvideEnergy(Direction side) {
        return false;
    }

    public ItemStack getToolDrop(Player p0) {
        return ItemStackUtil.create(toolDrop.asItem(), 1);
    }

    // RebornCore側のシグネチャはLevel。ServerLevelで宣言するとオーバーライドにならず動かないので注意
    @Override
    public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        if (world == null || WorldUtil.isClient(world) || !(world instanceof ServerLevel serverWorld)) {
            return;
        }
        charge(energySlot);
        BlockMachineBase block = (BlockMachineBase) state.getBlock();

        block.setActive(getEnergy() > 0, world, getBlockPos());
        if (coolDown <= 0) coolDown = coolDownDefault;
        else {
            coolDown--;
            return;
        }

        if (isActive()) {
            long harvestUseEnergy = getEuPerTick(AutoConfigAddon.config.farmingMachineHarvestUseEnergy);
            if (getEnergy() > harvestUseEnergy) {
                List<ItemStack> drops = new ArrayList<>();
                if (tryHarvest(serverWorld, pos, AutoConfigAddon.config.farmingMachineRange, drops)) {
                    for (ItemStack drop : drops) {
                        insertStack(drop);
                    }
                    useEnergy(harvestUseEnergy);
                }
            }

            // ここから!isEmpty↓
            if (getInventory().isEmpty()) return;

            long plantUseEnergy = getEuPerTick(AutoConfigAddon.config.farmingMachinePlantUseEnergy);

            if (getEnergy() > plantUseEnergy) {
                ItemStack stack =  getPlantStack();
                if (tryPlant(serverWorld, pos, AutoConfigAddon.config.farmingMachineRange, stack)) {
                    stack.shrink(1);
                    useEnergy(plantUseEnergy);
                }
            }
        }
    }

    public ItemStack getPlantStack() {
        for (int i : plantItemSlotIndex) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                if (!(stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof CropBlock)) continue;
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public void insertStack(ItemStack stack) {
        int[] indexes = harvestItemSlotIndex;
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof CropBlock) {
            indexes = ArrayUtils.addAll(plantItemSlotIndex, harvestItemSlotIndex);
        }
        for (int i : indexes) {
            ItemStack slotStack = inventory.getItem(i);
            if (slotStack.isEmpty()) {
                inventory.setItem(i, stack);
                BlockEntityUtil.markDirty(this);
                return;
            }
            if (slotStack.getItem() == stack.getItem() && slotStack.getCount() + stack.getCount() < 64) {
                inventory.setItem(i, ItemStackUtil.create(stack.getItem(), stack.getCount() + inventory.getItem(i).getCount()));
                BlockEntityUtil.markDirty(this);
                return;
            }
        }

        BlockPos pos = getBlockPos();
        WorldUtil.spawnEntity(level, ItemEntityUtil.create(level, pos.getX(), pos.getY(), pos.getZ(), stack));
    }


    public static void setFarmland(Level world, BlockPos pos, int range) {
        if (world == null || WorldUtil.isClient(world)) return;

        BlockPos downPos = pos.below();

        for (int x = -range; x < range + 1; x++) {
            for (int z = -range; z < range + 1; z++) {
                BlockPos executePos = PosUtil.flooredBlockPos(downPos.getX() + x, downPos.getY(), downPos.getZ() + z);
                if (WorldUtil.getBlockState(world, executePos).is(BlockTags.DIRT)) {
                    WorldUtil.setBlockState(world, executePos, MCBlocks.FARMLAND.getDefaultState());
                }
            }
        }
    }

    public static boolean tryHarvest(ServerLevel world, BlockPos pos, int range, List<ItemStack> drops) {
        for (int x = -range; x < range + 1; x++) {
            for (int z = -range; z < range + 1; z++) {
                BlockPos executePos = PosUtil.flooredBlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                BlockState state = WorldUtil.getBlockState(world, executePos);
                if (state.getBlock() instanceof CropBlock block) {
                    if (block.isMaxAge(WorldUtil.getBlockState(world, executePos))) {
                        if (drops != null)
                            drops.addAll(CropBlock.getDrops(state, world, executePos, null));
                        WorldUtil.breakBlock(world, executePos, false);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean tryPlant(ServerLevel world, BlockPos pos, int range, ItemStack stack) {
        if (!(stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof CropBlock)) return false;
        for (int x = -range; x < range + 1; x++) {
            for (int z = -range; z < range + 1; z++) {
                BlockPos executePos = PosUtil.flooredBlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                if (!WorldUtil.getBlockState(world, executePos).isAir()) continue;

                if (WorldUtil.getBlockState(world, executePos.below()).getBlock() instanceof FarmlandBlock) {
                    WorldUtil.setBlockState(world, executePos, ((BlockItem) stack.getItem()).getBlock().defaultBlockState(), 11);
                    return true;
                }
            }
        }
        return false;
    }

    public Container getInventory() {
        return inventory;
    }

    @Override
    public void onPlace(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onPlace(worldIn, pos, state, placer, stack);
        setFarmland(worldIn, pos, AutoConfigAddon.config.farmingMachineRange);
    }
}

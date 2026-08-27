package net.pitan76.advancedreborn.tile;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.BlockEntityUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
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

public class LoggingMachineTile extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

    public Block toolDrop;
    public int energySlot;
    public int saplingSlot = 0;

    public RebornInventory<?> inventory;

    public int[] insertItemSlots = new int[] {1, 2, 3, 4, 5};

    public int coolDownDefault = 5;
    public int coolDown = coolDownDefault;

    public LoggingMachineTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        toolDrop = Blocks.LOGGING_MACHINE.getOrNull();
        energySlot = 6;
        inventory = new RebornInventory<>(7, "LoggingMachineTile", 64, this);
        checkTier();
    }

    public LoggingMachineTile(BlockPos pos, BlockState state) {
        this(Tiles.LOGGING_MACHINE_TILE.getOrNull(), pos, state);
    }

    public LoggingMachineTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__LOGGING_MACHINE").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this)
                .slot(0, 55, 50)
                .slot(1, 55, 72).slot(2, 73, 72).slot(3, 91, 72).slot(4, 109, 72).slot(5, 127, 72)
                .energySlot(6, 8, 72).syncEnergyValue()
                .addInventory().create(this, syncID);
    }

    public long getBaseMaxPower() {
        return AutoConfigAddon.getConfig().loggingMachineMaxEnergy;
    }

    public long getBaseMaxOutput() {
        return 0;
    }

    public long getBaseMaxInput() {
        return AutoConfigAddon.getConfig().loggingMachineMaxInput;
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
            long loggingUseEnergy = getEuPerTick(AutoConfigAddon.config.loggingMachineLoggingUseEnergy);
            if (getEnergy() > loggingUseEnergy) {
                List<ItemStack> drops = new ArrayList<>();
                if (tryLogging(serverWorld, pos, getFacing(), AutoConfigAddon.config.loggingMachineRange, drops)) {
                    for (ItemStack drop : drops) {
                        insertStack(drop);
                    }
                    useEnergy(loggingUseEnergy);
                }
            }

            // ここから!isEmpty↓
            if (getInventory().isEmpty()) return;

            long plantUseEnergy = getEuPerTick(AutoConfigAddon.config.loggingMachinePlantUseEnergy);

            if (getEnergy() > plantUseEnergy) {
                ItemStack stack =  inventory.getItem(saplingSlot);
                if (tryPlant(serverWorld, pos, getFacing(), stack)) {
                    stack.shrink(1);
                    useEnergy(plantUseEnergy);
                }
            }
        }
    }
    public void insertStack(ItemStack stack) {
        int[] indexes = insertItemSlots;
        if (stack.is(ItemTags.SAPLINGS) || stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SaplingBlock) {
            indexes = ArrayUtils.addFirst(insertItemSlots, saplingSlot);
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

    public static boolean tryLogging(ServerLevel world, BlockPos pos, Direction direction, int range, List<ItemStack> drops) {
        for (int x = -range; x < range + 1; x++) {
            for (int z = -range; z < range + 1; z++) {
                for (int y = 0; y < range * 2 + 1; y++) {
                    BlockPos executePos = pos.relative(direction).offset(x, y, z);
                    BlockState state = WorldUtil.getBlockState(world, executePos);
                    if (state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES)) {
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

    public static boolean tryPlant(ServerLevel world, BlockPos pos, Direction direction, ItemStack stack) {
        BlockPos executePos = pos.relative(direction);
        if (!WorldUtil.getBlockState(world, executePos).isAir()) return false;

        if (WorldUtil.getBlockState(world, executePos.below()).is(BlockTags.DIRT)) {
            if (stack.is(ItemTags.SAPLINGS)) {
                WorldUtil.setBlockState(world, executePos, ((BlockItem) stack.getItem()).getBlock().defaultBlockState(), 11);
                return true;
            }
        }
        return false;
    }

    public Container getInventory() {
        return inventory;
    }
}

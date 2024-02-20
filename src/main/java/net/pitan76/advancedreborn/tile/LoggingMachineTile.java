package net.pitan76.advancedreborn.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import org.apache.commons.lang3.ArrayUtils;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
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

    public LoggingMachineTile(BlockEntityType<?> type) {
        super(type);
        toolDrop = Blocks.LOGGING_MACHINE;
        energySlot = 6;
        inventory = new RebornInventory<>(7, "LoggingMachineTile", 64, this);
        checkTier();
    }

    public LoggingMachineTile() {
        this(Tiles.LOGGING_MACHINE_TILE);
    }

    public LoggingMachineTile(TileCreateEvent event) {
        this();
    }

    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__LOGGING_MACHINE").player(player.inventory).inventory().hotbar().addInventory()
                .blockEntity(this)
                .slot(0, 55, 50)
                .slot(1, 55, 72).slot(2, 73, 72).slot(3, 91, 72).slot(4, 109, 72).slot(5, 127, 72)
                .energySlot(6, 8, 72).syncEnergyValue()
                .addInventory().create(this, syncID);
    }

    public double getBaseMaxPower() {
        return AutoConfigAddon.getConfig().loggingMachineMaxEnergy;
    }

    public double getBaseMaxOutput() {
        return 0;
    }

    public double getBaseMaxInput() {
        return AutoConfigAddon.getConfig().loggingMachineMaxInput;
    }

    public boolean canProvideEnergy(Direction side) {
        return false;
    }

    public ItemStack getToolDrop(PlayerEntity p0) {
        return new ItemStack(toolDrop, 1);
    }

    public void tick() {
        super.tick();
        if (world == null || world.isClient) {
            return;
        }
        charge(energySlot);
        BlockState state = world.getBlockState(getPos());
        BlockMachineBase block = (BlockMachineBase) state.getBlock();

        block.setActive(getEnergy() > 0, world, getPos());
        if (coolDown <= 0) coolDown = coolDownDefault;
        else {
            coolDown--;
            return;
        }


        if (isActive()) {
            double loggingUseEnergy = getEuPerTick(AutoConfigAddon.config.loggingMachineLoggingUseEnergy);
            if (getEnergy() > loggingUseEnergy) {
                List<ItemStack> drops = new ArrayList<>();
                if (tryLogging(world, pos, getFacing(), AutoConfigAddon.config.loggingMachineRange, drops)) {
                    for (ItemStack drop : drops) {
                        insertStack(drop);
                    }
                    useEnergy(loggingUseEnergy);
                }
            }

            // ここから!isEmpty↓
            if (getInventory().isEmpty()) return;

            double plantUseEnergy = getEuPerTick(AutoConfigAddon.config.loggingMachinePlantUseEnergy);

            if (getEnergy() > plantUseEnergy) {
                ItemStack stack =  inventory.getStack(saplingSlot);
                if (tryPlant(world, pos, getFacing(), stack)) {
                    stack.decrement(1);
                    useEnergy(plantUseEnergy);
                }
            }
        }
    }
    public void insertStack(ItemStack stack) {
        int[] indexes = insertItemSlots;
        if (stack.getItem().isIn(ItemTags.SAPLINGS) || stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SaplingBlock) {
            indexes = ArrayUtils.add(insertItemSlots, saplingSlot);
        }
        for (int i : indexes) {
            ItemStack slotStack = inventory.getStack(i);
            if (slotStack.isEmpty()) {
                inventory.setStack(i, stack);
                markDirty();
                return;
            }
            if (slotStack.getItem() == stack.getItem() && slotStack.getCount() + stack.getCount() < 64) {
                inventory.setStack(i, new ItemStack(stack.getItem(), stack.getCount() + inventory.getStack(i).getCount()));
                markDirty();
                return;
            }
        }

        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack));
    }

    public static boolean tryLogging(World world, BlockPos pos, Direction direction, int range, List<ItemStack> drops) {
        for (int x = -range; x < range + 1; x++) {
            for (int z = -range; z < range + 1; z++) {
                for (int y = 0; y < range * 2 + 1; y++) {
                    BlockPos executePos = pos.offset(direction).add(x, y, z);
                    BlockState state = world.getBlockState(executePos);
                    if (state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.LEAVES)) {
                        if (drops != null)
                            drops.addAll(CropBlock.getDroppedStacks(state, (ServerWorld) world, executePos, null));
                        world.breakBlock(executePos, false);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean tryPlant(World world, BlockPos pos, Direction direction, ItemStack stack) {
        BlockPos executePos = pos.offset(direction);
        if (!world.getBlockState(executePos).isAir()) return false;

        if (FarmingMachineTile.getDirtBlocks().contains(world.getBlockState(executePos.down()).getBlock())) {
            if (stack.getItem().isIn(ItemTags.SAPLINGS)) {
                world.setBlockState(executePos, ((BlockItem) stack.getItem()).getBlock().getDefaultState(), 11);
                return true;
            }
        }
        return false;
    }

    public Inventory getInventory() {
        return inventory;
    }
}

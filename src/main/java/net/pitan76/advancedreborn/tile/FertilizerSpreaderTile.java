package net.pitan76.advancedreborn.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.RebornInventory;

public class FertilizerSpreaderTile extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

    public Block toolDrop;
    public int energySlot;

    public RebornInventory<?> inventory;

    public int[] slotIndex = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public int coolDownDefault = 5;
    public int coolDown = coolDownDefault;

    public FertilizerSpreaderTile(BlockEntityType<?> type) {
        super(type);
        toolDrop = Blocks.FERTILIZER_SPREADER;
        energySlot = 10;
        inventory = new RebornInventory<>(11, "FertilizerSpreaderTile", 64, this);
        checkTier();
    }

    public FertilizerSpreaderTile(BlockPos pos, BlockState state) {
        this(Tiles.FERTILIZER_SPREADER_TILE);
    }

    public FertilizerSpreaderTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__FERTILIZER_SPREADER").player(player.inventory).inventory().hotbar().addInventory()
                .blockEntity(this)
                .slot(0, 55, 32).slot(1, 73, 32).slot(2, 91, 32).slot(3, 109, 32).slot(4, 127, 32)
                .slot(5, 55, 50).slot(6, 73, 50).slot(7, 91, 50).slot(8, 109, 50).slot(9, 127, 50)
                .energySlot(10, 8, 72).syncEnergyValue()
                .addInventory().create(this, syncID);
    }

    public double getBaseMaxPower() {
        return AutoConfigAddon.getConfig().fertilizerSpreaderMaxEnergy;
    }

    public double getBaseMaxOutput() {
        return 0;
    }

    public double getBaseMaxInput() {
        return AutoConfigAddon.getConfig().fertilizerSpreaderMaxInput;
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
        BlockState state = getWorld().getBlockState(getPos());
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

            double useEnergy = getEuPerTick(AutoConfigAddon.config.fertilizerSpreaderUseEnergy);

            if (getEnergy() > useEnergy) {
                ItemStack stack =  getFertilizerStack();
                if (trySpread(world, pos, AutoConfigAddon.config.fertilizerSpreaderRange, stack)) {
                    stack.decrement(1);
                    useEnergy(useEnergy);
                }
            }
        }
    }

    public ItemStack getFertilizerStack() {
        for (int i : slotIndex) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                if (!(stack.getItem() instanceof BoneMealItem)) continue;
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean trySpread(World world, BlockPos pos, int range, ItemStack stack) {
        if (!(stack.getItem() instanceof BoneMealItem)) return false;

        for (int x = -range; x < range + 1; x++) {
            for (int z = -range; z < range + 1; z++) {
                for (int y = -range; y < range + 1; y++) {
                    BlockPos executePos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState state = world.getBlockState(executePos);
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

    public Inventory getInventory() {
        return inventory;
    }
}

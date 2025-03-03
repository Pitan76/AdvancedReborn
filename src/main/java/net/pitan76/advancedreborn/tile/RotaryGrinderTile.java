package net.pitan76.advancedreborn.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.advancedreborn.tile.base.HeatMachineTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.init.ModRecipes;

public class RotaryGrinderTile extends HeatMachineTile implements IToolDrop, InventoryProvider, IRecipeCrafterProvider, BuiltScreenHandlerProvider {

    public Block toolDrop;
    public int energySlot;
    public RebornInventory<?> inventory;
    public RecipeCrafter crafter;

    public RotaryGrinderTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        toolDrop = Blocks.ROTARY_GRINDER.getOrNull();
        energySlot = 3;
        inventory = new RebornInventory<>(4, "RotaryGrinderTile", 64, this);
        crafter = new RecipeCrafter(ModRecipes.GRINDER, this, 2, 1, inventory, new int[]{0}, new int[]{1});
        checkTier();
    }

    public RotaryGrinderTile(BlockPos pos, BlockState state) {
        this(Tiles.ROTARY_GRINDER_TILE.getOrNull(), pos, state);
    }

    public RotaryGrinderTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__rotary_grinder_machine").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this).slot(0, 55, 45).outputSlot(1, 101 + 18, 45).outputSlot(2, 101, 45).energySlot(3, 8, 72).syncEnergyValue()
                .syncCrafterValue().addInventory().create(this, syncID);
    }

    public long getBaseMaxPower() {
        return AutoConfigAddon.getConfig().advancedMachineMaxEnergy;
    }

    public long getBaseMaxOutput() {
        return 0;
    }

    public long getBaseMaxInput() {
        return AutoConfigAddon.getConfig().advancedMachineMaxInput;
    }

    public boolean canProvideEnergy(Direction side) {
        return false;
    }

    public int getProgressScaled(int scale) {
        if (crafter != null && crafter.currentTickTime != 0 && crafter.currentNeededTicks != 0) {
            return crafter.currentTickTime * scale / crafter.currentNeededTicks;
        }
        return 0;
    }

    public RecipeCrafter getRecipeCrafter() {
        return crafter;
    }

    public ItemStack getToolDrop(PlayerEntity p0) {
        return ItemStackUtil.create(toolDrop.asItem(), 1);
    }

    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        if (world == null || WorldUtil.isClient(world)) {
            return;
        }
        charge(energySlot);
        if (!getInventory().getStack(1).isEmpty()) {
            if (getStack(1).getItem().equals(getStack(2).getItem())) {
                if (getStack(2).getCount() == getStack(2).getMaxCount()) return;
                getStack(2).increment(1);
                getStack(1).decrement(1);
            } else if (getStack(2).isEmpty()) {
                setStack(2, ItemStackUtil.create(getStack(1).getItem(), 1));
                getStack(1).decrement(1);
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}

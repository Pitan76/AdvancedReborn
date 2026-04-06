package net.pitan76.advancedreborn.tile;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Recipes;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;

public class CanningMachineTile extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, IRecipeCrafterProvider, BuiltScreenHandlerProvider {

    public Block toolDrop;
    public int energySlot;
    public RebornInventory<?> inventory;
    public RecipeCrafter crafter;

    public CanningMachineTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        toolDrop = Blocks.CANNING_MACHINE.getOrNull();
        energySlot = 3;
        inventory = new RebornInventory<>(4, "CanningMachineTile", 64, this);
        crafter = new RecipeCrafter(Recipes.CANNING_MACHINE, this, 3, 1, inventory, new int[]{0, 1}, new int[]{2});
        checkTier();
    }

    public CanningMachineTile(BlockPos pos, BlockState state) {
        this(Tiles.CANNING_MACHINE_TILE.getOrNull(), pos, state);
    }

    public CanningMachineTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__canning_machine").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this).slot(0, 55, 35).slot(1, 55, 55).outputSlot(2, 101, 45).energySlot(3, 8, 72).syncEnergyValue()
                .syncCrafterValue().addInventory().create(this, syncID);
    }

    public long getBaseMaxPower() {
        return AutoConfigAddon.getConfig().canningMachineMaxEnergy;
    }

    public long getBaseMaxOutput() {
        return 0;
    }

    public long getBaseMaxInput() {
        return AutoConfigAddon.getConfig().canningMachineMaxInput;
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
        block.setActive(!inventory.getItem(0).isEmpty() && !inventory.getItem(1).isEmpty(), world, getPos());
    }

    public Container getInventory() {
        return inventory;
    }
}

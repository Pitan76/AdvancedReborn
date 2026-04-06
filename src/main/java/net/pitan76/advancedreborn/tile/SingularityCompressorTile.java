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

public class SingularityCompressorTile extends HeatMachineTile implements IToolDrop, InventoryProvider, IRecipeCrafterProvider, BuiltScreenHandlerProvider {

    public Block toolDrop;
    public int energySlot;
    public RebornInventory<?> inventory;
    public RecipeCrafter crafter;

    public SingularityCompressorTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        toolDrop = Blocks.SINGULARITY_COMPRESSOR.getOrNull();
        energySlot = 2;
        inventory = new RebornInventory<>(3, "SingularityCompressorTile", 64, this);
        crafter = new RecipeCrafter(ModRecipes.COMPRESSOR, this, 2, 1, inventory, new int[]{0}, new int[]{1});
        checkTier();
    }

    public SingularityCompressorTile(BlockPos pos, BlockState state) {
        this(Tiles.SINGULARITY_COMPRESSOR_TILE.getOrNull(), pos, state);
    }

    public SingularityCompressorTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__centrifugal_extractor_machine").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this).slot(0, 55, 45).outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue()
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

    public ItemStack getToolDrop(Player p0) {
        return ItemStackUtil.create(toolDrop.asItem(), 1);
    }

    public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        if (world == null || WorldUtil.isClient(world)) {
            return;
        }
        charge(energySlot);
    }

    public Container getInventory() {
        return inventory;
    }
}

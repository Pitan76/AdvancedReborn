package net.pitan76.advancedreborn.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.advancedreborn.inventory.IInventory;
import net.pitan76.advancedreborn.tile.base.HeatMachineTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.RecipeUtil;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;

import java.util.Objects;
import java.util.Optional;

public class InductionFurnaceTile extends HeatMachineTile implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

    public Block toolDrop;
    public int energySlot;
    int inputSlot = 0;
    int inputSlot2 = 1;
    int outputSlot = 2;
    int outputSlot2 = 3;
    public RebornInventory<?> inventory;
    int ticksSinceLastChange;
    private SmeltingRecipe currentRecipe;
    private SmeltingRecipe currentRecipe2;
    private int cookTime;
    private int cookTimeTotal;
    final int EnergyPerTick = 1;

    public InductionFurnaceTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        toolDrop = Blocks.INDUCTION_FURNACE;
        energySlot = 4;
        inventory = new RebornInventory<>(5, "InductionFurnaceTile", 64, this);
        checkTier();
    }

    public InductionFurnaceTile(BlockPos pos, BlockState state) {
        this(Tiles.INDUCTION_FURNACE_TILE.getOrNull(), pos, state);
    }

    public InductionFurnaceTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }


    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__induction_furnace_machine").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this).slot(0, 55 - 18, 45).slot(1, 55, 45).outputSlot(2, 101, 45).outputSlot(3, 101 + 18, 45).energySlot(4, 8, 72).syncEnergyValue()
                .sync(PacketCodecs.INTEGER, this::getCookingTime, this::setCookTime).sync(PacketCodecs.INTEGER, this::getCookingTimeTotal, this::setCookTimeTotal).addInventory().create(this, syncID);
    }

    public Inventory getRecipe2AsInventory() {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(2, ItemStack.EMPTY);
        list.set(0, getInventory().getStack(1));
        list.set(1, getInventory().getStack(3));
        return IInventory.of(list);
    }

    private void setInvDirty(boolean isDirty) {
        inventory.setHashChanged(isDirty);
    }

    private boolean isInvDirty() {
        return inventory.hasChanged();
    }

    private void updateCurrentRecipe() {
        if (inventory.getStack(inputSlot).isEmpty()) {
            resetCrafter();
            return;
        }
        Optional<RecipeEntry<SmeltingRecipe>> testRecipe = Objects.requireNonNull(world).getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SingleStackRecipeInput(inventory.getStack(inputSlot)), world);
        if (testRecipe.isEmpty()) {
            resetCrafter();
            return;
        }
        if (!canAcceptOutput(testRecipe.get().value(), outputSlot)) {
            resetCrafter();
        }
        currentRecipe = testRecipe.get().value();
        cookTime = 0;
        cookTimeTotal = Math.max((int) (currentRecipe.getCookingTime() * (1.0 - getSpeedMultiplier())), 1);
        updateState();
    }

    private void updateCurrentRecipe2() {
        if (inventory.getStack(inputSlot2).isEmpty()) {
            resetCrafter2();
            return;
        }
        Optional<RecipeEntry<SmeltingRecipe>> testRecipe = Objects.requireNonNull(world).getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SingleStackRecipeInput(inventory.getStack(inputSlot2)), world);
        if (testRecipe.isEmpty()) {
            resetCrafter2();
            return;
        }
        if (!canAcceptOutput(testRecipe.get().value(), outputSlot2)) {
            resetCrafter2();
        }
        currentRecipe2 = testRecipe.get().value();
        if (currentRecipe == null) {
            cookTime = 0;
            cookTimeTotal = Math.max((int) (currentRecipe2.getCookingTime() * (1.0 - getSpeedMultiplier())), 1);
        }
        updateState();
    }

    private boolean canAcceptOutput(SmeltingRecipe recipe, int slot) {
        ItemStack recipeOutput = RecipeUtil.getOutput(recipe, world);
        if (recipeOutput.isEmpty()) {
            return false;
        }
        if (inventory.getStack(slot).isEmpty()) {
            return true;
        }
        if (ItemUtils.isItemEqual(inventory.getStack(slot), recipeOutput, true, true)) {
            return recipeOutput.getCount() + inventory.getStack(slot).getCount() <= recipeOutput.getMaxCount();
        }
        return false;
    }

    public boolean canCraftAgain() {
        if (inventory.getStack(inputSlot).isEmpty()) {
            return false;
        }
        if (currentRecipe == null) {
            return false;
        }
        if (!canAcceptOutput(currentRecipe, outputSlot)) {
            return false;
        }
        return !(getEnergy() < currentRecipe.getCookingTime() * getEuPerTick(EnergyPerTick));
    }

    public boolean canCraftAgain2() {
        if (inventory.getStack(inputSlot2).isEmpty()) {
            return false;
        }
        if (currentRecipe2 == null) {
            return false;
        }
        if (!canAcceptOutput(currentRecipe2, outputSlot2)) {
            return false;
        }
        return !(getEnergy() < currentRecipe2.getCookingTime() * getEuPerTick(EnergyPerTick));
    }

    private void resetCrafter() {
        currentRecipe = null;
        if (currentRecipe2 == null) {
            cookTime = 0;
            cookTimeTotal = 0;
        }
        updateState();
    }

    private void resetCrafter2() {
        currentRecipe2 = null;
        if (currentRecipe == null) {
            cookTime = 0;
            cookTimeTotal = 0;
        }
        updateState();
    }

    private void updateState() {
        Block furnaceBlock = getWorld().getBlockState(pos).getBlock();

        if (furnaceBlock instanceof BlockMachineBase) {
            BlockMachineBase blockMachineBase = (BlockMachineBase) furnaceBlock;
            boolean isActive = (currentRecipe != null || canCraftAgain() ) || (currentRecipe2 != null || canCraftAgain2());
            blockMachineBase.setActive(isActive, world, pos);
        }
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }


    private boolean hasAllInputs(SmeltingRecipe recipe) {
        if (recipe == null) return false;
        if (inventory.getStack(inputSlot).isEmpty()) return false;

        return recipe.matches(new SingleStackRecipeInput(inventory.getStack(inputSlot)), world);
    }

    private boolean hasAllInputs2(SmeltingRecipe recipe) {
        if (recipe == null) return false;
        if (inventory.getStack(inputSlot2).isEmpty()) return false;

        return recipe.matches(new SingleStackRecipeInput(inventory.getStack(inputSlot2)), world);
    }

    private void craftRecipe(SmeltingRecipe recipe, int outputSlot, int inputSlot) {
        if (recipe == null) {
            return;
        }
        if (!canAcceptOutput(recipe, outputSlot)) {
            return;
        }
        ItemStack outputStack = inventory.getStack(outputSlot);
        if (outputStack.isEmpty()) {
            inventory.setStack(outputSlot, RecipeUtil.getOutput(recipe, world).copy());
        } else {
            // Just increment. We already checked stack match and stack size
            outputStack.increment(1);
        }

        inventory.getStack(inputSlot).decrement(1);
    }

    public int getProgressScaled(int scale) {
        if (cookTimeTotal != 0) {
            return cookTime * scale / cookTimeTotal;
        }
        return 0;
    }

    public int getCookingTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getCookingTimeTotal() {
        return cookTimeTotal;
    }

    public void setCookTimeTotal(int cookTimeTotal) {
        this.cookTimeTotal = cookTimeTotal;
    }

    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        charge(2);

        if (world == null || world.isClient) {
            return;
        }

        ticksSinceLastChange++;
        // Force a has chanced every second
        if (ticksSinceLastChange == 20) {
            setInvDirty(true);
            ticksSinceLastChange = 0;
        }

        if (isInvDirty()) {
            if (currentRecipe == null) {
                updateCurrentRecipe();
            }
            if (currentRecipe2 == null) {
                updateCurrentRecipe2();
            }
            if (currentRecipe != null && (!hasAllInputs(currentRecipe) || !canAcceptOutput(currentRecipe, outputSlot))) {
                resetCrafter();
            }
            if (currentRecipe2 != null && (!hasAllInputs2(currentRecipe2) || !canAcceptOutput(currentRecipe2, outputSlot2))) {
                resetCrafter2();
            }
        }
        boolean crafting = false;
        boolean couldCraft = false;
        if (currentRecipe != null) {
            if (cookTime >= cookTimeTotal && hasAllInputs(currentRecipe)) {
                craftRecipe(currentRecipe, outputSlot, inputSlot);
                updateCurrentRecipe();
                couldCraft = true;
            } else if (cookTime < cookTimeTotal) {
                crafting = true;
            }
        }

        if (currentRecipe2 != null) {
            if (couldCraft && hasAllInputs2(currentRecipe2)) {
                craftRecipe(currentRecipe2, outputSlot2, inputSlot2);
                updateCurrentRecipe2();
            } else if (cookTime >= cookTimeTotal && hasAllInputs2(currentRecipe2)) {
                craftRecipe(currentRecipe2, outputSlot2, inputSlot2);
                updateCurrentRecipe2();
            } else if (cookTime < cookTimeTotal && currentRecipe == null) {
                crafting = true;
            }
        }

        if (crafting) {
            if (getStored() > getEuPerTick(EnergyPerTick)) {
                useEnergy(getEuPerTick(EnergyPerTick));
                cookTime++;
                if (cookTime == 1 || cookTime % 20 == 0 && RecipeCrafter.soundHandler != null) {
                    RecipeCrafter.soundHandler.playSound(false, this);
                }
            }
        }
        setInvDirty(false);
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

    public ItemStack getToolDrop(PlayerEntity p0) {
        return ItemStackUtil.create(toolDrop.asItem(), 1);
    }

    public Inventory getInventory() {
        return inventory;
    }
}

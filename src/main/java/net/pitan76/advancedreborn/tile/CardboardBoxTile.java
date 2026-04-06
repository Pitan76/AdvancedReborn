package net.pitan76.advancedreborn.tile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.advancedreborn.screen.CardboardBoxScreenHandler;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.event.container.factory.ExtraDataArgs;
import net.pitan76.mcpitanlib.api.event.nbt.NbtRWArgs;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.gui.v2.ExtendedScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.tile.CompatBlockEntity;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import org.jetbrains.annotations.Nullable;

public class CardboardBoxTile extends CompatBlockEntity implements IInventory, WorldlyContainer, ExtendedScreenHandlerFactory {

    public NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    private Component customName = null;
    private String note = "";

    public CardboardBoxTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CardboardBoxTile(BlockPos pos, BlockState state) {
        this(Tiles.CARDBOARD_BOX_TILE.getOrNull(), pos, state);
    }

    public CardboardBoxTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean hasNote() {
        return !note.isEmpty();
    }

    public void writeNbt(WriteNbtArgs args) {
        InventoryUtil.writeNbt(args, inventory);
        NbtUtil.putString(args.nbt, "note", getNote());
        super.writeNbt(args);
    }

    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);
        setNote(NbtUtil.getString(args.nbt, "note"));
        InventoryUtil.readNbt(args, inventory);
    }

    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    public void setCustomName(Component customName) {
        this.customName = customName;
    }

    public void readInventoryNbt(CompoundTag nbt) {
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (NbtUtil.has(nbt, "Items")) {
            InventoryUtil.readNbt(new NbtRWArgs(nbt), this.inventory);
        }

    }

    public CompoundTag writeInventoryNbt(CompoundTag nbt) {
        InventoryUtil.writeNbt(new NbtRWArgs(nbt), this.inventory, false);
        return nbt;
    }

    public Component getName() {
        return customName;
    }

    @Override
    public Component getDisplayName(DisplayNameArgs args) {
        return hasCustomName() ? customName : TextUtil.translatable("block.advanced_reborn.cardboard_box");
    }

    public boolean hasCustomName() {
        return customName != null && !customName.getString().isBlank();
    }

    public Component getCustomName() {
        return customName;
    }

    @Override
    public AbstractContainerMenu createMenu(CreateMenuEvent e) {
        return new CardboardBoxScreenHandler(e.getSyncId(), e.getPlayerInventory(), this, getNote(), this);
    }

    @Override
    public void writeExtraData(ExtraDataArgs args) {
        BlockPos pos = callGetPos();
        args.writeVar(pos.getX());
        args.writeVar(pos.getY());
        args.writeVar(pos.getZ());
        args.writeVar(getNote());
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @org.jspecify.annotations.Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return false;
    }
}

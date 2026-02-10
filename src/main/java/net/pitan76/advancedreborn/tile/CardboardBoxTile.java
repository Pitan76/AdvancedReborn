package net.pitan76.advancedreborn.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.screen.CardboardBoxScreenHandler;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import org.jetbrains.annotations.Nullable;

public class CardboardBoxTile extends LootableContainerBlockEntity implements SidedInventory, ScreenHandlerFactory {

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private String note = "";
    private static final Text CONTAINER_NAME_TEXT = TextUtil.translatable("block.advanced_reborn.cardboard_box");

    public CardboardBoxTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CardboardBoxTile(BlockPos pos, BlockState state) {
        this(Tiles.CARDBOARD_BOX_TILE.getOrNull(), pos, state);
    }

    public CardboardBoxTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        this.markDirty();
    }

    public boolean hasNote() {
        return !note.isEmpty();
    }

    @Override
    protected Text getContainerName() {
        return CONTAINER_NAME_TEXT;
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CardboardBoxScreenHandler(syncId, playerInventory, this, getNote(), this);
    }

    @Override
    public Text getDisplayName() {
        return this.getContainerName();
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(view)) {
            Inventories.readData(view, this.inventory);
        }
        view.getOptionalString("note").ifPresent(this::setNote);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        if (!this.writeLootTable(view)) {
            Inventories.writeData(view, this.inventory, false);
        }
        if (hasNote()) {
            view.putString("note", getNote());
        }
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[this.inventory.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}

package net.pitan76.advancedreborn.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.inventory.IInventory;
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

public class CardboardBoxTile extends CompatBlockEntity implements IInventory, SidedInventory, ExtendedScreenHandlerFactory {

    public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private Text customName = null;
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

    public DefaultedList<ItemStack> getItems() {
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
        args.nbt.putString("note", getNote());
        super.writeNbt(args);
    }

    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);
        setNote(args.nbt.getString("note"));
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

    public void setCustomName(Text customName) {
        this.customName = customName;
    }

    public void readInventoryNbt(NbtCompound nbt) {
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (nbt.contains("Items", 9)) {
            InventoryUtil.readNbt(new NbtRWArgs(nbt), this.inventory);
        }

    }

    public NbtCompound writeInventoryNbt(NbtCompound nbt) {
        InventoryUtil.writeNbt(new NbtRWArgs(nbt), this.inventory, false);
        return nbt;
    }

    public Text getName() {
        return customName;
    }

    @Override
    public Text getDisplayName(DisplayNameArgs args) {
        return hasCustomName() ? customName : TextUtil.translatable("block.advanced_reborn.cardboard_box");
    }

    public boolean hasCustomName() {
        return customName != null && !customName.getString().isBlank();
    }

    public Text getCustomName() {
        return customName;
    }

    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new CardboardBoxScreenHandler(e.getSyncId(), e.getPlayerInventory(), this, getNote(), this);
    }

    @Override
    public void writeExtraData(ExtraDataArgs args) {
        NbtCompound data = NbtUtil.create();
        data.putDouble("x", callGetPos().getX());
        data.putDouble("y", callGetPos().getY());
        data.putDouble("z", callGetPos().getZ());
        data.putString("note", getNote());
        args.writeVar(data);
    }
}

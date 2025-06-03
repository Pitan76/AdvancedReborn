package net.pitan76.advancedreborn.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.pitan76.advancedreborn.ScreenHandlers;
import net.pitan76.advancedreborn.tile.CardboardBoxTile;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandler;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import org.jetbrains.annotations.Nullable;

public class CardboardBoxScreenHandler extends ExtendedScreenHandler {

    public Inventory inventory;
    public String tmpNote = "";
    public BlockPos pos = PosUtil.flooredBlockPos(0, 0, 0);

    public CardboardBoxScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, InventoryUtil.createSimpleInventory(9), "", null);
        NbtCompound data = PacketByteUtil.readNbt(buf);

        if (data == null) return;
        if (NbtUtil.has(data, "x") && NbtUtil.has(data, "y") && NbtUtil.has(data, "z")) {
            pos = PosUtil.flooredBlockPos(NbtUtil.getDouble(data, "x"), NbtUtil.getDouble(data, "y"), NbtUtil.getDouble(data, "z"));
        }
        if (NbtUtil.has(data, "note")) {
            tmpNote = NbtUtil.getString(data, "note");
        }
    }

    public CardboardBoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, String note, @Nullable CardboardBoxTile tile) {
        super(ScreenHandlers.CARDBOARD_BOX_SCREEN_HANDLER.getOrNull(), syncId);
        checkSize(inventory, 9);
        this.inventory = inventory;
        this.tmpNote = note;
        if (tile != null) {
            pos = tile.callGetPos();
        }
        inventory.onOpen(playerInventory.player);
        int m;
        int l;
        for (l = 0; l < 9; ++l) {
            callAddSlot(new Slot(inventory, l, 8 + l * 18, 20));
        }
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                callAddSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 51 + m * 18));
            }
        }
        for (m = 0; m < 9; ++m) {
            callAddSlot(new Slot(playerInventory, m, 8 + m * 18, 109));
        }

    }

    public boolean canUse(Player player) {
        return true;
    }
    
    public ItemStack quickMoveOverride(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.callInsertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.callInsertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }
}

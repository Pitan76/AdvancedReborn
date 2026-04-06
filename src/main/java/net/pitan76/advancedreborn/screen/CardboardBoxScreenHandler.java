package net.pitan76.advancedreborn.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.BlockPos;
import net.pitan76.advancedreborn.ScreenHandlers;
import net.pitan76.advancedreborn.tile.CardboardBoxTile;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandler;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.SlotUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import org.jetbrains.annotations.Nullable;

public class CardboardBoxScreenHandler extends ExtendedScreenHandler {

    public Container inventory;
    public String tmpNote = "";
    public BlockPos pos = PosUtil.flooredBlockPos(0, 0, 0);

    public CardboardBoxScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, InventoryUtil.createSimpleInventory(9), "", null);

        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        pos = PosUtil.flooredBlockPos(x, y, z);
        tmpNote = buf.readUtf();
    }

    public CardboardBoxScreenHandler(int syncId, Inventory playerInventory, Container inventory, String note, @Nullable CardboardBoxTile tile) {
        super(ScreenHandlers.CARDBOARD_BOX_SCREEN_HANDLER.getOrNull(), syncId);
        checkContainerSize(inventory, 9);
        this.inventory = inventory;
        this.tmpNote = note;
        if (tile != null) {
            pos = tile.callGetPos();
        }
        inventory.startOpen(playerInventory.player);
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
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.callInsertItem(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.callInsertItem(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                SlotUtil.markDirty(slot);
            }
        }

        return newStack;
    }
}

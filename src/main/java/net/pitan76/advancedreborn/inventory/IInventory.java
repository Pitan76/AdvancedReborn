package net.pitan76.advancedreborn.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public interface IInventory extends Container {

    NonNullList<ItemStack> getItems();

    static IInventory of(NonNullList<ItemStack> items) {
        return () -> items;
    }

    static IInventory ofSize(int size) {
        return of(NonNullList.ofSize(size, ItemStack.EMPTY));
    }

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = ContainerHelper.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    default ItemStack removeStack(int slot) {
        return ContainerHelper.removeStack(getItems(), slot);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    default void clear() {
        getItems().clear();
    }

    @Override
    default void markDirty() {
    }

    @Override
    default boolean canPlayerUse(Player player) {
        return true;
    }
}
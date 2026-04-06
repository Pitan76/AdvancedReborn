package net.pitan76.advancedreborn.api;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyItem;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Energy {

    private static final HashMap<Predicate<Object>, Function<Object, EnergyStorage>> holderRegistry = new HashMap<>();

    static {
        registerHolder(object -> object instanceof EnergyStorage, object -> (EnergyStorage) object);
    }

    @Nullable
    public static SimpleEnergyItem of(Object object) {
        if (object instanceof ItemStack) {
            ItemStack stack = (ItemStack) object;
            if (stack.getItem() instanceof SimpleEnergyItem) {
                return (SimpleEnergyItem) stack.getItem();
            }
        }
        return null;
    }

    public static boolean isHolder(Object object) {
        if (object instanceof ItemStack) {
            ItemStack stack = (ItemStack) object;
            if (stack.getItem() instanceof SimpleEnergyItem) {
                return true;
            }
        }
        if (object instanceof SimpleEnergyItem) return true;
        return false;
    }

    public static boolean valid(Object object){
        for (Predicate<Object> predicate : holderRegistry.keySet()) {
            if (predicate.test(object)) return true;
        }
        return false;
    }

    public static <T> void registerHolder(Class<T> clazz, Function<Object, EnergyStorage> holderFunction) {
        registerHolder(object -> object.getClass() == clazz, holderFunction);
    }

    public static void registerHolder(Predicate<Object> supports, Function<Object, EnergyStorage> holderFunction) {
        holderRegistry.put(supports, holderFunction);
    }
}

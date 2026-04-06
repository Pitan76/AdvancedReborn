package net.pitan76.advancedreborn;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RecipeManager;

import static net.pitan76.advancedreborn.AdvancedReborn.INSTANCE;

public class Recipes {
    public static RecipeType<RebornRecipe> CANNING_MACHINE = RecipeManager.newRecipeType(INSTANCE.id("canning_machine"));

    public static RecipeType<?> byName(Identifier identifier) {
        return (RecipeType<?>) BuiltInRegistries.RECIPE_SERIALIZER.get(identifier).get();
    }

    public static void init() {

    }
}

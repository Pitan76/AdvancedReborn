package net.pitan76.advancedreborn;

import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;

import static net.pitan76.advancedreborn.AdvancedReborn.INSTANCE;

public class Recipes {
    public static RebornRecipeType<?> CANNING_MACHINE = RecipeManager.newRecipeType(INSTANCE.id("canning_machine"));

    public static RecipeType<?> byName(Identifier identifier) {
        return (RecipeType<?>) Registries.RECIPE_SERIALIZER.get(identifier);
    }

    public static void init() {

    }
}

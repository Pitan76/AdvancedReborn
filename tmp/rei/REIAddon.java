package net.pitan76.advancedreborn.addons.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Recipes;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.advancedreborn.addons.rei.machine.TwoInputRightOutputCategory;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RecipeManager;
import techreborn.compat.rei.FluidReplicatorRecipeDisplay;
import techreborn.compat.rei.MachineRecipeDisplay;
import techreborn.client.compat.rei.ReiPlugin;
import techreborn.compat.rei.RollingMachineDisplay;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.recipe.recipes.FluidReplicatorRecipe;
import techreborn.recipe.recipes.RollingMachineRecipe;

import java.util.function.Function;

import static net.pitan76.advancedreborn.AdvancedReborn.INSTANCE;

public class REIAddon implements REIClientPlugin {

    public static CompatIdentifier PLUGIN = INSTANCE.compatId("advanced_plugin");

    public REIAddon() {
        ReiPlugin.iconMap.put(Recipes.CANNING_MACHINE, Blocks.CANNING_MACHINE.getOrNull());
        ReiPlugin.iconMap.put(ModRecipes.GRINDER, Blocks.ROTARY_GRINDER.getOrNull());
        ReiPlugin.iconMap.put(ModRecipes.EXTRACTOR, Blocks.CENTRIFUGAL_EXTRACTOR.getOrNull());
        ReiPlugin.iconMap.put(ModRecipes.COMPRESSOR, Blocks.SINGULARITY_COMPRESSOR.getOrNull());
    }

    public Identifier getPluginIdentifier() {
        return PLUGIN.toMinecraft();
    }

    public void registerCategories(CategoryRegistry recipeHelper) {
        recipeHelper.add(new TwoInputRightOutputCategory<>(Recipes.CANNING_MACHINE));
        registerOthers();
    }

    private void addWorkstations(Identifier identifier, EntryStack<?>... stacks) {
        CategoryRegistry.getInstance().addWorkstations(CategoryIdentifier.of(identifier), stacks);
    }

    public void registerDisplays(DisplayRegistry registry) {
        registerRecipeDisplays(registry);
    }

    public void registerRecipeDisplays(DisplayRegistry recipeHelper) {
        RecipeManager.getRecipeTypes(AdvancedReborn.MOD_ID).forEach(recipeType -> registerMachineRecipe(recipeHelper, recipeType));
    }

    private <R extends RebornRecipe> void registerMachineRecipe(DisplayRegistry registry, RecipeType<?> recipeType) {
        if (recipeType != ModRecipes.RECYCLER) {
            Function<RecipeEntry<RebornRecipe>, Display> recipeDisplay = MachineRecipeDisplay::new;
            if (recipeType == ModRecipes.ROLLING_MACHINE) {
                recipeDisplay = RollingMachineDisplay::new;
            }

            if (recipeType == ModRecipes.FLUID_REPLICATOR) {
                recipeDisplay = FluidReplicatorRecipeDisplay::new;
            }

            Function<RecipeEntry, Display> finalRecipeDisplay = (Function) recipeDisplay;
            registry.beginFiller(RecipeEntry.class).filter(recipeType1 -> true)
                    .filter((recipeEntry) -> recipeEntry.value().getType() == recipeType)
                    .fill(finalRecipeDisplay);
        }
    }

    public void registerOthers() {
        if (AutoConfigAddon.getConfig().linkReiWithTR) registerOthersTR();
        if (AutoConfigAddon.getConfig().linkReiWithAR) {
            addWorkstations(Registries.RECIPE_TYPE.getId(Recipes.CANNING_MACHINE), of(Blocks.CANNING_MACHINE.get()));
            addWorkstations(Registries.RECIPE_TYPE.getId(ModRecipes.GRINDER), of(Blocks.ROTARY_GRINDER.get()));
            addWorkstations(Registries.RECIPE_TYPE.getId(ModRecipes.EXTRACTOR), of(Blocks.CENTRIFUGAL_EXTRACTOR.get()));
            addWorkstations(Registries.RECIPE_TYPE.getId(ModRecipes.COMPRESSOR), of(Blocks.SINGULARITY_COMPRESSOR.get()));
            addWorkstations(BuiltinPlugin.SMELTING.getIdentifier(), of(Blocks.INDUCTION_FURNACE.get()));
        }
    }

    public void registerOthersTR() {
        addWorkstations(BuiltinPlugin.SMELTING.getIdentifier(), of(TRContent.Machine.IRON_FURNACE));
        addWorkstations(BuiltinPlugin.SMELTING.getIdentifier(), of(TRContent.Machine.ELECTRIC_FURNACE));
    }

    public static EntryStack<ItemStack> of(ItemConvertible item) {
        return EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(item));
    }
}

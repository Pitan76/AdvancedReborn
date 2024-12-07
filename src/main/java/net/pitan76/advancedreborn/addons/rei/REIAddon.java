package net.pitan76.advancedreborn.addons.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.util.Identifier;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Recipes;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.advancedreborn.addons.rei.machine.TwoInputRightOutputCategory;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import techreborn.compat.rei.MachineRecipeDisplay;
import techreborn.compat.rei.ReiPlugin;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;

import java.util.function.Function;

public class REIAddon implements REIClientPlugin {

    public static CompatIdentifier PLUGIN = AdvancedReborn._id("advanced_plugin");

    public REIAddon() {
        ReiPlugin.iconMap.put(Recipes.CANNING_MACHINE, Blocks.CANNING_MACHINE.get());
        ReiPlugin.iconMap.put(ModRecipes.GRINDER, Blocks.ROTARY_GRINDER.get());
        ReiPlugin.iconMap.put(ModRecipes.EXTRACTOR, Blocks.CENTRIFUGAL_EXTRACTOR.get());
        ReiPlugin.iconMap.put(ModRecipes.COMPRESSOR, Blocks.SINGULARITY_COMPRESSOR.get());
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
        RecipeManager.getRecipeTypes(AdvancedReborn.MOD_ID).forEach(rebornRecipeType -> registerMachineRecipe(recipeHelper, rebornRecipeType));
    }

    public <R extends RebornRecipe> void registerMachineRecipe(DisplayRegistry registry, RebornRecipeType<R> recipeType) {
        Function<R, Display> recipeDisplay = r -> new MachineRecipeDisplay<>((RebornRecipe) r);
        registry.registerFiller(RebornRecipe.class, recipe -> {
            if (recipe instanceof RebornRecipe) {
                return recipe.getRebornRecipeType() == recipeType;
            }
            return false;
        }, recipeDisplay);
    }

    public void registerOthers() {
        if (AutoConfigAddon.getConfig().linkReiWithTR) registerOthersTR();
        if (AutoConfigAddon.getConfig().linkReiWithAR) {
            addWorkstations(Recipes.CANNING_MACHINE.name(), EntryStacks.of(Blocks.CANNING_MACHINE.get()));
            addWorkstations(ModRecipes.GRINDER.name(), EntryStacks.of(Blocks.ROTARY_GRINDER.get()));
            addWorkstations(ModRecipes.EXTRACTOR.name(), EntryStacks.of(Blocks.CENTRIFUGAL_EXTRACTOR.get()));
            addWorkstations(ModRecipes.COMPRESSOR.name(), EntryStacks.of(Blocks.SINGULARITY_COMPRESSOR.get()));
            addWorkstations(BuiltinPlugin.SMELTING.getIdentifier(), EntryStacks.of(Blocks.INDUCTION_FURNACE.get()));
        }
    }

    public void registerOthersTR() {
        addWorkstations(BuiltinPlugin.SMELTING.getIdentifier(), EntryStacks.of(TRContent.Machine.IRON_FURNACE));
        addWorkstations(BuiltinPlugin.SMELTING.getIdentifier(), EntryStacks.of(TRContent.Machine.ELECTRIC_FURNACE));
    }
}
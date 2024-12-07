package net.pitan76.advancedreborn.items;

import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.registry.CompatRegistry;

public class FuelCanItem extends ExtendItem {
    public FuelCanItem(CompatibleItemSettings settings) {
        super(settings);
        CompatRegistry.registerFuel(2000, this);
    }
}

package net.pitan76.advancedreborn.items;

import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItem;

public class ForgeHammer extends ExtendItem {
    public ForgeHammer(CompatibleItemSettings settings, int damage) {
        super(settings.maxDamage(damage));
    }
}

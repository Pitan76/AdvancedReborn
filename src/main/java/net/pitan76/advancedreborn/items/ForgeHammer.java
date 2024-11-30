package net.pitan76.advancedreborn.items;

import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;

public class ForgeHammer extends CompatItem {
    public ForgeHammer(CompatibleItemSettings settings, int damage) {
        super(settings.maxDamage(damage));
    }
}

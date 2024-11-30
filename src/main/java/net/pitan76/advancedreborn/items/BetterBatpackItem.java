package net.pitan76.advancedreborn.items;

import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.event.item.ItemAppendTooltipEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleArmorMaterial;
import net.pitan76.mcpitanlib.api.item.v2.CompatItemProvider;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.items.armor.BatpackItem;
import techreborn.utils.TRItemUtils;

public class BetterBatpackItem extends BatpackItem implements CompatItemProvider {

    protected CompatibleItemSettings settings;

    @Override
    public CompatibleItemSettings getCompatSettings() {
        return settings;
    }

    public BetterBatpackItem(CompatibleItemSettings settings, int maxCharge, CompatibleArmorMaterial material, RcEnergyTier tier) {
        super(maxCharge, material.build(), tier);
        this.settings = settings;
    }

    /*
    @Override
    public boolean isDamageable() {
        return false;
    }
     */

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemAppendTooltipEvent e, Options options) {
        CompatItemProvider.super.appendTooltip(e, options);
        TRItemUtils.buildActiveTooltip(e.stack, e.tooltip);

    }
}

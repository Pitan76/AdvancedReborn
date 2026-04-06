package net.pitan76.advancedreborn.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.mcpitanlib.api.event.item.*;
import net.pitan76.mcpitanlib.api.item.CompatibleArmorMaterial;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatItemProvider;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import org.jetbrains.annotations.Nullable;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.utils.TRItemUtils;

public class BetterBatpackItem extends ExtendItem implements CompatItemProvider, RcEnergyItem {
    public final long maxCharge;
    private final RcEnergyTier energyTier;

    protected CompatibleItemSettings settings;

    private static int num = 0;

    @Override
    public CompatibleItemSettings getCompatSettings() {
        return settings;
    }

    public BetterBatpackItem(CompatibleItemSettings settings, int maxCharge, CompatibleArmorMaterial material, RcEnergyTier tier) {
//        super(maxCharge, material.build(), tier, "better_batpack_" + num++);
        super(CompatibleItemSettings.of(AdvancedReborn._id("better_batpack_" + num++)).maxCount(1).build().humanoidArmor(material.build(), ArmorType.CHESTPLATE));
        this.settings = settings;
        this.maxCharge = maxCharge;
        this.energyTier = tier;
    }

    @Override
    public void inventoryTick(InventoryTickEvent e, Options options) {
        if (!e.isClient()) {
            if (e.isPlayer()) {
                ItemUtils.distributePowerToInventory((Player) e.getEntity(), e.stack, this.getTier().getMaxOutput());
            }
        }
    }

    public RcEnergyTier getTier() {
        return this.energyTier;
    }

    @Override
    public int getItemBarStep(ItemBarStepArgs args) {
        return ItemUtils.getPowerForDurabilityBar(args.stack);
    }

    @Override
    public boolean isItemBarVisible(ItemBarVisibleArgs args) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemBarColorArgs args) {
        return ItemUtils.getColorForDurabilityBar(args.stack);
    }

    public long getEnergyCapacity(ItemStack stack) {
        return this.maxCharge;
    }

    public @Nullable EquipmentSlot getSlotType() {
        Equippable equippableComponent = this.components().get(DataComponents.EQUIPPABLE);
        return equippableComponent != null ? equippableComponent.slot() : null;
    }

    /*
    @Override
    public boolean isDamageable() {
        return false;
    }
     */

    @Override
    public boolean isEnchantable(EnchantableArgs args) {
        return true;
    }

    @Override
    public void appendTooltip(ItemAppendTooltipEvent e, Options options) {
        CompatItemProvider.super.appendTooltip(e, options);
        TRItemUtils.buildActiveTooltip(e.stack, e.textConsumer);
    }
}

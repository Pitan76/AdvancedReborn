package net.pitan76.advancedreborn.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.event.item.*;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.StackActionResult;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.items.BatteryItem;

public class AdvancedBattery extends ExtendItem implements RcEnergyItem {
    public int maxEnergy;
    public RcEnergyTier tier;

    public AdvancedBattery(CompatibleItemSettings settings, int maxEnergy, RcEnergyTier tier) {
        super(settings);
        this.maxEnergy = maxEnergy;
        this.tier = tier;
    }

    @Override
    public StackActionResult onRightClick(ItemUseEvent e) {
        final ItemStack stack = e.getStack();
        if (e.isSneaking()) {
            ItemUtils.switchActive(stack, 1, e.user.getEntity());
            return e.success();
        }
        return e.success();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        ItemUtils.checkActive(stack, 1, entity);
        if (world.isClient) {
            return;
        }
        if (!ItemUtils.isActive(stack)){
            return;
        }
        if (entity instanceof PlayerEntity) {
            ItemUtils.distributePowerToInventory((PlayerEntity) entity, stack, tier.getMaxOutput(), (testStack) -> !(testStack.getItem() instanceof BatteryItem));
        }
    }

    @Override
    public void appendTooltip(ItemAppendTooltipEvent e) {
        ItemUtils.buildActiveTooltip(e.stack, e.tooltip);
    }

    @Override
    public long getEnergyCapacity() {
        return maxEnergy;
    }

    @Override
    public RcEnergyTier getTier() {
        return tier;
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
}

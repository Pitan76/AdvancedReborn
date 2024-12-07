package net.pitan76.advancedreborn.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.event.item.*;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.util.StackActionResult;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.items.BatteryItem;
import techreborn.utils.MessageIDs;

public class AdvancedBattery extends CompatItem implements RcEnergyItem {
    public int maxEnergy;
    public RcEnergyTier tier;

    public AdvancedBattery(CompatibleItemSettings settings, int maxEnergy, RcEnergyTier tier) {
        super(settings);
        this.maxEnergy = maxEnergy;
        this.tier = tier;
    }

    @Override
    public StackActionResult onRightClick(ItemUseEvent e) {
        final ItemStack stack = e.user.getStackInHand(e.hand);
        if (e.isSneaking()) {
            ItemUtils.switchActive(stack, 1, MessageIDs.poweredToolID, e.user.getPlayerEntity());
            return e.success();
        }
        return e.pass();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        ItemUtils.checkActive(stack, 1, MessageIDs.poweredToolID, entity);
        if (WorldUtil.isClient(world)) return;
        if (!ItemUtils.isActive(stack)) return;

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
    public int getItemBarColor(ItemBarColorArgs args) {
        return ItemUtils.getColorForDurabilityBar(args.stack);
    }

    @Override
    public boolean isItemBarVisible(ItemBarVisibleArgs args) {
        return true;
    }
}

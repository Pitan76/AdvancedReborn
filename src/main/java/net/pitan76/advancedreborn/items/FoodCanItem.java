package net.pitan76.advancedreborn.items;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.Items;
import net.pitan76.mcpitanlib.api.event.item.ItemFinishUsingEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.StackActionResult;

public class FoodCanItem extends ExtendItem {
    public FoodCanItem(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public StackActionResult onRightClick(ItemUseEvent e) {
        StackActionResult result = super.onRightClick(e);
        if (result.equals(StackActionResult.CONSUME)) {
            e.user.getPlayerEntity().heal(1);
        }
        return result;
    }

    public ItemStack onFinishUsing(ItemFinishUsingEvent e) {
        World world = e.world;
        ItemStack stack = e.stack;

        PlayerEntity playerEntity = e.user instanceof PlayerEntity ? (PlayerEntity) e.user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }
        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.getAbilities().creativeMode) {
                playerEntity.eatFood(world, stack);
            }
        }

        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.EMPTY_CAN.get());
            }

            if (playerEntity != null) {
                ItemStack emptyCan = new ItemStack(Items.EMPTY_CAN.get());
                boolean inserted = playerEntity.getInventory().insertStack(emptyCan);
                if (!inserted) {
                    playerEntity.dropItem(emptyCan, false);
                }
                if (playerEntity.canConsume(false)) super.onFinishUsing(e);
            }
        }

        return stack;
    }
}

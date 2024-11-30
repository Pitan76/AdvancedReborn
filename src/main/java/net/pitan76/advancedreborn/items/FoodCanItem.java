package net.pitan76.advancedreborn.items;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.Items;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemFinishUsingEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.StackActionResult;

import static net.pitan76.advancedreborn.Items.CAN_FOOD_COMPONENT;

public class FoodCanItem extends CompatItem {
    public FoodCanItem(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public StackActionResult onRightClick(ItemUseEvent e) {
        StackActionResult result = super.onRightClick(e);
        if (result.toActionResult().equals(ActionResult.CONSUME)) {
            e.user.getPlayerEntity().heal(1);
        }
        return result;
    }

    public ItemStack onFinishUsing(ItemFinishUsingEvent e) {
        World world = e.world;
        ItemStack stack = e.stack;

        PlayerEntity playerEntity = e.user instanceof PlayerEntity ? (PlayerEntity) e.user : null;
        if (playerEntity instanceof ServerPlayerEntity)
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);


        if (playerEntity != null) {
            Player player = new Player(playerEntity);

            player.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!player.isCreative()) {
                player.getEntity().eatFood(world, stack, CAN_FOOD_COMPONENT.build());
            }
        }

        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            if (stack.isEmpty()) {
                return ItemStackUtil.create(Items.EMPTY_CAN.get());
            }

            if (playerEntity != null) {
                ItemStack emptyCan = ItemStackUtil.create(Items.EMPTY_CAN.get());
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

package net.pitan76.advancedreborn.items;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
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
        if (result.toActionResult().equals(InteractionResult.CONSUME)) {
            e.user.getPlayerEntity().heal(1);
        }
        return result;
    }

    public ItemStack onFinishUsing(ItemFinishUsingEvent e) {
        ItemStack stack = e.stack;

        net.minecraft.world.entity.player.Player playerEntity = e.user instanceof net.minecraft.world.entity.player.Player ? (net.minecraft.world.entity.player.Player) e.user : null;
        if (playerEntity instanceof ServerPlayer)
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)playerEntity, stack);


        if (playerEntity != null) {
            Player player = new Player(playerEntity);

            player.incrementStat(Stats.ITEM_USED.get(this));
            if (!player.isCreative()) {
                player.getEntity().getFoodData().eat(CAN_FOOD_COMPONENT.build());
            }
        }

        if (playerEntity == null || !playerEntity.getAbilities().instabuild) {
            if (stack.isEmpty()) {
                return ItemStackUtil.create(Items.EMPTY_CAN.get());
            }

            if (playerEntity != null) {
                ItemStack emptyCan = ItemStackUtil.create(Items.EMPTY_CAN.get());
                boolean inserted = playerEntity.getInventory().add(emptyCan);
                if (!inserted) {
                    playerEntity.drop(emptyCan, false);
                }
                if (playerEntity.canEat(false)) super.onFinishUsing(e);
            }
        }

        return stack;
    }
}

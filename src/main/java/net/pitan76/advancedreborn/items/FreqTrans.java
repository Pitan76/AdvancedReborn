package net.pitan76.advancedreborn.items;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.pitan76.advancedreborn.Items;
import net.pitan76.advancedreborn.tile.TeleporterTile;
import net.pitan76.mcpitanlib.api.event.item.ItemAppendTooltipEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;

import java.util.List;

public class FreqTrans extends CompatItem {
    public FreqTrans(CompatibleItemSettings settings) {
        super(settings);
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem().equals(Items.FREQ_TRANS.getOrNull())) {
                if (WorldUtil.isClient(world)) return InteractionResult.PASS;
                BlockEntity tile = WorldUtil.getBlockEntity(world, pos);

                if (tile instanceof TeleporterTile) {
                    if (!CustomDataUtil.hasNbt(stack)) return InteractionResult.FAIL;
                    CompoundTag tag = CustomDataUtil.getNbt(stack);
                    if (!tag.contains("tpX") || !tag.contains("tpY") || !tag.contains("tpZ")) return InteractionResult.FAIL;
                    TeleporterTile machine = (TeleporterTile) tile;
                    machine.setTeleportPos(PosUtil.flooredBlockPos(NbtUtil.getDouble(tag, "tpX"), NbtUtil.getDouble(tag, "tpY"), NbtUtil.getDouble(tag, "tpZ")));
                    player.sendMessage(TextUtil.literal("Loaded Teleport Pos from The Frequency Transmitter.(" + NbtUtil.getDouble(tag, "tpX") + "," + NbtUtil.getDouble(tag, "tpY") + "," + NbtUtil.getDouble(tag, "tpZ") + ")"), false);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }

    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        BlockEntity tile = e.getBlockEntity();
        if (tile == null) return e.pass();
        if (!(tile instanceof TeleporterTile)) return e.pass();
        if (e.isClient()) return e.success();

        TeleporterTile machine = (TeleporterTile) tile;
        ItemStack stack = e.player.getStackInHand(e.hand);
        CompoundTag tag = CustomDataUtil.getNbt(stack);
        if (tag == null)
            tag = NbtUtil.create();

        tag.putDouble("tpX", machine.getX());
        tag.putDouble("tpY", machine.getY());
        tag.putDouble("tpZ", machine.getZ());
        CustomDataUtil.setNbt(stack, tag);

        e.player.sendMessage(TextUtil.literal("Saved Machine's Pos to The Frequency Transmitter.(" + NbtUtil.getDouble(tag, "tpX") + "," + NbtUtil.getDouble(tag, "tpY") + "," + NbtUtil.getDouble(tag, "tpZ") + ")"));
        return e.success();
    }

    @Override
    public void appendTooltip(ItemAppendTooltipEvent e) {
        List<Component> tooltip = e.getTooltip();
        ItemStack stack = e.getItem();

        tooltip.add(TextUtil.literal("Save pos to Wrench when Right Click with Teleporter."));
        tooltip.add(TextUtil.literal("Load pos from Wrench when Left Click with Teleporter."));
        if (CustomDataUtil.hasNbt(stack)) {
            CompoundTag nbt = CustomDataUtil.getNbt(stack);
            if (nbt != null) if (nbt.contains("tpX") && nbt.contains("tpY") && nbt.contains("tpZ"))
                tooltip.add(TextUtil.literal("Pos(" + nbt.getDouble("tpX") + "," + nbt.getDouble("tpY") + "," + nbt.getDouble("tpZ") + ")"));
        }
        super.appendTooltip(e);
    }
}

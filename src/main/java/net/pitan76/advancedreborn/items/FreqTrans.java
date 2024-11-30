package net.pitan76.advancedreborn.items;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
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
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem().equals(Items.FREQ_TRANS.getOrNull())) {
                if (WorldUtil.isClient(world)) return ActionResult.PASS;
                BlockEntity tile = WorldUtil.getBlockEntity(world, pos);

                if (tile instanceof TeleporterTile) {
                    if (!CustomDataUtil.hasNbt(stack)) return ActionResult.FAIL;
                    NbtCompound tag = CustomDataUtil.getNbt(stack);
                    if (!tag.contains("tpX") || !tag.contains("tpY") || !tag.contains("tpZ")) return ActionResult.FAIL;
                    TeleporterTile machine = (TeleporterTile) tile;
                    machine.setTeleportPos(PosUtil.flooredBlockPos(tag.getDouble("tpX"), tag.getDouble("tpY"), tag.getDouble("tpZ")));
                    player.sendMessage(TextUtil.literal("Loaded Teleport Pos from The Frequency Transmitter.(" + tag.getDouble("tpX") + "," + tag.getDouble("tpY") + "," + tag.getDouble("tpZ") + ")"), false);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }

    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        BlockEntity tile = e.getBlockEntity();
        if (tile == null) return e.pass();
        if (!(tile instanceof TeleporterTile)) return e.pass();
        if (e.isClient()) return e.success();

        TeleporterTile machine = (TeleporterTile) tile;
        ItemStack stack = e.player.getStackInHand(e.hand);
        NbtCompound tag = CustomDataUtil.getNbt(stack);
        if (tag == null)
            tag = NbtUtil.create();

        tag.putDouble("tpX", machine.getX());
        tag.putDouble("tpY", machine.getY());
        tag.putDouble("tpZ", machine.getZ());
        CustomDataUtil.setNbt(stack, tag);

        e.player.sendMessage(TextUtil.literal("Saved Machine's Pos to The Frequency Transmitter.(" + tag.getDouble("tpX") + "," + tag.getDouble("tpY") + "," + tag.getDouble("tpZ") + ")"));
        return e.success();
    }

    @Override
    public void appendTooltip(ItemAppendTooltipEvent e) {
        List<Text> tooltip = e.getTooltip();
        ItemStack stack = e.getStack();

        tooltip.add(TextUtil.literal("Save pos to Wrench when Right Click with Teleporter."));
        tooltip.add(TextUtil.literal("Load pos from Wrench when Left Click with Teleporter."));
        if (CustomDataUtil.hasNbt(stack)) {
            NbtCompound nbt = CustomDataUtil.getNbt(stack);
            if (nbt != null) if (nbt.contains("tpX") && nbt.contains("tpY") && nbt.contains("tpZ"))
                tooltip.add(TextUtil.literal("Pos(" + nbt.getDouble("tpX") + "," + nbt.getDouble("tpY") + "," + nbt.getDouble("tpZ") + ")"));
        }
        super.appendTooltip(e);
    }
}

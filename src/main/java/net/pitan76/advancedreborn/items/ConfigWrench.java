package net.pitan76.advancedreborn.items;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.Items;
import net.pitan76.advancedreborn.mixins.MachineBaseBlockEntityAccessor;
import net.pitan76.mcpitanlib.api.event.item.ItemAppendTooltipEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import reborncore.common.blockentity.FluidConfiguration;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.blockentity.SlotConfiguration;

import java.util.List;

public class ConfigWrench extends ExtendItem {
    public ConfigWrench(CompatibleItemSettings settings) {
        super(settings);
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem().equals(Items.CONFIG_WRENCH)) {
                if (world.isClient()) return ActionResult.PASS;
                BlockEntity tile = world.getBlockEntity(pos);
                if (tile instanceof MachineBaseBlockEntity) {
                    if (!stack.hasNbt()) return ActionResult.FAIL;
                    NbtCompound tag = stack.getNbt();
                    if (!tag.contains("configs")) return ActionResult.FAIL;
                    NbtCompound config = tag.getCompound("configs");
                    MachineBaseBlockEntityAccessor accessor = (MachineBaseBlockEntityAccessor) tile;
                    if (config.contains("slot"))
                        accessor.getSlotConfiguration().read(config.getCompound("slot"));
                    if (config.contains("fluid"))
                        accessor.getFluidConfiguration().read(config.getCompound("fluid"));
                    if (config.contains("redstone"))
                        accessor.getRedstoneConfiguration().read(config.getCompound("redstone"));
                    player.sendMessage(TextUtil.literal("Loaded Configuration from The Config Wrench."), false);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }

    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        World world = e.world;
        BlockPos pos = e.hit.getBlockPos();
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile == null) return CompatActionResult.PASS;
        if (!(tile instanceof MachineBaseBlockEntity)) return CompatActionResult.PASS;
        if (world.isClient()) return CompatActionResult.SUCCESS;
        MachineBaseBlockEntity machine = (MachineBaseBlockEntity) tile;
        MachineBaseBlockEntityAccessor machineAccessor = (MachineBaseBlockEntityAccessor) machine;
        SlotConfiguration slotConfig = null;
        FluidConfiguration fluidConfig = null;
        if (machine.hasSlotConfig()) slotConfig = machineAccessor.getSlotConfiguration();
        RedstoneConfiguration redstoneConfig = machineAccessor.getRedstoneConfiguration();
        if (machine.fluidConfiguration != null)
            fluidConfig = machineAccessor.getFluidConfiguration();

        ItemStack stack = e.player.getPlayerEntity().getStackInHand(e.hand);
        NbtCompound tag = stack.getNbt();
        if (tag == null) {
            tag = new NbtCompound();
        }
        NbtCompound config = new NbtCompound();
        if (slotConfig != null)
            config.put("slot", slotConfig.write());
        if (fluidConfig != null)
            config.put("fluid", fluidConfig.write());
        if (redstoneConfig != null)
            config.put("redstone", redstoneConfig.write());
        tag.put("configs", config);
        stack.setNbt(tag);
        e.player.sendMessage(TextUtil.literal("Saved Configuration to The Config Wrench."));
        return CompatActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemAppendTooltipEvent e) {
        List<Text> tooltip = e.getTooltip();

        tooltip.add(TextUtil.literal("Save TR Machine configurations to Wrench when Right Click with TR Machine."));
        tooltip.add(TextUtil.literal("Load TR Machine configurations from Wrench when Left Click with TR Machine."));
        super.appendTooltip(e);
    }
}

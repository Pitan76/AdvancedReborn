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
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.*;
import reborncore.common.blockentity.FluidConfiguration;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.blockentity.SlotConfiguration;

import java.util.List;
import java.util.Map;

public class ConfigWrench extends CompatItem {
    public ConfigWrench(CompatibleItemSettings settings) {
        super(settings);
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem().equals(Items.CONFIG_WRENCH.getOrNull())) {
                if (WorldUtil.isClient(world)) return ActionResult.PASS;
                BlockEntity tile = WorldUtil.getBlockEntity(world, pos);
                if (tile instanceof MachineBaseBlockEntity) {
                    if (!CustomDataUtil.hasNbt(stack)) return ActionResult.FAIL;
                    NbtCompound tag = CustomDataUtil.getNbt(stack);
                    if (!tag.contains("configs")) return ActionResult.FAIL;
                    NbtCompound config = tag.getCompound("configs");
                    MachineBaseBlockEntityAccessor accessor = (MachineBaseBlockEntityAccessor) tile;
                    if (config.contains("slot"))
                        accessor.getSlotConfiguration().read(config.getCompound("slot"));
                    if (config.contains("fluid"))
                        accessor.getFluidConfiguration().read(config.getCompound("fluid"));
                    if (config.contains("redstone")) {
                        Map<RedstoneConfiguration.Element, RedstoneConfiguration.State> stateMap = accessor.getRedstoneConfiguration().stateMap();
                        NbtCompound redstone = config.getCompound("redstone");
                        stateMap.forEach((element, state) -> {
                            if (redstone.contains(element.name())) {
                                stateMap.put(element, RedstoneConfiguration.State.valueOf(redstone.getString(element.name())));
                            }
                        });
                    }
                    player.sendMessage(TextUtil.literal("Loaded Configuration from The Config Wrench."), false);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }

    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        World world = e.world;
        BlockPos pos = e.getBlockPos();
        BlockEntity tile = WorldUtil.getBlockEntity(world, pos);
        if (tile == null) return e.pass();
        if (!(tile instanceof MachineBaseBlockEntity)) return e.pass();
        if (e.isClient()) return e.success();
        MachineBaseBlockEntity machine = (MachineBaseBlockEntity) tile;
        MachineBaseBlockEntityAccessor machineAccessor = (MachineBaseBlockEntityAccessor) machine;
        SlotConfiguration slotConfig = null;
        FluidConfiguration fluidConfig = null;
        if (machine.hasSlotConfig()) slotConfig = machineAccessor.getSlotConfiguration();
        RedstoneConfiguration redstoneConfig = machineAccessor.getRedstoneConfiguration();
        if (machine.fluidConfiguration != null)
            fluidConfig = machineAccessor.getFluidConfiguration();

        ItemStack stack = e.player.getStackInHand(e.hand);
        NbtCompound tag = CustomDataUtil.getNbt(stack);
        if (tag == null) {
            tag = NbtUtil.create();
        }
        NbtCompound config = NbtUtil.create();
        if (slotConfig != null)
            config.put("slot", slotConfig.write());
        if (fluidConfig != null)
            config.put("fluid", fluidConfig.write());
        if (redstoneConfig != null) {
            NbtCompound redstone = NbtUtil.create();
            
            redstoneConfig.stateMap().forEach((element, state) -> {
                redstone.putString(element.name(), state.name());
            });
            
            config.put("redstone", redstone);
        }
        tag.put("configs", config);
        CustomDataUtil.setNbt(stack, tag);
        e.player.sendMessage(TextUtil.literal("Saved Configuration to The Config Wrench."));
        return e.success();
    }

    @Override
    public void appendTooltip(ItemAppendTooltipEvent e) {
        List<Text> tooltip = e.getTooltip();

        tooltip.add(TextUtil.literal("Save TR Machine configurations to Wrench when Right Click with TR Machine."));
        tooltip.add(TextUtil.literal("Load TR Machine configurations from Wrench when Left Click with TR Machine."));
        super.appendTooltip(e);
    }
}

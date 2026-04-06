package net.pitan76.advancedreborn.items;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.ProblemReporter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
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
                if (WorldUtil.isClient(world)) return InteractionResult.PASS;
                BlockEntity tile = WorldUtil.getBlockEntity(world, pos);
                if (tile instanceof MachineBaseBlockEntity) {
                    if (!CustomDataUtil.hasNbt(stack)) return InteractionResult.FAIL;
                    CompoundTag tag = CustomDataUtil.getNbt(stack);
                    if (!tag.contains("configs")) return InteractionResult.FAIL;
                    CompoundTag config = NbtUtil.get(tag, "configs");
                    TagValueInput readView = TagValueInput.create(ProblemReporter.EMPTY, world.getRegistryManager(), config);

                    MachineBaseBlockEntityAccessor accessor = (MachineBaseBlockEntityAccessor) tile;
                    if (config.contains("slot"))
                        accessor.getSlotConfiguration().read(readView.getReadView("slot"));
                    if (config.contains("fluid"))
                        accessor.getFluidConfiguration().read(readView.getReadView("fluid"));
                    if (config.contains("redstone")) {
                        Map<RedstoneConfiguration.Element, RedstoneConfiguration.State> stateMap = accessor.getRedstoneConfiguration().stateMap();
                        CompoundTag redstone = NbtUtil.get(config, "redstone");
                        stateMap.forEach((element, state) -> {
                            if (redstone.contains(element.name())) {
                                stateMap.put(element, RedstoneConfiguration.State.valueOf(NbtUtil.getString(redstone, element.name())));
                            }
                        });
                    }
                    player.sendMessage(TextUtil.literal("Loaded Configuration from The Config Wrench."), false);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }

    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        Level world = e.world;
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
        CompoundTag tag = CustomDataUtil.getNbt(stack);
        if (tag == null) {
            tag = NbtUtil.create();
        }
        CompoundTag config = NbtUtil.create();
        if (slotConfig != null) {
            TagValueOutput view = TagValueOutput.create(ProblemReporter.EMPTY, world.getRegistryManager());
            slotConfig.write(view);
            config.put("slot", view.getNbt());
        }
        if (fluidConfig != null) {
            TagValueOutput view = TagValueOutput.create(ProblemReporter.EMPTY, world.getRegistryManager());
            fluidConfig.write(view);
            config.put("fluid", view.getNbt());
        }
        if (redstoneConfig != null) {
            CompoundTag redstone = NbtUtil.create();
            
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
        List<Component> tooltip = e.getTooltip();

        tooltip.add(TextUtil.literal("Save TR Machine configurations to Wrench when Right Click with TR Machine."));
        tooltip.add(TextUtil.literal("Load TR Machine configurations from Wrench when Left Click with TR Machine."));
        super.appendTooltip(e);
    }
}

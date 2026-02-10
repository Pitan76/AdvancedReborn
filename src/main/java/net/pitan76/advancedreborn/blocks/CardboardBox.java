package net.pitan76.advancedreborn.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.tile.CardboardBoxTile;
import net.pitan76.mcpitanlib.api.block.CompatBlockRenderType;
import net.pitan76.mcpitanlib.api.block.args.RenderTypeArgs;
import net.pitan76.mcpitanlib.api.block.args.v2.GetComparatorOutputArgs;
import net.pitan76.mcpitanlib.api.block.args.v2.HasComparatorOutputArgs;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.ExtendBlockEntityProvider;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.event.block.*;
import net.pitan76.mcpitanlib.api.event.block.result.BlockBreakResult;
import net.pitan76.mcpitanlib.api.event.item.ItemAppendTooltipEvent;
import net.pitan76.mcpitanlib.api.event.nbt.NbtRWArgs;
import net.pitan76.mcpitanlib.api.state.property.CompatProperties;
import net.pitan76.mcpitanlib.api.state.property.DirectionProperty;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;

import java.util.List;

public class CardboardBox extends CompatBlock implements ExtendBlockEntityProvider {

    public static CompatIdentifier CONTENTS = CompatIdentifier.of("contents");
    public static DirectionProperty FACING = CompatProperties.HORIZONTAL_FACING;

    public CardboardBox(CompatibleBlockSettings settings) {
        super(settings);
        setNewDefaultState(getNewDefaultState().with(FACING.getProperty(), Direction.NORTH));
    }

    public void setFacing(Direction facing, World world, BlockPos pos) {
        WorldUtil.setBlockState(world, pos, WorldUtil.getBlockState(world, pos).with(FACING.getProperty(), facing));
    }

    public Direction getFacing(BlockState state) {
        return FACING.get(state);
    }

    @Override
    public void appendProperties(AppendPropertiesArgs args) {
        super.appendProperties(args);
        args.addProperty(FACING);
    }

    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new CardboardBoxTile(event);
    }

    public BlockBreakResult onBreak(BlockBreakEvent e) {
        World world = e.world;
        BlockPos pos = e.pos;

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof CardboardBoxTile) {
            CardboardBoxTile tile = (CardboardBoxTile) blockEntity;
            if (!WorldUtil.isClient(world) && e.player.isCreative() && !tile.isEmpty()) {
                ItemStack stack = ItemStackUtil.create(this.asItem());
//                NbtCompound nbt = tile.writeInventoryNbt(NbtUtil.create());
                NbtCompound nbt = BlockEntityUtil.getBlockEntityNbt(world, tile);
                if (!NbtUtil.has(nbt, "id"))
                    NbtUtil.putString(nbt, "id", BlockEntityTypeUtil.toID(Tiles.CARDBOARD_BOX_TILE.get()).toString());

                BlockEntityDataUtil.setBlockEntityNbt(stack, nbt);
//                if (tile.hasNote()) NbtUtil.set(nbt, "note" ,tile.getNote());
//                if (!nbt.isEmpty()) stack.set(DataComponentTypes.BLOCK_ENTITY_DATA, TypedEntityData.create(Tiles.CARDBOARD_BOX_TILE.get(), nbt));
//                if (tile.hasCustomName()) stack.set(DataComponentTypes.CUSTOM_NAME, tile.getCustomName());

                ItemEntity itemEntity = ItemEntityUtil.create(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
                ItemEntityUtil.setToDefaultPickupDelay(itemEntity);
                WorldUtil.spawnEntity(world, itemEntity);
            }
        }
        return super.onBreak(e);
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        if (e.isSameState())
            return;

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof CardboardBoxTile)
            e.updateComparators();

        super.onStateReplaced(e);
    }

    @Override
    public void onPlaced(BlockPlacedEvent e) {
        LivingEntity placer = e.placer;
        World world = e.world;
        BlockPos pos = e.pos;
        ItemStack stack = e.stack;

        if (placer != null)
            setFacing(placer.getHorizontalFacing().getOpposite(), world, pos);

        if (stack.contains(DataComponentTypes.CUSTOM_NAME)) {
            BlockEntity blockEntity = WorldUtil.getBlockEntity(world, pos);
            if (blockEntity instanceof CardboardBoxTile) {
                ((CardboardBoxTile)blockEntity).setCustomName(stack.getName());
            }
        }
        super.onPlaced(e);
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        if (e.isClient()) return e.success();

        if (e.player.getPlayerEntity().isSpectator())
            return e.consume();

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof CardboardBoxTile) {
            CardboardBoxTile tile = (CardboardBoxTile) blockEntity;
            e.player.openExtendedMenu(tile);
            return e.consume();
        }
        return e.pass();
    }

    @Override
    public ItemStack getPickStack(PickStackEvent e) {
        ItemStack itemStack = super.getPickStack(e);
        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof CardboardBoxTile)
            BlockEntityUtil.setStackNbt(blockEntity, itemStack, RegistryLookupUtil.getRegistryLookup(blockEntity));

        return itemStack;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemAppendTooltipEvent e) {
        super.appendTooltip(e);

        if (!e.stack.contains(DataComponentTypes.BLOCK_ENTITY_DATA)) return;

        TypedEntityData<BlockEntityType<?>> entityData = e.stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
        if (entityData != null) {
            NbtCompound nbt = entityData.copyNbtWithoutId();
            if (nbt.contains("note")) {
                e.addTooltip(TextUtil.literal(NbtUtil.getString(nbt, "note")));
            }
            if (NbtUtil.has(nbt, "LootTable")) {
                e.addTooltip(TextUtil.literal("???????"));
            }
            if (NbtUtil.has(nbt, "Items")) {
                DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
                NbtRWArgs args = new NbtRWArgs(nbt, e.getRegistryLookup());
                InventoryUtil.readNbt(args, defaultedList);
                int i = 0;
                int j = 0;

                for (ItemStack itemStack : defaultedList) {
                    if (!itemStack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            MutableText mutableText = itemStack.getName().copy();
                            mutableText.append(" x").append(String.valueOf(itemStack.getCount()));
                            e.addTooltip(mutableText);
                        }
                    }
                }
                if (j - i > 0) {
                    e.addTooltip((TextUtil.translatable("container.advanced_reborn.cardboard_box.more", new Object[]{j - i})).copy().formatted(Formatting.ITALIC));
                }
            }
        }

    }

    @Override
    public CompatBlockRenderType getRenderType(RenderTypeArgs args) {
        return CompatBlockRenderType.MODEL;
    }

    @Override
    public boolean hasComparatorOutput(HasComparatorOutputArgs args) {
        return true;
    }

    @Override
    public int getComparatorOutput(GetComparatorOutputArgs args) {
        return args.calcComparatorOutputFromBlockEntity();
    }

    @Override
    public List<ItemStack> getDroppedStacks(DroppedStacksArgs args) {
        BlockEntity blockEntity = args.getBlockEntity();
        if (blockEntity instanceof CardboardBoxTile) {
            CardboardBoxTile tile = (CardboardBoxTile)blockEntity;
            args.builder = args.builder.addDynamicDrop(CONTENTS.toMinecraft(), (consumer) -> {
                for (int i = 0; i < tile.size(); ++i) {
                    consumer.accept(tile.getStack(i));
                }
            });
        }
        return super.getDroppedStacks(args);
    }
}

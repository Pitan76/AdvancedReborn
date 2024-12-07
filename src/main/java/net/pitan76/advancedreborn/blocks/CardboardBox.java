package net.pitan76.advancedreborn.blocks;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.tile.CardboardBoxTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.block.ExtendBlockEntityProvider;
import net.pitan76.mcpitanlib.api.event.block.*;
import net.pitan76.mcpitanlib.api.event.block.result.BlockBreakResult;
import net.pitan76.mcpitanlib.api.event.item.ItemAppendTooltipEvent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.TextUtil;

import java.util.List;

public class CardboardBox extends CompatBlock implements ExtendBlockEntityProvider {

    public static Identifier CONTENTS = new Identifier("contents");
    public static DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public CardboardBox(CompatibleBlockSettings settings) {
        super(settings);
        setNewDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    public void setFacing(Direction facing, World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
    }

    public Direction getFacing(BlockState state) {
        return state.get(FACING);
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
            if (!world.isClient() && e.player.isCreative() && !tile.isEmpty()) {
                ItemStack itemStack = new ItemStack(this);
                NbtCompound nbtCompound = tile.writeInventoryNbt(new NbtCompound());
                if (tile.hasNote()) {
                    nbtCompound.putString("note" ,tile.getNote());
                }
                if (!nbtCompound.isEmpty()) {
                    itemStack.setSubNbt("BlockEntityTag", nbtCompound);
                }
                if (tile.hasCustomName()) {
                    itemStack.setCustomName(tile.getCustomName());
                }

                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
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

        if(placer != null) {
            setFacing(placer.getHorizontalFacing().getOpposite(), world, pos);
        }
        if (stack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CardboardBoxTile) {
                ((CardboardBoxTile)blockEntity).setCustomName(stack.getName());
            }
        }
        super.onPlaced(e);
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        if (e.isClient()) {
            return e.success();
        } else if (e.player.isSpectator()) {
            return e.consume();
        } else {
            BlockEntity blockEntity = e.getBlockEntity();
            if (blockEntity instanceof CardboardBoxTile) {
                CardboardBoxTile tile = (CardboardBoxTile)blockEntity;
                e.player.openGuiScreen(tile);
                e.player.incrementStat(Stats.OPEN_SHULKER_BOX);
                PiglinBrain.onGuardedBlockInteracted(e.player.getPlayerEntity(), true);
                return e.consume();
            } else {
                return e.pass();
            }
        }
    }

    @Override
    public ItemStack getPickStack(PickStackEvent e) {
        ItemStack itemStack = super.getPickStack(e);
        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof CardboardBoxTile)
            blockEntity.setStackNbt(itemStack);

        return itemStack;
    }

    @Override
    public void appendTooltip(ItemAppendTooltipEvent e) {
        super.appendTooltip(e);
        NbtCompound nbtCompound = e.stack.getSubNbt("BlockEntityTag");
        if (nbtCompound != null) {
            if (nbtCompound.contains("note")) {
                e.addTooltip(TextUtil.literal(nbtCompound.getString("note")));
            }
            if (nbtCompound.contains("LootTable", 8)) {
                e.addTooltip(TextUtil.literal("???????"));
            }
            if (nbtCompound.contains("Items", 9)) {
                DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
                Inventories.readNbt(nbtCompound, defaultedList);
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

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput((Inventory)world.getBlockEntity(pos));
    }

    @Override
    public List<ItemStack> getDroppedStacks(DroppedStacksArgs args) {
        BlockEntity blockEntity = args.getBlockEntity();
        if (blockEntity instanceof CardboardBoxTile) {
            CardboardBoxTile tile = (CardboardBoxTile)blockEntity;
            args.builder = args.builder.putDrop(CONTENTS, (lootContext, consumer) -> {
                for(int i = 0; i < tile.size(); ++i) {
                    consumer.accept(tile.getStack(i));
                }
            });
        }
        return super.getDroppedStacks(args);
    }
}

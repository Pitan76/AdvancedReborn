package net.pitan76.advancedreborn.tile;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.enchantment.CompatEnchantment;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.BlockEntityUtil;
import net.pitan76.mcpitanlib.api.util.EnchantmentUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentExtractorTile extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

    public Block toolDrop;
    public int energySlot;
    public RebornInventory<?> inventory;
    public int coolDownDefault = 100;
    public int coolDown = coolDownDefault;

    public EnchantmentExtractorTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        toolDrop = Blocks.ENCHANTMENT_EXTRACTOR.getOrNull();
        energySlot = 10;
        inventory = new RebornInventory<>(12, "EnchantmentExtractorTile", 64, this);
        checkTier();
    }

    public EnchantmentExtractorTile(BlockPos pos, BlockState state) {
        this(Tiles.ENCHANTMENT_EXTRACTOR_TILE.getOrNull(), pos, state);
    }

    public EnchantmentExtractorTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__enchantment_extractor").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this)
                .slot(11, 60, 25) // Book Input

                .slot(0, 40, 25) // Input
                .slot(1, 40, 65) // Output
                // Enchantment book output
                .slot(2, 82, 40).slot(3, 100, 40).slot(4, 118, 40).slot(5, 136, 40)
                .slot(6, 82, 58).slot(7, 100, 58).slot(8, 118, 58).slot(9, 136, 58)
                .energySlot(10, 8, 72).syncEnergyValue()
                .sync(ByteBufCodecs.VAR_INT, this::getCoolDown, this::setCoolDown).sync(ByteBufCodecs.VAR_INT, this::getCoolDownDefault, this::setCoolDownDefault).addInventory().create(this, syncID);
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDownDefault(int coolDownDefault) {
        this.coolDownDefault = coolDownDefault;
    }

    public int getCoolDownDefault() {
        return coolDownDefault;
    }

    public long getBaseMaxPower() {
        return AutoConfigAddon.getConfig().enchantmentExtractorMaxEnergy;
    }

    public long getBaseMaxOutput() {
        return 0;
    }

    public long getBaseMaxInput() {
        return AutoConfigAddon.getConfig().enchantmentExtractorMaxInput;
    }

    public long getBaseUsePower() {
        return AutoConfigAddon.getConfig().enchantmentExtractorUseEnergy;
    }

    public boolean canProvideEnergy(Direction side) {
        return false;
    }

    public int getProgressScaled(int scale) {
        return (getCoolDownDefault() - getCoolDown()) * scale / getCoolDownDefault();
    }

    public ItemStack getToolDrop(Player p0) {
        return ItemStackUtil.create(toolDrop.asItem(), 1);
    }

    public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        if (world == null || WorldUtil.isClient(world)) {
            return;
        }
        charge(energySlot);

        BlockMachineBase block = (BlockMachineBase) state.getBlock();
        block.setActive(getCoolDown() != getCoolDownDefault(), world, getPos());
        if (!getInventory().getItem(1).isEmpty() || getInventory().getItem(0).isEmpty() || getInventory().getItem(11).isEmpty()) {
            if (getCoolDown() <= 0) setCoolDown(getCoolDownDefault());
            return;
        }
        if (getEnergy() > getEuPerTick(getBaseUsePower())) {
            if (!getInventory().getItem(0).isEmpty()) {
                useEnergy(getEuPerTick(getBaseUsePower()));
                if (getCoolDown() <= 0) {
                    setCoolDown(getCoolDownDefault());

                    ItemStack bookStack = inventory.getItem(11);
                    ItemStack inputStack = inventory.getItem(0);
                    if (bookStack.getItem() == Items.BOOK && inputStack.hasEnchantments()) {
                        Map<CompatEnchantment, Integer> enchantments = EnchantmentUtil.getEnchantment(inputStack, world);
                        if (bookStack.getCount() >= enchantments.size()) {
                            for (Map.Entry<CompatEnchantment, Integer> entry : enchantments.entrySet()) {
                                ItemStack itemStack = ItemStackUtil.create(Items.ENCHANTED_BOOK, 1);
                                Map<CompatEnchantment, Integer> map = new HashMap<>();
                                map.put(entry.getKey(), entry.getValue());

                                EnchantmentUtil.setEnchantment(itemStack, map, world);
                                ItemStackUtil.decrementCount(bookStack, 1);
                                insertStack(itemStack);
                            }
                            ItemStack newStack = inputStack.copy();

                            if (EnchantmentUtil.hasEnchantment(newStack))
                                EnchantmentUtil.setEnchantment(newStack, new HashMap<>(), world);

                            inventory.setItem(1, newStack);

                            inventory.setItem(0, ItemStack.EMPTY);
                        }
                    }

                    return;
                }
                setCoolDown(getCoolDown() - 1);
            } else {
                if (getCoolDown() != getCoolDownDefault()) {
                    setCoolDown(getCoolDownDefault());
                }
            }
        } else {
            block.setActive(false, world, getBlockPos());
        }
    }

    public void insertStack(ItemStack stack) {
        int[] indexes = {2, 3, 4, 5, 6, 7, 8, 9};

        for (int i : indexes) {
            ItemStack slotStack = inventory.getItem(i);
            if (slotStack.isEmpty()) {
                inventory.setItem(i, stack);
                BlockEntityUtil.markDirty(this);
                return;
            }
        }

        WorldUtil.spawnEntity(world, ItemEntityUtil.create(world, pos.getX(), pos.getY(), pos.getZ(), stack));
    }

    public Container getInventory() {
        return inventory;
    }
}

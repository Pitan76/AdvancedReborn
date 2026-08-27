package net.pitan76.advancedreborn.tile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.sound.CompatSoundCategory;
import net.pitan76.mcpitanlib.api.sound.CompatSoundEvents;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.screen.builder.SyncedObjectTypes;
import reborncore.common.util.RebornInventory;

public class RenamingMachineTile extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

    public Block toolDrop;
    public int energySlot;
    public RebornInventory<?> inventory;
    public int coolDownDefault = 150;
    public int coolDown = coolDownDefault;
    public String name = "";

    public RenamingMachineTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        toolDrop = Blocks.RENAMING_MACHINE.getOrNull();
        energySlot = 2;
        inventory = new RebornInventory<>(3, "RenamingMachineTile", 64, this);
        checkTier();
    }

    public RenamingMachineTile(BlockPos pos, BlockState state) {
        this(Tiles.RENAMING_MACHINE_TILE.getOrNull(), pos, state);
    }

    public RenamingMachineTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__renaming_machine").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this).slot(0, 55, 45).outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue()
            .sync(SyncedObjectTypes.COMPOUND_TAG, () -> {
                CompoundTag nbt = new CompoundTag();
                nbt.putString("name", getName() == null ? "" : getName());
                return nbt;
            }, (nbt) -> setName(nbt.getString("name").orElse(null))).sync(SyncedObjectTypes.INT, this::getCoolDown, this::setCoolDown).sync(SyncedObjectTypes.INT, this::getCoolDownDefault, this::setCoolDownDefault).addInventory().create(this, syncID);
    }



    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setNameClient(@Nullable String name) {
        setName(name);
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
        return AutoConfigAddon.getConfig().renamingMachineMaxEnergy;
    }

    public long getBaseMaxOutput() {
        return 0;
    }

    public long getBaseMaxInput() {
        return AutoConfigAddon.getConfig().renamingMachineMaxInput;
    }

    public long getBaseUsePower() {
        return AutoConfigAddon.getConfig().renamingMachineUseEnergy;
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

        //BlockState state = getWorld().getBlockState(getPos());
        BlockMachineBase block = (BlockMachineBase) state.getBlock();
        block.setActive(getCoolDown() != getCoolDownDefault(), world, getBlockPos());
        if (!getInventory().getItem(1).isEmpty()) {
            if (getCoolDown() <= 0) setCoolDown(getCoolDownDefault());
            return; // 出力スロットにアイテムがあれば停止
        }
        if (getEnergy() > getEuPerTick(getBaseUsePower())) {
            if (!getInventory().getItem(0).isEmpty()) {
                useEnergy(getEuPerTick(getBaseUsePower()));
                if (getCoolDown() <= 0) {
                    setCoolDown(getCoolDownDefault());
                    ItemStack stack = getItem(0).copy();
                    getInventory().setItem(0, ItemStack.EMPTY);
                    if (getName().isEmpty()) stack.remove(DataComponents.CUSTOM_NAME);
                    else stack.set(DataComponents.CUSTOM_NAME, TextUtil.literal(getName()));
                    getInventory().setItem(1, stack);
                    WorldUtil.playSound(world, null, getBlockPos(), CompatSoundEvents.BLOCK_ANVIL_USE, CompatSoundCategory.BLOCKS, 0.75F, 1.5F);
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

    public Container getInventory() {
        return inventory;
    }

    @Override
    public void saveAdditional(ValueOutput view) {
        if (getName() != null) view.putString("option_name", getName());
        view.putInt("option_time", coolDown);
        super.saveAdditional(view);
    }

    @Override
    public void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        setName(view.getStringOr("option_name", ""));
        coolDown = view.getIntOr("option_time", getCoolDownDefault());
    }
}

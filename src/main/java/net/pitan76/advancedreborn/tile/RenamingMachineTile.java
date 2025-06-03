package net.pitan76.advancedreborn.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.AdvancedReborn;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.Tiles;
import net.pitan76.advancedreborn.addons.autoconfig.AutoConfigAddon;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
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

    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder(AdvancedReborn.MOD_ID + "__renaming_machine").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this).slot(0, 55, 45).outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue()
            .sync(PacketCodecs.STRING, this::getName, this::setName).sync(PacketCodecs.INTEGER, this::getCoolDown, this::setCoolDown).sync(PacketCodecs.INTEGER, this::getCoolDownDefault, this::setCoolDownDefault).addInventory().create(this, syncID);
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

    public ItemStack getToolDrop(PlayerEntity p0) {
        return ItemStackUtil.create(toolDrop.asItem(), 1);
    }

    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        if (world == null || WorldUtil.isClient(world)) {
            return;
        }
        charge(energySlot);

        //BlockState state = getWorld().getBlockState(getPos());
        BlockMachineBase block = (BlockMachineBase) state.getBlock();
        block.setActive(getCoolDown() != getCoolDownDefault(), world, getPos());
        if (!getInventory().getStack(1).isEmpty()) {
            if (getCoolDown() <= 0) setCoolDown(getCoolDownDefault());
            return; // 出力スロットにアイテムがあれば停止
        }
        if (getEnergy() > getEuPerTick(getBaseUsePower())) {
            if (!getInventory().getStack(0).isEmpty()) {
                useEnergy(getEuPerTick(getBaseUsePower()));
                if (getCoolDown() <= 0) {
                    setCoolDown(getCoolDownDefault());
                    ItemStack stack = getStack(0).copy();
                    getInventory().setStack(0, ItemStack.EMPTY);
                    if (getName().isEmpty()) stack.remove(DataComponentTypes.CUSTOM_NAME);
                    else stack.set(DataComponentTypes.CUSTOM_NAME, TextUtil.literal(getName()));
                    getInventory().setStack(1, stack);
                    world.playSound(null, getPos(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 0.75F, 1.5F);
                    return;
                }
                setCoolDown(getCoolDown() - 1);
            } else {
                if (getCoolDown() != getCoolDownDefault()) {
                    setCoolDown(getCoolDownDefault());
                }
            }
        } else {
            block.setActive(false, world, getPos());
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (getName() != null) nbt.putString("option_name", getName());
        nbt.putInt("option_time", coolDown);
        super.writeNbt(nbt, registryLookup);
    }

    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (NbtUtil.has(nbt, "option_name")) setName(NbtUtil.getString(nbt, "option_name"));
        if (NbtUtil.has(nbt, "option_time")) coolDown = NbtUtil.getInt(nbt, "option_time");
    }
}

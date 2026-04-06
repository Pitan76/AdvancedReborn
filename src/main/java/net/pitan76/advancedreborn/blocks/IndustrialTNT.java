package net.pitan76.advancedreborn.blocks;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.pitan76.advancedreborn.entities.IndustrialTNTEntity;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.sound.CompatSoundCategory;
import net.pitan76.mcpitanlib.api.sound.CompatSoundEvents;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import net.pitan76.mcpitanlib.midohra.block.MCBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class IndustrialTNT extends TntBlock {

    public IndustrialTNT(CompatibleBlockSettings settings) {
        super(settings.build());
        DispenserBlock.registerBehavior(this, new DispenseItemBehavior() {
            public ItemStack dispense(BlockSource pointer, ItemStack stack) {
                DispenserBlockEntity blockEntity = pointer.blockEntity();
                Level world = blockEntity.getLevel();
                BlockPos pointerPos = blockEntity.getBlockPos();
                BlockPos blockPos = pointerPos.relative(Objects.requireNonNull(world).getBlockState(pointerPos).getValue(DispenserBlock.FACING));
                IndustrialTNTEntity tntEntity = new IndustrialTNTEntity(world, blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, null);
                WorldUtil.spawnEntity(world, tntEntity);

                WorldUtil.playSound(world, null, PosUtil.flooredBlockPos(tntEntity.getX(), tntEntity.getY(), tntEntity.getZ()), CompatSoundEvents.ENTITY_TNT_PRIMED, CompatSoundCategory.BLOCKS, 1.0F, 1.0F);
                ItemStackUtil.decrementCount(stack, 1);
                return stack;
            }
        });
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.is(state.getBlock())) {
            if (WorldUtil.isReceivingRedstonePower(world, pos)) {
                primeITnt(world, pos);
                WorldUtil.removeBlock(world, pos, false);
            }
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!WorldUtil.isReceivingRedstonePower(world, pos)) return;

        primeITnt(world, pos);
        WorldUtil.removeBlock(world, pos, false);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!WorldUtil.isClient(world) && !player.isCreative() && state.getValue(UNSTABLE)) {
            primeITnt(world, pos);
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void wasExploded(ServerLevel world, BlockPos pos, Explosion explosion) {
        if (WorldUtil.isClient(world)) return;

        IndustrialTNTEntity tntEntity = new IndustrialTNTEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, explosion.getIndirectSourceEntity());
        tntEntity.setFuse(WorldUtil.getRandom(world).nextInt(tntEntity.getFuse() / 4) + tntEntity.getFuse() / 8);
        WorldUtil.spawnEntity(world, tntEntity);
    }

    public static void primeITnt(Level world, BlockPos pos) {
        primeITnt(world, pos, null);
    }

    private static void primeITnt(Level world, BlockPos pos, @Nullable LivingEntity entity) {
        if (WorldUtil.isClient(world)) return;

        IndustrialTNTEntity tntEntity = new IndustrialTNTEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, entity);
        WorldUtil.spawnEntity(world, tntEntity);
        WorldUtil.playSound(world, null, PosUtil.flooredBlockPos(tntEntity.getX(), tntEntity.getY(), tntEntity.getZ()), CompatSoundEvents.ENTITY_TNT_PRIMED, CompatSoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        primeITnt(world, pos, player);
        WorldUtil.setBlockState(world, pos, MCBlocks.AIR.getDefaultState(), 11);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (WorldUtil.isClient(world)) return;

        Entity entity = projectile.getOwner();
        if (projectile.isOnFire()) {
            BlockPos blockPos = hit.getBlockPos();
            primeITnt(world, blockPos, entity instanceof LivingEntity ? (LivingEntity)entity : null);
            world.removeBlock(blockPos, false);
        }
    }
}

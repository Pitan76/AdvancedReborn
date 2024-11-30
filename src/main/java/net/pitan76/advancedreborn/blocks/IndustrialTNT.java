package net.pitan76.advancedreborn.blocks;

import net.minecraft.block.*;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.pitan76.advancedreborn.entities.IndustrialTNTEntity;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.sound.CompatSoundCategory;
import net.pitan76.mcpitanlib.api.sound.CompatSoundEvents;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class IndustrialTNT extends TntBlock {

    public IndustrialTNT(CompatibleBlockSettings settings) {
        super(settings.build());
        DispenserBlock.registerBehavior(this, new ItemDispenserBehavior() {
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                World world = pointer.blockEntity().getWorld();
                BlockPos pointerPos = pointer.blockEntity().getPos();
                BlockPos blockPos = pointer.blockEntity().getPos().offset(Objects.requireNonNull(world).getBlockState(pointerPos).get(DispenserBlock.FACING));
                IndustrialTNTEntity tntEntity = new IndustrialTNTEntity(world, blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, null);
                WorldUtil.spawnEntity(world, tntEntity);

                world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                stack.decrement(1);
                return stack;
            }
        });
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            if (world.isReceivingRedstonePower(pos)) {
                primeITnt(world, pos);
                world.removeBlock(pos, false);
            }
        }
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isReceivingRedstonePower(pos)) return;

        primeITnt(world, pos);
        world.removeBlock(pos, false);
    }

    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!WorldUtil.isClient(world) && !player.isCreative() && state.get(UNSTABLE)) {
            primeITnt(world, pos);
        }

        return super.onBreak(world, pos, state, player);
    }

    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (WorldUtil.isClient(world)) return;

        IndustrialTNTEntity tntEntity = new IndustrialTNTEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, explosion.getCausingEntity());
        tntEntity.setFuse(WorldUtil.getRandom(world).nextInt(tntEntity.getFuse() / 4) + tntEntity.getFuse() / 8);
        WorldUtil.spawnEntity(world, tntEntity);
    }

    public static void primeITnt(World world, BlockPos pos) {
        primeITnt(world, pos, null);
    }

    private static void primeITnt(World world, BlockPos pos, @Nullable LivingEntity entity) {
        if (WorldUtil.isClient(world)) return;

        IndustrialTNTEntity tntEntity = new IndustrialTNTEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, entity);
        WorldUtil.spawnEntity(world, tntEntity);
        WorldUtil.playSound(world, null, PosUtil.flooredBlockPos(tntEntity.getX(), tntEntity.getY(), tntEntity.getZ()), CompatSoundEvents.ENTITY_TNT_PRIMED, CompatSoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        primeITnt(world, pos, player);
        WorldUtil.setBlockState(world, pos, Blocks.AIR.getDefaultState(), 11);
        return ActionResult.success(WorldUtil.isClient(world));
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (WorldUtil.isClient(world)) return;

        Entity entity = projectile.getOwner();
        if (projectile.isOnFire()) {
            BlockPos blockPos = hit.getBlockPos();
            primeITnt(world, blockPos, entity instanceof LivingEntity ? (LivingEntity)entity : null);
            world.removeBlock(blockPos, false);
        }
    }
}

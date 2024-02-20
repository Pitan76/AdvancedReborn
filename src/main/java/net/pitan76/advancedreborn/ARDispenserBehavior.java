package net.pitan76.advancedreborn;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.entities.IndustrialTNTEntity;

import java.util.Objects;

public class ARDispenserBehavior {
    public static void init() {
        DispenserBlock.registerBehavior(Blocks.INDUSTRIAL_TNT, new ItemDispenserBehavior() {
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                World world = pointer.blockEntity().getWorld();
                BlockPos pointerPos = pointer.blockEntity().getPos();
                BlockPos blockPos = pointer.blockEntity().getPos().offset(Objects.requireNonNull(world).getBlockState(pointerPos).get(DispenserBlock.FACING));
                IndustrialTNTEntity tntEntity = new IndustrialTNTEntity(world, blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, null);
                world.spawnEntity(tntEntity);
                world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                stack.decrement(1);
                return stack;
            }
        });
    }
}

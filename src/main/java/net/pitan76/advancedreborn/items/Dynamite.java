package net.pitan76.advancedreborn.items;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.pitan76.advancedreborn.entities.DynamiteEntity;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.StackActionResult;

public class Dynamite extends ExtendItem {

    public boolean isSticky = false;
    public boolean isIndustrial = false;

    public Dynamite(CompatibleItemSettings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, new ProjectileDispenserBehavior() {
            public ProjectileEntity createProjectile(World world, Position pos, ItemStack stack) {
                DynamiteEntity dynamiteEntity = new DynamiteEntity(world, pos.getX(), pos.getY(), pos.getZ());
                dynamiteEntity.setItem(stack);
                dynamiteEntity.setSticky(isSticky);
                dynamiteEntity.setIndustrial(isIndustrial);
                return dynamiteEntity;
            }
        });
    }

    public Dynamite(CompatibleItemSettings settings, boolean isSticky) {
        this(settings);
        this.isSticky = isSticky;
    }

    public Dynamite(CompatibleItemSettings settings, boolean isSticky, boolean isIndustrial) {
        this(settings);
        this.isSticky = isSticky;
        this.isIndustrial = isIndustrial;
    }

    public StackActionResult onRightClick(ItemUseEvent e) {
        ItemStack stack = e.getStack();
        if (!e.user.isCreative()) stack.decrement(1);
        if (!e.world.isClient()) {
            DynamiteEntity dynamiteEntity = new DynamiteEntity(e.world, e.user.getEntity());
            dynamiteEntity.setVelocity(e.user.getPlayerEntity(), e.user.getPlayerEntity().getPitch(), e.user.getPlayerEntity().getYaw(), 0.0F, 1.5F, 1.0F);
            dynamiteEntity.setItem(stack);
            dynamiteEntity.setSticky(isSticky);
            dynamiteEntity.setIndustrial(isIndustrial);
            e.world.spawnEntity(dynamiteEntity);
            e.world.playSound(null, dynamiteEntity.getX(), dynamiteEntity.getY(), dynamiteEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        return e.success();
    }
}

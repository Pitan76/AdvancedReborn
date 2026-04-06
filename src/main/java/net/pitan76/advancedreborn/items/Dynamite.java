package net.pitan76.advancedreborn.items;

import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.pitan76.advancedreborn.entities.DynamiteEntity;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.sound.CompatSoundCategory;
import net.pitan76.mcpitanlib.api.sound.CompatSoundEvents;
import net.pitan76.mcpitanlib.api.util.StackActionResult;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;

public class Dynamite extends CompatItem implements ProjectileItem {

    public boolean isSticky = false;
    public boolean isIndustrial = false;

    public Dynamite(CompatibleItemSettings settings) {
        super(settings);
        DispenserBlock.registerProjectileBehavior(this);
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

    @Override
    public StackActionResult onRightClick(ItemUseEvent e) {
        ItemStack stack = e.user.getStackInHand(e.hand);
        if (e.isClient()) return e.success();

        if (!e.user.isCreative()) stack.shrink(1);

        DynamiteEntity dynamiteEntity = new DynamiteEntity(e.world, e.user.getEntity());
        dynamiteEntity.setVelocity(e.user.getPlayerEntity(), e.user.getPitch(), e.user.getYaw(), 0.0F, 1.5F, 1.0F);
        dynamiteEntity.callSetItem(stack);
        dynamiteEntity.setSticky(isSticky);
        dynamiteEntity.setIndustrial(isIndustrial);
        WorldUtil.spawnEntity(e.world, dynamiteEntity);
        BlockPos blockPos = PosUtil.flooredBlockPos(dynamiteEntity.getX(), dynamiteEntity.getY(), dynamiteEntity.getZ());
        WorldUtil.playSound(e.world, null, blockPos, CompatSoundEvents.ENTITY_TNT_PRIMED, CompatSoundCategory.BLOCKS, 1.0F, 1.0F);

        return e.success();
    }

    @Override
    public Projectile createEntity(Level world, Position pos, ItemStack stack, Direction direction) {
        DynamiteEntity dynamiteEntity = new DynamiteEntity(world, pos.getX(), pos.getY(), pos.getZ());
        dynamiteEntity.callSetItem(stack);
        dynamiteEntity.setSticky(isSticky);
        dynamiteEntity.setIndustrial(isIndustrial);
        return dynamiteEntity;
    }
}

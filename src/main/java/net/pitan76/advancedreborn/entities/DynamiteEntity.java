package net.pitan76.advancedreborn.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.pitan76.advancedreborn.Entities;
import net.pitan76.advancedreborn.Items;
import net.pitan76.mcpitanlib.api.entity.CompatThrownItemEntity;
import net.pitan76.mcpitanlib.api.event.entity.CollisionEvent;
import net.pitan76.mcpitanlib.api.event.entity.InitDataTrackerArgs;
import net.pitan76.mcpitanlib.api.util.EntityUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

public class DynamiteEntity extends CompatThrownItemEntity {

    public static EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(DynamiteEntity.class, EntityDataSerializers.INT);

    public boolean stopped = false;
    public boolean isSticky = false;
    public boolean isIndustrial = false;
    public static int fuseTimerInit = 60;
    public int fuseTimer = fuseTimerInit;

    public DynamiteEntity(EntityType<? extends ThrowableItemProjectile> entityType, ServerLevel world) {
        super(entityType, world);
        setFuse(fuseTimerInit);
    }

    public DynamiteEntity(ServerLevel world, LivingEntity owner) {
        super((EntityType<? extends ThrowableItemProjectile>) Entities.DYNAMITE.getOrNull(), owner, world);
        setFuse(fuseTimerInit);
    }

    public DynamiteEntity(ServerLevel world, double x, double y, double z) {
        super((EntityType<? extends ThrowableItemProjectile>) Entities.DYNAMITE.getOrNull(), x, y, z, world);
        setFuse(fuseTimerInit);
    }

    public DynamiteEntity(EntityType<DynamiteEntity> dynamiteEntityEntityType, Level level) {
        super(dynamiteEntityEntityType, level);
        setFuse(fuseTimerInit);
    }

    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }

    public void setIndustrial(boolean industrial) {
        isIndustrial = industrial;
    }

    @Override
    public Item getDefaultItemOverride() {
        return Items.DYNAMITE.getOrNull();
    }

    @Override
    public void initDataTracker(InitDataTrackerArgs args) {
        super.initDataTracker(args);
        args.add(FUSE, fuseTimerInit);
    }

    public void setFuse(int fuse) {
        entityData.set(FUSE, fuse);
        fuseTimer = fuse;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (FUSE.equals(data)) {
            fuseTimer = getFuse();
        }
    }

    public int getFuse() {
        return entityData.get(FUSE);
    }

    public int getFuseTimer() {
        return fuseTimer;
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        Vec3 distance = blockHitResult.getLocation().subtract(getX(), getY(), getZ());
        EntityUtil.setVelocity(this, distance);
        Vec3 pos = distance.normalize().scale(0.05000000074505806D);
        setPos(getX() - pos.x, getY() - pos.y, getZ() - pos.z);
        setOnGround(true);
        stopped = true;
    }

    @Override
    public void onCollision(CollisionEvent e) {
        super.onCollision(e);
        if (isSticky) {
            Vec3 distance = e.getPos().subtract(getX(), getY(), getZ());
            EntityUtil.setVelocity(this, distance);
            Vec3 pos = distance.normalize().scale(0.05000000074505806D);
            setPos(getX() - pos.x, getY() - pos.y, getZ() - pos.z);
            setOnGround(true);
            setNoGravity(true);
            stopped = true;
        }
    }

    public void tick() {
        super.tick();
        if (stopped) {
            fuseTimer--;
            if (fuseTimer <= 0) {
                if (level() instanceof ServerLevel)
                    kill((ServerLevel) level());
                if (!level().isClientSide()) {
                    explode();
                }
            } else {
                updateFluidInteraction();
            }
        }
        if (level().isClientSide()) {
            WorldUtil.addParticle(level(), ParticleTypes.FLAME, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    public void explode() {
        if (isIndustrial) {
            this.level()
                    .explode(
                            this,
                            null,
                            new IndustrialTNTEntity.IndustrialTNTExplosionBehavior(this),
                            this.getX(),
                            this.getY(0.0625),
                            this.getZ(),
                            2.5F,
                            false,
                            ServerLevel.ExplosionInteraction.BLOCK
                    );
            return;
        }
        level().explode(this, getX(), getY(0.0625D), getZ(), 4.0F, ServerLevel.ExplosionInteraction.TNT);
    }
}

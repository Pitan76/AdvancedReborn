package net.pitan76.advancedreborn.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.pitan76.advancedreborn.Entities;
import net.pitan76.advancedreborn.Items;
import net.pitan76.advancedreborn.entities.itnt.IndustrialExplosion;
import net.pitan76.mcpitanlib.api.entity.CompatThrownItemEntity;
import net.pitan76.mcpitanlib.api.event.entity.CollisionEvent;
import net.pitan76.mcpitanlib.api.event.entity.InitDataTrackerArgs;
import net.pitan76.mcpitanlib.api.sound.CompatSoundCategory;
import net.pitan76.mcpitanlib.api.sound.CompatSoundEvents;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

public class DynamiteEntity extends CompatThrownItemEntity {

    public static TrackedData<Integer> FUSE = DataTracker.registerData(DynamiteEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public boolean stopped = false;
    public boolean isSticky = false;
    public boolean isIndustrial = false;
    public static int fuseTimerInit = 60;
    public int fuseTimer = fuseTimerInit;

    public DynamiteEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        setFuse(fuseTimerInit);
    }

    public DynamiteEntity(World world, LivingEntity owner) {
        super((EntityType<? extends ThrownItemEntity>) Entities.DYNAMITE.getOrNull(), owner, world);
        setFuse(fuseTimerInit);
    }

    public DynamiteEntity(World world, double x, double y, double z) {
        super((EntityType<? extends ThrownItemEntity>) Entities.DYNAMITE.getOrNull(), x, y, z, world);
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
        //args.add(FUSE, fuseTimerInit);
        dataTracker.startTracking(FUSE, fuseTimerInit);
    }

    public void setFuse(int fuse) {
        dataTracker.set(FUSE, fuse);
        fuseTimer = fuse;
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (FUSE.equals(data)) {
            fuseTimer = getFuse();
        }
    }

    public int getFuse() {
        return dataTracker.get(FUSE);
    }

    public int getFuseTimer() {
        return fuseTimer;
    }

    public void onBlockHit(BlockHitResult blockHitResult) {
        Vec3d distance = blockHitResult.getPos().subtract(getX(), getY(), getZ());
        setVelocity(distance);
        Vec3d pos = distance.normalize().multiply(0.05000000074505806D);
        setPos(getX() - pos.x, getY() - pos.y, getZ() - pos.z);
        setOnGround(true);
        stopped = true;
    }

    @Override
    public void onCollision(CollisionEvent e) {
        super.onCollision(e);
        if (isSticky) {
            Vec3d distance = e.getPos().subtract(getX(), getY(), getZ());
            setVelocity(distance);
            Vec3d pos = distance.normalize().multiply(0.05000000074505806D);
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
                kill();
                if (!getEntityWorld().isClient()) {
                    explode();
                }
            } else {
                updateWaterState();
            }
        }
        if (getEntityWorld().isClient()) {
            WorldUtil.addParticle(getEntityWorld(), ParticleTypes.FLAME, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    public void explode() {
        if (isIndustrial) {
            Explosion explosion = new IndustrialExplosion(getEntityWorld(), this, null, null, getX(), getBodyY(0.0625D), getZ(),2.5F,false, Explosion.DestructionType.DESTROY);
            explosion.collectBlocksAndDamageEntities();
            explosion.affectWorld(true);
            WorldUtil.playSound(getEntityWorld(), null, getBlockPos(), CompatSoundEvents.ENTITY_GENERIC_EXPLODE, CompatSoundCategory.BLOCKS, 4.0F, (1.0F + (getEntityWorld().random.nextFloat() - getEntityWorld().random.nextFloat()) * 0.2F) * 0.7F);
            ((ServerWorld)getEntityWorld()).spawnParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
            return;
        }
        getEntityWorld().createExplosion(this, getX(), getBodyY(0.0625D), getZ(), 4.0F, World.ExplosionSourceType.TNT);
    }
}

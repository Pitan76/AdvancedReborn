package net.pitan76.advancedreborn.entities;

import net.minecraft.entity.*;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import org.jetbrains.annotations.Nullable;

public class IndustrialTNTEntity extends TntEntity {
    public IndustrialTNTEntity(EntityType<? extends IndustrialTNTEntity> entityType, World world) {
        super(entityType, world);
    }

    public IndustrialTNTEntity(World world, double x, double y, double z, @Nullable LivingEntity entity) {
        super(world, x, y, z, entity);
    }

    /*
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return super.createSpawnPacket(); //EntitySpawnPacket.create(this, Defines.SPAWN_PACKET_ID);
    }

     */

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        return super.createSpawnPacket(entityTrackerEntry);
    }

    public void tick() {
        if (!hasNoGravity()) {
            setVelocity(getVelocity().add(0.0D, -0.04D, 0.0D));
        }

        move(MovementType.SELF, this.getVelocity());
        setVelocity(this.getVelocity().multiply(0.98D));
        if (isOnGround()) {
            setVelocity(this.getVelocity().multiply(0.7D, -0.5D, 0.7D));
        }
        setFuse(getFuse() - 1);
        if (getFuse() <= 0) {
            if (getEntityWorld() instanceof ServerWorld)
                kill((ServerWorld) getEntityWorld());
            if (!getEntityWorld().isClient()) {
                iExplode();
            }
        } else {
            updateWaterState();
            if (getEntityWorld().isClient()) {
                WorldUtil.addParticle(getEntityWorld(), ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    public void iExplode() {
        this.getEntityWorld()
                .createExplosion(
                        this,
                        null,
                        new IndustrialTNTExplosionBehavior(this),
                        this.getX(),
                        this.getBodyY(0.0625),
                        this.getZ(),
                        2.5F,
                        false,
                        World.ExplosionSourceType.BLOCK
                );
    }

    public static class IndustrialTNTExplosionBehavior extends EntityExplosionBehavior {

        public IndustrialTNTExplosionBehavior(Entity entity) {
            super(entity);
        }

        @Override
        public boolean shouldDamage(Explosion explosion, Entity entity) {
            return false;
        }
    }
}

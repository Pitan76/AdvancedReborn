package net.pitan76.advancedreborn.entities;

import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.pitan76.mcpitanlib.api.util.EntityUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.particle.CompatParticleTypes;
import org.jetbrains.annotations.Nullable;

public class IndustrialTNTEntity extends PrimedTnt {
    public IndustrialTNTEntity(EntityType<? extends IndustrialTNTEntity> entityType, ServerLevel world) {
        super(entityType, world);
    }

    public IndustrialTNTEntity(Level world, double x, double y, double z, @Nullable LivingEntity entity) {
        super(world, x, y, z, entity);
    }

    /*
    public Packet<ClientPlayPacketListener> getAddEntityPacket() {
        return super.getAddEntityPacket(); //EntitySpawnPacket.create(this, Defines.SPAWN_PACKET_ID);
    }

     */

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entityTrackerEntry) {
        return super.getAddEntityPacket(entityTrackerEntry);
    }

    public void tick() {
        if (!EntityUtil.hasNoGravity(this)) {
            EntityUtil.setVelocity(this, EntityUtil.getVelocity(this).add(0.0D, -0.04D, 0.0D));
        }

        move(MoverType.SELF, EntityUtil.getVelocity(this));
        EntityUtil.setVelocity(this, EntityUtil.getVelocity(this).scale(0.98D));
        if (EntityUtil.isOnGround(this)) {
            EntityUtil.setVelocity(this, EntityUtil.getVelocity(this).multiply(0.7D, -0.5D, 0.7D));
        }
        setFuse(getFuse() - 1);

        Level world = EntityUtil.getWorld(this);

        if (getFuse() <= 0) {
            if (world instanceof ServerLevel)
                kill((ServerLevel) world);
            if (!WorldUtil.isClient(world)) {
                iExplode();
            }
        } else {
            updateFluidInteraction();
            if (WorldUtil.isClient(world)) {
                WorldUtil.addParticle(world, CompatParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    public void iExplode() {
        this.level()
                .explode(
                        this,
                        null,
                        new IndustrialTNTExplosionBehavior(this),
                        this.getX(),
                        this.getY(0.0625),
                        this.getZ(),
                        2.5F,
                        false,
                        Level.ExplosionInteraction.BLOCK
                );
    }

    public static class IndustrialTNTExplosionBehavior extends EntityBasedExplosionDamageCalculator {

        public IndustrialTNTExplosionBehavior(Entity entity) {
            super(entity);
        }

        @Override
        public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
            return false;
        }
    }
}

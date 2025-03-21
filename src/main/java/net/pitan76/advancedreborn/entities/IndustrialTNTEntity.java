package net.pitan76.advancedreborn.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.TntEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.pitan76.advancedreborn.entities.itnt.IndustrialExplosion;
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
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return super.createSpawnPacket();
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
            kill();
            if (!getEntityWorld().isClient) {
                iExplode();
            }
        } else {
            updateWaterState();
            if (getEntityWorld().isClient) {
                getEntityWorld().addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    public void iExplode() {
        Explosion explosion = new IndustrialExplosion(getEntityWorld(), this, null, null, getX(), getBodyY(0.0625D), getZ(),2.5F,false, Explosion.DestructionType.DESTROY);
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(true);
        WorldUtil.playSound(getEntityWorld(), null, getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (getEntityWorld().random.nextFloat() - getEntityWorld().random.nextFloat()) * 0.2F) * 0.7F);
        ((ServerWorld)getEntityWorld()).spawnParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
    }
}

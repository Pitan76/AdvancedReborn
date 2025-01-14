package net.pitan76.advancedreborn.entities.itnt;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.pitan76.advancedreborn.mixins.ExplosionAccessor;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public class IndustrialExplosion extends Explosion {
    public IndustrialExplosion(World world, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, DestructionType destructionType) {
        super(world, entity, damageSource, behavior, x, y, z, power, createFire, destructionType, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE);
    }

    public World world;
    public ExplosionBehavior behavior;

    @Override
    public void collectBlocksAndDamageEntities() {
        ExplosionAccessor thisAccessor = (ExplosionAccessor) this;
        world = thisAccessor.getWorld();
        behavior = thisAccessor.getBehavior();
        Set<BlockPos> set = Sets.newHashSet();
        int k;
        int l;
        for(int j = 0; j < 16; ++j) {
            for(k = 0; k < 16; ++k) {
                for(l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d = (j / 15.0F * 2.0F - 1.0F);
                        double e = (k / 15.0F * 2.0F - 1.0F);
                        double f = (l / 15.0F * 2.0F - 1.0F);
                        double g = Math.sqrt(d * d + e * e + f * f);
                        d /= g;
                        e /= g;
                        f /= g;
                        float h = thisAccessor.getPower() * (0.7F + WorldUtil.getRandom(world).nextFloat() * 0.6F);
                        double m = thisAccessor.getX();
                        double n = thisAccessor.getY();
                        double o = thisAccessor.getZ();

                        for(float var21 = 0.3F; h > 0.0F; h -= 0.22500001F) {
                            BlockPos blockPos = PosUtil.flooredBlockPos(m, n, o);
                            BlockState blockState = WorldUtil.getBlockState(world, blockPos);
                            FluidState fluidState = WorldUtil.getFluidState(world, blockPos);
                            Optional<Float> optional = this.behavior.getBlastResistance(this, world, blockPos, blockState, fluidState);
                            if (optional.isPresent()) {
                                h -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if (h > 0.0F && behavior.canDestroyBlock(this, world, blockPos, blockState, h)) {
                                set.add(blockPos);
                            }

                            m += d * 0.30000001192092896D;
                            n += e * 0.30000001192092896D;
                            o += f * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        getAffectedBlocks().addAll(set);
    }
}

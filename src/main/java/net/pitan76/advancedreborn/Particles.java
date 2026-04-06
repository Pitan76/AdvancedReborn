package net.pitan76.advancedreborn;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.ParticleType;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;

import static net.pitan76.advancedreborn.AdvancedReborn.INSTANCE;
import static net.pitan76.advancedreborn.AdvancedReborn.registry;

public class Particles {
    public static RegistryResult<ParticleType<?>> ENERGY;

    public static void init() {
        ENERGY = registry.registerParticleType(INSTANCE.compatId("energy"), FabricParticleTypes::simple);
    }
}

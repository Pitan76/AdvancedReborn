package net.pitan76.advancedreborn;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.pitan76.advancedreborn.entities.DynamiteEntity;
import net.pitan76.advancedreborn.entities.IndustrialTNTEntity;
import net.pitan76.mcpitanlib.api.entity.EntityTypeBuilder;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;

import static net.pitan76.advancedreborn.AdvancedReborn.INSTANCE;
import static net.pitan76.advancedreborn.AdvancedReborn.registry;

public class Entities {
    public static RegistryResult<EntityType<?>> DYNAMITE;
    public static RegistryResult<EntityType<?>> I_TNT;

    public static void init() {
        DYNAMITE = registry.registerEntity(INSTANCE.compatId("dynamite"),  () -> (EntityTypeBuilder.<DynamiteEntity>create(MobCategory.MISC, DynamiteEntity::new)).setDimensions(EntityDimensions.fixed(0.25f,0.25f)).setMaxTrackDistance(10).setTrackTickInterval(10).build());
        I_TNT = registry.registerEntity(INSTANCE.compatId("industrial_tnt"), () -> (EntityTypeBuilder.<IndustrialTNTEntity>create(MobCategory.MISC, IndustrialTNTEntity::new)).setDimensions(EntityDimensions.fixed(0.98f,0.98f)).setMaxTrackDistance(10).setTrackTickInterval(10).build());
    }
}

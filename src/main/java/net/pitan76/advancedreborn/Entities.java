package net.pitan76.advancedreborn;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import net.pitan76.advancedreborn.entities.DynamiteEntity;
import net.pitan76.advancedreborn.entities.IndustrialTNTEntity;

public class Entities {
    public static EntityType<DynamiteEntity> DYNAMITE = Registry.register(Registry.ENTITY_TYPE, AdvancedReborn._id("dynamite").toMinecraft(), (FabricEntityTypeBuilder.<DynamiteEntity>create(SpawnGroup.MISC, DynamiteEntity::new)).dimensions(EntityDimensions.fixed(0.25f,0.25f)).trackRangeBlocks(10).trackedUpdateRate(10).build());
    public static EntityType<IndustrialTNTEntity> I_TNT = Registry.register(Registry.ENTITY_TYPE, AdvancedReborn._id("industrial_tnt").toMinecraft(), (FabricEntityTypeBuilder.<IndustrialTNTEntity>create(SpawnGroup.MISC, IndustrialTNTEntity::new)).dimensions(EntityDimensions.fixed(0.98f,0.98f)).trackRangeBlocks(10).trackedUpdateRate(10).build());

    public static void init() {

    }
}

package net.pitan76.advancedreborn;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.EmotionParticle;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.DefaultParticleType;
import net.pitan76.advancedreborn.entities.IndustrialTNTEntity;
import net.pitan76.advancedreborn.renderer.IndustrialTNTEntityRenderer;
import net.pitan76.advancedreborn.screen.CardboardBoxScreen;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.mcpitanlib.api.client.registry.EntityRendererRegistry;

import static net.pitan76.advancedreborn.AdvancedReborn.INSTANCE;

public class AdvancedRebornClient implements ClientModInitializer {

    public static MinecraftClient client = MinecraftClient.getInstance();
    public static boolean isCopiedIconMap = false;

    public void onInitializeClient() {
        CompatRegistryClient.registryClientSpriteAtlasTexture(INSTANCE.id("particle/energy"));
        INSTANCE.info("Registered particle texture");

        CompatRegistryClient.registerParticle((DefaultParticleType) Particles.ENERGY.getOrNull(), EmotionParticle.HeartFactory::new);
        INSTANCE.info("Registered particle type");

        EntityRendererRegistry.registerEntityRendererAsFlyingItem(() -> Entities.DYNAMITE.getOrNull());
        INSTANCE.info("Registered entity renderer");

        CompatRegistryClient.registerEntityRenderer(() -> (EntityType<IndustrialTNTEntity>) Entities.I_TNT.getOrNull(), IndustrialTNTEntityRenderer::new);
        INSTANCE.info("Registered entity renderer");

        CompatRegistryClient.registerScreen(AdvancedReborn.MOD_ID, ScreenHandlers.CARDBOARD_BOX_SCREEN_HANDLER.getOrNull(), CardboardBoxScreen::new);
        INSTANCE.info("Registered screen");

        /*
        ClientNetworking.registerReceiver(Defines.SPAWN_PACKET_ID.toMinecraft(), (client, player, buf) -> {
            EntityType<?> et = Registries.ENTITY_TYPE.get(buf.readVarInt());
            UUID uuid = buf.readUuid();
            int entityId = buf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(buf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(buf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(buf);
            if (client.world == null) return;
            Entity entity = et.create(client.world);
            if (entity == null) return;
            entity.updateTrackedPosition(pos.x, pos.y, pos.z);
            entity.setPos(pos.x, pos.y, pos.z);
            entity.setYaw(pitch);
            entity.setYaw(yaw);
            entity.setId(entityId);
            entity.setUuid(uuid);
            client.world.addEntity(entity);
        });

         */
    }
}
package net.pitan76.advancedreborn;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.EmotionParticle;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.pitan76.advancedreborn.packet.EntitySpawnPacket;
import net.pitan76.advancedreborn.renderer.IndustrialTNTEntityRenderer;
import net.pitan76.advancedreborn.screen.CardboardBoxScreen;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.mcpitanlib.api.network.ClientNetworking;

import java.util.UUID;

public class AdvancedRebornClient implements ClientModInitializer {

    public static MinecraftClient client = MinecraftClient.getInstance();

    public void onInitializeClient() {
        GuiTypes.init();
        CompatRegistryClient.registryClientSpriteAtlasTexture(AdvancedReborn.id("particle/energy"));

        CompatRegistryClient.registerParticle(Particles.ENERGY, EmotionParticle.HeartFactory::new);

        EntityRendererRegistry.INSTANCE.register(Entities.DYNAMITE, (manager, context) -> new FlyingItemEntityRenderer<>(manager, context.getItemRenderer()));
        CompatRegistryClient.registerEntityRenderer(() -> Entities.I_TNT, IndustrialTNTEntityRenderer::new);

        CompatRegistryClient.registerScreen(ScreenHandlers.CARDBOARD_BOX_SCREEN_HANDLER, CardboardBoxScreen::new);

        ClientNetworking.registerReceiver(Defines.SPAWN_PACKET_ID, (client, player, byteBuf) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            if (client.world == null) return;
            Entity entity = et.create(client.world);
            if (entity == null) return;
            entity.updateTrackedPosition(pos.x, pos.y, pos.z);
            entity.setPos(pos.x, pos.y, pos.z);
            entity.pitch = pitch;
            entity.yaw = yaw;
            entity.setEntityId(entityId);
            entity.setUuid(uuid);
            client.world.addEntity(entityId, entity);
        });
    }
}
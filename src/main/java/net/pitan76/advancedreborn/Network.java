package net.pitan76.advancedreborn;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.pitan76.advancedreborn.tile.CardboardBoxTile;
import net.pitan76.advancedreborn.tile.RenamingMachineTile;
import net.pitan76.mcpitanlib.api.network.v2.ServerNetworking;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;

public class Network {
    public static void init() {
        ServerNetworking.registerReceiver(Defines.CARDBOARD_BOX_CLOSE_PACKET_ID, (e) -> {
            PacketByteBuf buf = e.getBuf();
            NbtCompound data = buf.readNbt();
            e.server.execute(() -> {
                if (data == null) return;
                if (!data.contains("x")) return;
                if (!data.contains("y")) return;
                if (!data.contains("z")) return;
                if (!data.contains("note")) return;
                BlockEntity blockEntity = e.player.getWorld().getBlockEntity(PosUtil.flooredBlockPos(data.getDouble("x"), data.getDouble("y"), data.getDouble("z")));
                if (!(blockEntity instanceof CardboardBoxTile)) return;
                CardboardBoxTile tile = (CardboardBoxTile) blockEntity;
                tile.setNote(data.getString("note"));
            });
        });
        ServerNetworking.registerReceiver(Defines.RENAMING_PACKET_ID, (e) -> {
            PacketByteBuf buf = e.getBuf();
            NbtCompound data = buf.readNbt();
            e.server.execute(() -> {
                if (data == null) return;
                if (!data.contains("x")) return;
                if (!data.contains("y")) return;
                if (!data.contains("z")) return;
                if (!data.contains("name")) return;
                BlockEntity blockEntity = e.player.getWorld().getBlockEntity(PosUtil.flooredBlockPos(data.getDouble("x"), data.getDouble("y"), data.getDouble("z")));
                if (!(blockEntity instanceof RenamingMachineTile)) return;
                RenamingMachineTile tile = (RenamingMachineTile) blockEntity;
                tile.setName(data.getString("name"));
            });
        });
    }
}

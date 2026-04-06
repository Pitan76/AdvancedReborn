package net.pitan76.advancedreborn;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.pitan76.advancedreborn.tile.CardboardBoxTile;
import net.pitan76.advancedreborn.tile.RenamingMachineTile;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.ServerNetworking;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;

public class Network {
    public static void init() {
        ServerNetworking.registerReceiver(Defines.CARDBOARD_BOX_CLOSE_PACKET_ID.toMinecraft(), (server, player, buf) -> {
            CompoundTag data = PacketByteUtil.readNbt(buf);
            server.execute(() -> {
                if (data == null) return;
                if (!NbtUtil.has(data, "x")) return;
                if (!NbtUtil.has(data, "y")) return;
                if (!NbtUtil.has(data, "z")) return;
                if (!NbtUtil.has(data, "note")) return;
                BlockEntity blockEntity = WorldUtil.getBlockEntity(player.getEntityWorld(), PosUtil.flooredBlockPos(NbtUtil.getDouble(data, "x"), NbtUtil.getDouble(data, "y"), NbtUtil.getDouble(data, "z")));
                if (!(blockEntity instanceof CardboardBoxTile)) return;

                CardboardBoxTile tile = (CardboardBoxTile) blockEntity;
                tile.setNote(NbtUtil.getString(data, "note"));
            });
        });
        ServerNetworking.registerReceiver(Defines.RENAMING_PACKET_ID.toMinecraft(), (server, player, buf) -> {
            CompoundTag data = PacketByteUtil.readNbt(buf);
            server.execute(() -> {
                if (data == null) return;
                if (!NbtUtil.has(data, "x")) return;
                if (!NbtUtil.has(data, "y")) return;
                if (!NbtUtil.has(data, "z")) return;
                if (!NbtUtil.has(data, "name")) return;
                BlockEntity blockEntity = WorldUtil.getBlockEntity(player.getEntityWorld(), PosUtil.flooredBlockPos(NbtUtil.getDouble(data, "x"), NbtUtil.getDouble(data, "y"), NbtUtil.getDouble(data, "z")));
                if (!(blockEntity instanceof RenamingMachineTile)) return;

                RenamingMachineTile tile = (RenamingMachineTile) blockEntity;
                tile.setName(NbtUtil.getString(data, "name"));
            });
        });
    }
}

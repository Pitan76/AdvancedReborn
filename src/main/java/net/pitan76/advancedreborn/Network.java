package net.pitan76.advancedreborn;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.pitan76.advancedreborn.tile.CardboardBoxTile;
import net.pitan76.advancedreborn.tile.RenamingMachineTile;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.ServerNetworking;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;

public class Network {
    public static void init() {
        ServerNetworking.registerReceiver(Defines.CARDBOARD_BOX_CLOSE_PACKET_ID.toMinecraft(), (server, player, buf) -> {
            NbtCompound data = PacketByteUtil.readNbt(buf);
            server.execute(() -> {
                if (data == null) return;
                if (!data.contains("x")) return;
                if (!data.contains("y")) return;
                if (!data.contains("z")) return;
                if (!data.contains("note")) return;
                BlockEntity blockEntity = WorldUtil.getBlockEntity(player.getWorld(), PosUtil.flooredBlockPos(data.getDouble("x"), data.getDouble("y"), data.getDouble("z")));
                if (!(blockEntity instanceof CardboardBoxTile)) return;

                CardboardBoxTile tile = (CardboardBoxTile) blockEntity;
                tile.setNote(data.getString("note"));
            });
        });
        ServerNetworking.registerReceiver(Defines.RENAMING_PACKET_ID.toMinecraft(), (server, player, buf) -> {
            NbtCompound data = PacketByteUtil.readNbt(buf);
            server.execute(() -> {
                if (data == null) return;
                if (!data.contains("x")) return;
                if (!data.contains("y")) return;
                if (!data.contains("z")) return;
                if (!data.contains("name")) return;
                BlockEntity blockEntity = WorldUtil.getBlockEntity(player.getWorld(), PosUtil.flooredBlockPos(data.getDouble("x"), data.getDouble("y"), data.getDouble("z")));
                if (!(blockEntity instanceof RenamingMachineTile)) return;

                RenamingMachineTile tile = (RenamingMachineTile) blockEntity;
                tile.setName(data.getString("name"));
            });
        });
    }
}

package net.pitan76.advancedreborn.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class EntitySpawnPacket {

    /*
    public static Packet<ClientCommonPacketListener> create(Entity entity, Identifier packetID) {
        if (entity.getEntityWorld().isClient)
            throw new IllegalStateException("SpawnPacketUtil.create called on the logical client!");
        PacketByteBuf byteBuf = PacketByteUtil.create();
        byteBuf.writeVarInt(Registries.ENTITY_TYPE.getRawId(entity.getType()));
        byteBuf.writeUuid(entity.getUuid());
        byteBuf.writeVarInt(entity.getId());

        PacketBufUtil.writeVec3d(byteBuf, entity.getPos());
        PacketBufUtil.writeAngle(byteBuf, entity.getPitch());
        PacketBufUtil.writeAngle(byteBuf, entity.getYaw());



        return ServerPlayNetworking.createS2CPacket(packetID, byteBuf);
    }

     */

    public static final class PacketBufUtil {

        /**
         * Packs a floating-point angle into a {@code byte}.
         *
         * @param angle
         *         angle
         * @return packed angle
         */
        public static byte packAngle(float angle) {
            return (byte) Mth.floor(angle * 256 / 360);
        }

        /**
         * Unpacks a floating-point angle from a {@code byte}.
         *
         * @param angleByte
         *         packed angle
         * @return angle
         */
        public static float unpackAngle(byte angleByte) {
            return (angleByte * 360) / 256f;
        }

        /**
         * Writes an angle to a {@link PacketByteBuf}.
         *
         * @param byteBuf
         *         destination buffer
         * @param angle
         *         angle
         */
        public static void writeAngle(FriendlyByteBuf byteBuf, float angle) {
            byteBuf.writeByte(packAngle(angle));
        }

        /**
         * Reads an angle from a {@link PacketByteBuf}.
         *
         * @param byteBuf
         *         source buffer
         * @return angle
         */
        public static float readAngle(FriendlyByteBuf byteBuf) {
            return unpackAngle(byteBuf.readByte());
        }

        /**
         * Writes a {@link Vec3d} to a {@link PacketByteBuf}.
         *
         * @param byteBuf
         *         destination buffer
         * @param vec3d
         *         vector
         */
        public static void writeVec3d(FriendlyByteBuf byteBuf, Vec3 vec3d) {
            byteBuf.writeDouble(vec3d.x);
            byteBuf.writeDouble(vec3d.y);
            byteBuf.writeDouble(vec3d.z);
        }

        /**
         * Reads a {@link Vec3d} from a {@link PacketByteBuf}.
         *
         * @param byteBuf
         *         source buffer
         * @return vector
         */
        public static Vec3 readVec3d(FriendlyByteBuf byteBuf) {
            double x = byteBuf.readDouble();
            double y = byteBuf.readDouble();
            double z = byteBuf.readDouble();
            return new Vec3d(x, y, z);
        }
    }


}


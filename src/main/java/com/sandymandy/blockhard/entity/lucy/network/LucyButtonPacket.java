package com.sandymandy.blockhard.entity.lucy.network;

import com.sandymandy.blockhard.BlockHard;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;

public record LucyButtonPacket(int entityId, String actionId) implements CustomPayload {

    public static final Id<LucyButtonPacket> ID = new Id<>(Identifier.of(BlockHard.MOD_ID, "lucy_button"));

    public static final PacketCodec<RegistryByteBuf, LucyButtonPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, LucyButtonPacket::entityId,
                    PacketCodecs.STRING, LucyButtonPacket::actionId,
                    LucyButtonPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

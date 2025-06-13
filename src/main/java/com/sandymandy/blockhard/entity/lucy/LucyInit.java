package com.sandymandy.blockhard.entity.lucy;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.lucy.network.LucyButtonPacket;
import com.sandymandy.blockhard.entity.lucy.screen.LucyScreenHandler;
import com.sandymandy.blockhard.scene.SceneStateManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class LucyInit {
    public static final Identifier LUCY_ID = Identifier.of(BlockHard.MOD_ID, "lucy");

    public static final EntityType<LucyEntity> LUCY = Registry.register(
            Registries.ENTITY_TYPE,
            LUCY_ID,
            EntityType.Builder.create(LucyEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.5f, 1.85f)
                    .build()
    );

    public static final ExtendedScreenHandlerType<LucyScreenHandler, LucyScreenData> LUCY_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    Identifier.of(BlockHard.MOD_ID, "lucy_screen"),
                    new ExtendedScreenHandlerType<>(LucyScreenHandler::new, LucyScreenData.PACKET_CODEC)
            );

    public record LucyScreenData(int entityId) {
        public static final PacketCodec<RegistryByteBuf, LucyScreenData> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT,
                LucyScreenData::entityId,
                LucyScreenData::new
        );
    }

    public static void init() {


        // Attributes
        FabricDefaultAttributeRegistry.register(LUCY, LucyEntity.createAttributes());

        // Networking
        PayloadTypeRegistry.playC2S().register(LucyButtonPacket.ID, LucyButtonPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(
                LucyButtonPacket.ID,
                (packet, context) -> context.player().getServer().execute(() -> {
                    var entity = context.player().getWorld().getEntityById(packet.entityId());
                    if (entity instanceof LucyEntity lucy) {
                        switch (packet.actionId()) {
                            case "breakUp" -> lucy.disown(context.player());
                            case "setBase" -> lucy.setBasePosHere();
                            case "startScene" -> SceneStateManager.startScene(context.player(), lucy);
                            case "goToBase" -> lucy.teleportToBase();
                            default -> BlockHard.LOGGER.warn("Unknown Lucy action: " + packet.actionId());
                        }
                    }
                })
        );

    }
}

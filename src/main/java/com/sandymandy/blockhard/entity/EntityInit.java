package com.sandymandy.blockhard.entity;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.bia.BiaEntity;
import com.sandymandy.blockhard.entity.lucy.LucyEntity;
import com.sandymandy.blockhard.util.entity.AbstractGirlEntity;
import com.sandymandy.blockhard.scene.SceneStateManager;
import com.sandymandy.blockhard.util.network.ButtonPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class EntityInit {
    public static final Identifier LUCY_ID = Identifier.of(BlockHard.MOD_ID, "lucy");
    public static final Identifier BIA_ID = Identifier.of(BlockHard.MOD_ID, "bia");

    public static final EntityType<LucyEntity> LUCY = Registry.register(
            Registries.ENTITY_TYPE,
            LUCY_ID,
            EntityType.Builder.create(LucyEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.5f, 1.85f)
                    .build()
    );

    public static final EntityType<BiaEntity> BIA = Registry.register(
            Registries.ENTITY_TYPE,
            BIA_ID,
            EntityType.Builder.create(BiaEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.5f, 1.75f)
                    .build()
    );

    public static void init() {
        // Attributes
        FabricDefaultAttributeRegistry.register(LUCY, LucyEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(BIA, BiaEntity.createAttributes());

        // Network
        PayloadTypeRegistry.playC2S().register(ButtonPacket.ID, ButtonPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(
            ButtonPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                    var entity = context.player().getWorld().getEntityById(packet.entityId());
                    if (entity instanceof AbstractGirlEntity girl) {
                        switch (packet.actionId()) {
                            case "stripOrDressup" -> girl.setStripped(!girl.isStripped());
                            case "breakUp" -> girl.breakUp(context.player());
                            case "setBase" -> girl.setBasePosHere();
                            case "startScene" -> SceneStateManager.startScene(context.player(), girl);
                            case "goToBase" -> girl.teleportToBase();
                            case "sit" -> girl.setSit(!girl.isSittingdown());
                            case "follow" -> girl.setFollowing(!girl.isFollowing());
                            case "stop" -> girl.setMovementLocked(!girl.isMovementLocked());
                            default -> BlockHard.LOGGER.warn("Unknown Girl interaction: " + packet.actionId());
                        }
                    }
                })
        );

    }
}

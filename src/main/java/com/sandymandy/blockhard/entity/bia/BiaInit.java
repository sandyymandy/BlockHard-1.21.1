package com.sandymandy.blockhard.entity.bia;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.util.entity.AbstractGirlEntity;
import com.sandymandy.blockhard.scene.SceneStateManager;
import com.sandymandy.blockhard.util.network.ButtonPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class BiaInit {
    public static final Identifier BIA_ID = Identifier.of(BlockHard.MOD_ID, "bia");

    public static final EntityType<BiaEntity> BIA = Registry.register(
            Registries.ENTITY_TYPE,
            BIA_ID,
            EntityType.Builder.create(BiaEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.5f, 1.5f)
                    .build()
    );

    public static void init() {


        // Attributes
        FabricDefaultAttributeRegistry.register(BIA, BiaEntity.createAttributes());

        ServerPlayNetworking.registerGlobalReceiver(
                ButtonPacket.ID,
                (packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
                    var entity = context.player().getWorld().getEntityById(packet.entityId());
                    if (entity instanceof AbstractGirlEntity bia) {
                        switch (packet.actionId()) {
                            case "stripOrDressup" -> bia.setStripped(!bia.isStripped());
                            case "breakUp" -> bia.breakUp(context.player());
                            case "setBase" -> bia.setBasePosHere();
                            case "startScene" -> SceneStateManager.startScene(context.player(), bia);
                            case "goToBase" -> bia.teleportToBase();
                            default -> BlockHard.LOGGER.warn("Unknown Bia action: " + packet.actionId());
                        }
                    }
                })
        );

    }
}

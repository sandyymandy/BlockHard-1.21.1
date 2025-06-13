package com.sandymandy.blockhard;

import com.sandymandy.blockhard.entity.lucy.LucyEntity;
import com.sandymandy.blockhard.entity.lucy.network.LucyButtonPacket;
import com.sandymandy.blockhard.item.ModItemGroups;
import com.sandymandy.blockhard.item.ModItems;
import com.sandymandy.blockhard.entity.lucy.screen.LucyScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockHard implements ModInitializer {
	public static final String MOD_ID = "blockhard";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Initialize Entities
	public static final EntityType<LucyEntity> LUCY = Registry.register(Registries.ENTITY_TYPE,
			Identifier.of(BlockHard.MOD_ID, "lucy"), EntityType.Builder.create(LucyEntity::new, SpawnGroup.CREATURE).dimensions(.5f, 1.85f).build());

	// Initialize ScreenHandlers
	public static final ExtendedScreenHandlerType<LucyScreenHandler, LucyScreenData> LUCY_SCREEN_HANDLER =
			Registry.register(
					Registries.SCREEN_HANDLER,
					Identifier.of(BlockHard.MOD_ID, "lucy_screen"),
					new ExtendedScreenHandlerType<>(
							LucyScreenHandler::new, // (int syncId, PlayerInventory, LucyScreenData)
							LucyScreenData.PACKET_CODEC
					)
			);



	public record LucyScreenData(int entityId) {
		public static final PacketCodec<RegistryByteBuf, LucyScreenData> PACKET_CODEC = PacketCodec.tuple(
				PacketCodecs.VAR_INT,  // Codec for entityId
				LucyScreenData::entityId,
				LucyScreenData::new
		);
	}


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		FabricDefaultAttributeRegistry.register(LUCY, LucyEntity.createAttributes());

		// 1. Register payload type (C2S = client to server)
		PayloadTypeRegistry.playC2S().register(LucyButtonPacket.ID, LucyButtonPacket.CODEC);

		// 2. Register server-side receiver
		ServerPlayNetworking.registerGlobalReceiver(
				LucyButtonPacket.ID,
				(packet, context) -> {
					context.player().getServer().execute(() -> {
						var entity = context.player().getWorld().getEntityById(packet.entityId());
						if (entity instanceof LucyEntity lucy) {
							switch (packet.actionId()) {
								case "sit" -> lucy.setSit(!lucy.isSittingdown());
								case "breakUp" -> lucy.disown(context.player());
								default -> BlockHard.LOGGER.warn("Unknown Lucy action: " + packet.actionId());
							}
						}
					});
				}
		);
	}
}
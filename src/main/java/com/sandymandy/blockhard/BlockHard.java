package com.sandymandy.blockhard;

import com.sandymandy.blockhard.entity.bia.BiaInit;
import com.sandymandy.blockhard.entity.lucy.LucyInit;
import com.sandymandy.blockhard.screen.GirlScreenHandler;
import com.sandymandy.blockhard.item.ModItemGroups;
import com.sandymandy.blockhard.item.ModItems;
import com.sandymandy.blockhard.util.network.ButtonPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockHard implements ModInitializer {
	public static final String MOD_ID = "blockhard";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("BlockHard");

	public static final ExtendedScreenHandlerType<GirlScreenHandler, GirlScreenData> GIRL_SCREEN_HANDLER =
			Registry.register(
					Registries.SCREEN_HANDLER,
					Identifier.of(BlockHard.MOD_ID, "lucy_screen"),
					new ExtendedScreenHandlerType<>(GirlScreenHandler::new, GirlScreenData.PACKET_CODEC)
			);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();


		PayloadTypeRegistry.playC2S().register(ButtonPacket.ID, ButtonPacket.CODEC);
		LucyInit.init();
		BiaInit.init();

	}

	public record GirlScreenData(int entityId) {
		public static final PacketCodec<RegistryByteBuf, GirlScreenData> PACKET_CODEC = PacketCodec.tuple(
				PacketCodecs.VAR_INT,
				GirlScreenData::entityId,
				GirlScreenData::new
		);
	}
}
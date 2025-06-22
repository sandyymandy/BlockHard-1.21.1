package com.sandymandy.blockhard;

import com.sandymandy.blockhard.entity.EntityInit;
import com.sandymandy.blockhard.screen.GirlScreenHandler;
import com.sandymandy.blockhard.item.ModItemGroups;
import com.sandymandy.blockhard.item.ModItems;
import net.fabricmc.api.ModInitializer;
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
	public static final Logger LOGGER = LoggerFactory.getLogger("BlockHard");

	public static final ExtendedScreenHandlerType<GirlScreenHandler, GirlScreenData> GIRL_SCREEN_HANDLER =
			Registry.register(
					Registries.SCREEN_HANDLER,
					Identifier.of(BlockHard.MOD_ID, "lucy_screen"),
					new ExtendedScreenHandlerType<>(GirlScreenHandler::new, GirlScreenData.PACKET_CODEC)
			);


	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		EntityInit.init();
	}


	public record GirlScreenData(int entityId) {
		public static final PacketCodec<RegistryByteBuf, GirlScreenData> PACKET_CODEC = PacketCodec.tuple(
				PacketCodecs.VAR_INT,
				GirlScreenData::entityId,
				GirlScreenData::new
		);
	}
}
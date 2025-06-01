package com.sandymandy.blockhard;

import com.sandymandy.blockhard.entity.custom.LucyEntity;
import com.sandymandy.blockhard.item.ModItemGroups;
import com.sandymandy.blockhard.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
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
			Identifier.of(BlockHard.MOD_ID, "lucy"), EntityType.Builder.create(LucyEntity::new, SpawnGroup.CREATURE).dimensions(.5f, 1.95f).build());

	// Initialize ScreenHandlers


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		FabricDefaultAttributeRegistry.register(LUCY, LucyEntity.createAttributes());
	}
}
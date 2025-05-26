package com.sandymandy.blockhard.entity;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.custom.LucyEnitiy;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<LucyEnitiy> LUCY = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(BlockHard.MOD_ID, "lucy"),
            EntityType.Builder.create(LucyEnitiy::new, SpawnGroup.CREATURE)
                    .dimensions(1f, 2.5f).build());

    public static void registerModEntities(){
        BlockHard.LOGGER.info("Registering Mod Entities for " + BlockHard.MOD_ID);
    }
}

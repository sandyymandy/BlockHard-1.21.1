package com.sandymandy.blockhard.item;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.entity.ModEntities;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item LUCY_SPAWN_EGG = registerItem("lucy_spawn_egg",
            new SpawnEggItem(ModEntities.LUCY, 0xf531d1, 0xa710c9, new Item.Settings()));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(BlockHard.MOD_ID, name), item);
    }

    public static void registerModItems(){
        BlockHard.LOGGER.info("Registering Mod Items for " + BlockHard.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(LUCY_SPAWN_EGG);
        });
    }
}

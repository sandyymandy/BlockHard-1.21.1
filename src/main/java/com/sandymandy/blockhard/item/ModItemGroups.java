package com.sandymandy.blockhard.item;

import com.sandymandy.blockhard.BlockHard;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup BLOCK_HARD_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(BlockHard.MOD_ID, "block_hard_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.LUCY_SPAWN_EGG))
                    .displayName(Text.translatable("itemgroup.blockhard.block_hard_items"))
                    .entries((displayContext, entries) -> {
                      entries.add(ModItems.LUCY_SPAWN_EGG);




                    }).build());

    /*public static final ItemGroup BLOCK_HARD_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(BlockHard.MOD_ID, "block_hard_blocks"),
            FabricItemGroup.builder().icon(() -> new ItemStack("a block"))
                    .displayName(Text.translatable("itemgroup.blockhard.block_hard_blocks"))
                    .entries((displayContext, entries) -> {
                        entries.add();




                    }).build()); */


    public static void registerItemGroups(){
        BlockHard.LOGGER.info("Registering Item Groups for " + BlockHard.MOD_ID);
    }
}

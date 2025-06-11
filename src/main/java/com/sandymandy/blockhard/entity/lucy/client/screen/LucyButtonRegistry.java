package com.sandymandy.blockhard.entity.lucy.client.screen;

import net.minecraft.text.Text;
import com.sandymandy.blockhard.entity.lucy.LucyEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class LucyButtonRegistry {
    public static final List<LucyButtonAction> BUTTONS = List.of(
            new LucyButtonAction(Text.literal("Sit"), (lucy, player) -> {
                lucy.setSit(!lucy.isSittingdown());
            }),

            new LucyButtonAction(Text.literal("Break Up"), (lucy, player) -> {
                lucy.disown(player);
            }),

            new LucyButtonAction(Text.literal("Say Hi"), (lucy, player) -> {
                player.sendMessage(Text.literal("Lucy waves at you!"), true);
            })
    );
}
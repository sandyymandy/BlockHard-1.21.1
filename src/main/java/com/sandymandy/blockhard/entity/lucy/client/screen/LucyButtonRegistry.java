package com.sandymandy.blockhard.entity.lucy.client.screen;

import com.sandymandy.blockhard.entity.lucy.network.LucyButtonPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import com.sandymandy.blockhard.entity.lucy.LucyEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class LucyButtonRegistry {
    public static final List<LucyButtonAction> BUTTONS = List.of(
            new LucyButtonAction(Text.literal("Sit"), (lucy, player) -> {
                ClientPlayNetworking.send(new LucyButtonPacket(lucy.getId(), "sit"));
            }),

            new LucyButtonAction(Text.literal("Break Up"), (lucy, player) -> {
                ClientPlayNetworking.send(new LucyButtonPacket(lucy.getId(), "breakUp"));
            }),

            new LucyButtonAction(Text.literal("Say Hi"), (lucy, player) -> {
                player.sendMessage(Text.literal("Lucy waves at you!"), true);
            })
    );
}
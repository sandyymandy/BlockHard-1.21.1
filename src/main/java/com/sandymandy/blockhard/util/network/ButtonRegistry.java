package com.sandymandy.blockhard.util.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

import java.util.List;

public class ButtonRegistry {
    public static final List<ButtonAction> BUTTONS = List.of(
            new ButtonAction(Text.literal("Strip"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "stripOrDressup"));
            }),

            new ButtonAction(Text.literal("Break Up"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "breakUp"));
            }),

            new ButtonAction(Text.literal("Set Base Here"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "setBase"));
            }),

            new ButtonAction(Text.literal("Go To Base"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "goToBase"));
            }),

            new ButtonAction(Text.literal("Say Hi"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "startScene"));
            })
    );
}